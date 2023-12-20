import pyrebase, json, os, random
from requests.exceptions import HTTPError
from flask import Flask, send_from_directory, request, send_file, jsonify
from flask_restful import Api
from flask_jwt_extended import JWTManager, create_access_token, jwt_required, get_jwt_identity
from dotenv import load_dotenv, dotenv_values
import uuid
# import firebase_admin
# from firebase_admin import credentials, db, storage
from cryptography.fernet import Fernet
import base64
from flask_cors import CORS
import asyncio
from werkzeug.utils import secure_filename

# ML MODULES

import cv2
import numpy as np
import re

from io import BytesIO

from datetime import datetime
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing.image import img_to_array
from google.cloud import vision
from google.oauth2.service_account import Credentials

load_dotenv()

firebaseConfig = {
  'apiKey': os.getenv("API_KEY"),
  'authDomain': os.getenv("AUTH_DOMAIN"),
  'projectId': os.getenv("PROJECT_ID"),
  'storageBucket': os.getenv("STORAGE_BUCKET"),
  'messagingSenderId': os.getenv("MESSAGE_SENDER_ID"),
  'appId': os.getenv("APP_ID"),
  'measurementId': os.getenv("MEASUREMENT_ID"),
  'databaseURL': os.getenv("DB_URL"),
};

firebase = pyrebase.initialize_app(firebaseConfig)

auth = firebase.auth()
storage = firebase.storage()
db = firebase.database()

app = Flask(__name__)
app.config['JWT_SECRET_KEY'] = Fernet.generate_key()
app.config['ALLOWED_EXTENSIONS'] = {'png', 'jpg', 'jpeg'}
app.config['UPLOAD_FOLDER'] = 'static/uploads/'
app.config['MODEL_FILE'] = 'models/fine_tuned_detector.h5'
app.config['GOOGLE_CLOUD_KEY_FILE'] = 'key/cloudvis_key.json'

model = load_model(app.config['MODEL_FILE'], compile=False)

creds = Credentials.from_service_account_file(app.config['GOOGLE_CLOUD_KEY_FILE'])
vision_client = vision.ImageAnnotatorClient(credentials=creds)

cipher_suite = Fernet(app.config['JWT_SECRET_KEY'])

api = Api(app)
jwt = JWTManager(app)
CORS(app)

def generatePrivateUniqueId(length, adder=''):
    characters = 'qwertyuiopasdfghjklzxcvbnm' + '1234567890'
    unique_id = ''.join(random.choices(characters, k=length))
    if len(adder) > 0:
        return adder+"-"+unique_id
    else:
        return unique_id

def registerMethod(email, password, username, storename):
    try:
        user = auth.create_user_with_email_and_password(email, password)
        registerDetail = auth.get_account_info(user['idToken'])['users'][0]
        userData = {
            'localId': registerDetail['localId'],
            'email': email,
            'role': 1,
            'username': username,
            'storename': storename,
            'registeredAt': datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        }
        return {
            'msg': 'CREATED', 
            'desc': 'Successfully registered!', 
            'registerDetail': db.child('users').child(registerDetail['localId']).set(userData)
        }, 201
    except HTTPError as e:
        errMsg = json.loads(e.strerror)['error']['message']
        if errMsg == 'EMAIL_EXISTS':
            return {
                'msg':errMsg, 
                'desc':'Email is already existed!'
            }, 400
        else:
            return {
                'msg':errMsg
            }, 400

def loginMethod(email, password):
    try:
        user = auth.sign_in_with_email_and_password(email, password)
        loginDetail = auth.get_account_info(user['idToken'])['users'][0]
        del loginDetail['providerUserInfo']
        loginDetail['role'] = db.child('users').child(loginDetail['localId']).child('role').get().val()
        encrypted_id = cipher_suite.encrypt(loginDetail['localId'].encode())
        encoded_id = base64.urlsafe_b64encode(encrypted_id).decode()
        apiToken = create_access_token(identity=encoded_id)
        return {
            'msg': 'OK', 
            'desc': 'Successfully signed in!',
            'apiToken': apiToken,
            'loginDetail': loginDetail
        }, 200
    except HTTPError as e:
        errMsg = json.loads(e.strerror)['error']['message']
        if errMsg == 'INVALID_LOGIN_CREDENTIALS':
            return {
                'msg':errMsg, 
                'desc':'Password is incorrect!'
            }, 400
        else:
            return {
                'msg':errMsg
            }, 400

# API ROUTES

@app.route('/')
def host():
    return {
            'status': {
                'code': 200,
                'msg': 'Flask REST API is working!'
            }
        }, 200

@app.route('/favicon.ico')
def favicon():
    return send_from_directory(os.path.join(app.root_path, 'static'), 'favicon.ico', mimetype='image/vnd.microsoft.icon')

@app.route('/login', methods=['GET', 'POST'])
def login():
    try:
        if request.method == 'GET':
            return {
                'status': {
                    'code': 200,
                    'msg': 'Login route is working'
                }
            }, 200
        elif request.method == 'POST':
            email = request.form['email']
            password = request.form['password']
            return loginMethod(email, password)
    except Exception as e:
        return {
            'status': {
                'code': 400,
                'msg': 'Bad Request',
                'errMsg': str(e)
            }
        }, 400

@app.route('/register', methods=['GET', 'POST'])
def register():
    try:
        if request.method == 'GET':
            return {
                'status': {
                    'code': 200,
                    'msg': 'Register route is working'
                }
            }, 200
        elif request.method == 'POST':
            email = request.form['email']
            password = request.form['password']
            username = str(request.form['email']).split('@')[0] if not 'username' in request.form else request.form['username']
            storename = str(request.form['email']).split('@')[0] if not 'storename' in request.form else request.form['storename']
            return registerMethod(email, password, username, storename)
    except Exception as e:
            return {
                'status': {
                    'code': 400,
                    'msg': 'Bad Request',
                    'errMsg': str(e)
                }
            }, 400

def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in app.config['ALLOWED_EXTENSIONS']

def extract_date_from_text(text):
    # Use regular expression to find a date pattern in the text
    date_match = re.search(r'\b\d{1,2}\.\d{1,2}\.\d{4}\b|\b\d{1,2}\.\d{1,2}\b|\b\d{1,2}\.\d{4}\b|\b\d{4}\b', text)

    if date_match:
        detected_date_str = date_match.group(0)
        try:
            # Parse the detected date
            if len(detected_date_str) == 4:
                # If only the year is detected
                detected_date = datetime.strptime(detected_date_str, '%Y')
            elif len(detected_date_str) == 7:
                # If month and year are detected
                detected_date = datetime.strptime(detected_date_str, '%m.%Y')
            elif '.' in detected_date_str:
                # If day and month, or full date, are detected
                detected_date = datetime.strptime(detected_date_str, '%d.%m.%Y')
            else:
                # If only day and month are detected
                detected_date = datetime.strptime(detected_date_str, '%d.%m')

            return detected_date
        except ValueError:
            # print("Detected date is not a valid date.")
            return None

    else:
        # print("No date pattern found in the text.")
        return None

def predict(image):
    try:
        # Convert the image to a NumPy array
        nparr = np.frombuffer(image.read(), np.uint8)
        img = cv2.imdecode(nparr, cv2.IMREAD_UNCHANGED)

        # Preprocess the image for the object detection model
        input_image = cv2.resize(img, (224, 224))
        input_image = img_to_array(input_image) / 255.0
        input_image = np.expand_dims(input_image, axis=0)

        # Make bounding box predictions on the input image
        preds = model.predict(input_image)[0]
        (startX, startY, endX, endY) = preds

        # Scale the predicted bounding box coordinates based on the image dimensions
        (h, w) = img.shape[:2]
        startX = int(startX * w)
        startY = int(startY * h)
        endX = int(endX * w)
        endY = int(endY * h)

        # Define scale factors
        scale_factor_width = 1.5
        scale_factor_height = 4

        # Calculate the center of the bounding box
        centerX, centerY = (startX + endX) // 2, (startY + endY) // 2

        # Calculate the new dimensions of the bounding box
        width = int((endX - startX) * scale_factor_width)
        height = int((endY - startY) * scale_factor_height)

        # Recalculate the new starting and ending coordinates
        startX = max(0, centerX - width // 2)
        startY = max(0, centerY - height // 2)
        endX = min(w, centerX + width // 2)
        endY = min(h, centerY + height // 2)

        # Draw the bounding box on the image
        cv2.rectangle(img, (startX, startY), (endX, endY), (0, 255, 0), 2)

        # Crop the image based on the adjusted bounding box coordinates
        cropped_image = img[startY:endY, startX:endX]

        # Encode the cropped image to JPEG format
        _, buffer = cv2.imencode('.jpg', cropped_image)
        cropped_image_bytes = buffer.tobytes()

        # Perform text detection using Google Cloud Vision API
        image_content = BytesIO(cropped_image_bytes).read()
        image = vision.Image(content=image_content)
        response = vision_client.text_detection(image=image)
        text_annotations = response.text_annotations

        # Check if any text is detected
        if text_annotations:
            text = text_annotations[0].description

            # Print the detected text
            # print(f"Detected Text: {text}")

            # Extract and validate date from the text
            detected_date = extract_date_from_text(text)

            if detected_date:
                # Check if the detected date is expired
                current_date = datetime.now()
                if detected_date < current_date:
                    # print("Detected date is expired.")
                    return {
                        "status": {
                            "code": 200,
                            "message": "Detected date is expired.",
                            "detected_date": detected_date.strftime('%Y-%m-%d')
                        },
                        "data": None,
                    }, 200
                else:
                    # print("Detected date is valid.")
                    # return BytesIO(cropped_image_bytes), {
                    #     "status": {
                    #         "code": 200,
                    #         "message": "Detected date is valid.",
                    #         "detected_date": detected_date.strftime('%Y-%m-%d')
                    #     },
                    #     "data": None,
                    # }
                    return  {
                        "status": {
                            "code": 200,
                            "message": "Detected date is valid.",
                            "detected_date": detected_date.strftime('%Y-%m-%d')
                        },
                        "data": None,
                    }
            else:
                return {
                    "status": {
                        "code": 200,
                        "message": "Detected date is not a valid date.",
                        "detected_date": None
                    },
                    "data": None,
                }, 200

        else:
            # print("No text detected.")
            return {
                "status": {
                    "code": 200,
                    "message": "No text detected.",
                    "detected_date": None
                },
                "data": None,
            }, 200

    except cv2.error as e:
        # print(f"Error decoding image: {e}")
        return {
            "status": {
                "code": 400,
                "message": "Error decoding image. Please make sure the file is a valid image.",
                "detected_date": None
            },
            "data": None,
        }, 400

    except ValueError as e:
        # print(f"Error: {e}")
        return {
            "status": {
                "code": 400,
                "message": "Empty file. Please upload a non-empty image file.",
                "detected_date": None
            },
            "data": None,
        }, 400

@app.route("/prediction", methods=["GET", "POST"])
def prediction_route():
    if request.method == "POST":
        if 'uploadedFile' not in request.files:
            return {
                "status": {
                    "code": 400,
                    "message": "No file part"
                },
                "data": None,
            }, 400

        image = request.files["uploadedFile"]
        print(f"Type of 'image': {type(image)}")

        if image.filename == '':
            return {
                "status": {
                    "code": 400,
                    "message": "No selected file"
                },
                "data": None,
            }, 400

        if image and allowed_file(image.filename):
            filename = secure_filename(image.filename)
            #image.save(os.path.join(app.config["UPLOAD_FOLDER"], filename))
            image_path = os.path.join(app.config["UPLOAD_FOLDER"], filename)

            # Reset the cursor position to the beginning of the file content
            image.seek(0)

            # Call the predict function to get the processed image and text
            cropped_image, detected_text = predict(image)

            # Check if the response is a BytesIO object
            if isinstance(cropped_image, BytesIO):
                # Rewind the BytesIO object to the beginning
                cropped_image.seek(0)

                # Send the BytesIO object as a file response
                return send_file(cropped_image, mimetype='image/jpeg', download_name='cropped_image.jpg', as_attachment=True)

            # If the predict function returns an error response, return that response
            return cropped_image

        else:
            return {
                "status": {
                    "code": 400,
                    "message": "Invalid file format. Please upload a JPG, JPEG, or PNG image."
                },
                "data": None,
            }, 400
    else:
        return {
            "status": {
                "code": 405,
                "message": "Method not allowed"
            },
            "data": None,
        }, 405

def prediction(image):
    if image and allowed_file(image.filename):
        filename = secure_filename(image.filename)
        # image.save(os.path.join(app.config["UPLOAD_FOLDER"], filename))
        # image_path = os.path.join(app.config["UPLOAD_FOLDER"], filename)

        # Reset the cursor position to the beginning of the file content
        image.seek(0)

        # Call the predict function to get the processed image and text
        cropped_image, detected_text = predict(image)

        # Check if the response is a BytesIO object
        if isinstance(cropped_image, BytesIO):
            # Rewind the BytesIO object to the beginning
            cropped_image.seek(0)

            # Send the BytesIO object as a file response
            return send_file(cropped_image, mimetype='image/jpeg', download_name='cropped_image.jpg', as_attachment=True)

        # If the predict function returns an error response, return that response
        return cropped_image

    else:
        return {
            "status": {
                "code": 400,
                "message": "Invalid file format. Please upload a JPG, JPEG, or PNG image."
            },
            "data": None,
        }, 400

async def process_upload(request, userId):
    productId = generatePrivateUniqueId(length=5)
    uploadedFile = request.files['uploadedFile']
    name = request.form['name']
    price = request.form['price']
    desc = "" if not 'desc' in request.form else request.form['desc']

    new_filename = f"{datetime.now().strftime('%Y%m%d_%H%M%S')}.{uploadedFile.filename.split('.')[-1]}"
    imagePath = f"images/{secure_filename(new_filename)}"

    storage.child(imagePath).put(uploadedFile)
    
    predictedData = prediction(uploadedFile)

    imageUrl = storage.child(imagePath).get_url(userId)

    predictedData = {
        'detectedDate': predictedData['status']['detected_date'],
        'message': predictedData['status']['message'],
        'data': predictedData['data']
    }
    data = {
        'imagePath': imagePath,
        'imageUrl': imageUrl,
        'name': name,
        'productId': productId,
        'ownerId': userId,
        'price': int(price),
        'desc': desc,
    }

    db.child('products').child(productId).set(data)
    db.child('users').child(userId).child('products').child(len(db.child('users').child('VQk0MxKZnCg301nJrONHo1gZpZx2').child('products').get().val())).set(productId)

    return data

def get_all_products():
    all_products = []

    users = db.child("users").get()
    for user_id, user_data in users.val().items():
        products = user_data.get("products", {})
        for product_id, product_data in products.items():
            all_products.append(product_data)

    return all_products

@app.route('/products', methods=['GET', 'POST', 'DELETE', 'PUT'])
@jwt_required()
async def products():
    encoded_id = get_jwt_identity()
    decoded_id = base64.urlsafe_b64decode(encoded_id)
    userId = (cipher_suite.decrypt(decoded_id).decode())
    if userId:
        # GET REQUEST
        if request.method == 'GET':
            productId = request.args.get('id')
            try:
                if productId is None:
                    return {
                        'myProducts': list(
                                        map(
                                            lambda x: db.child('products').child(x).get().val(),
                                            db.child('users').child(userId).child('products').get().val()
                                        )
                                    ),
                        }, 200
                else:
                    return {
                        'myProduct': dict(
                                        filter(
                                            lambda x: x[1]['ownerId'] == userId and x[0] == productId,
                                            list(db.child('products').get().val().items())
                                        )
                                    )
                        }, 200
            except Exception as e:
                if '\'NoneType\' object is not iterable' == str(e):
                    return {
                        'myProducts':None
                    }, 200
                else:
                    return {
                        'errMsg': str(e)
                    }, 400
      
        # POST REQUEST
        elif request.method == 'POST':
            try:
                result = await asyncio.gather(process_upload(request, userId))
                return {
                    'status': {
                        'code': 201,
                        'msg': 'Product has been uploaded'
                    },
                    'data' : result[0]
                }, 201
            except Exception as e:
                return {
                    'status': {
                        'code': 400,
                        'msg': 'There is an error',
                    },
                    'errMsg': str(e)
                }
        elif request.method == 'DELETE':
            productId = request.args.get('id')
            deleteAll = request.args.get('all')
            if productId is None and deleteAll is None:
                return {
                    'msg':'Nothing to delete'
                }, 200
            elif deleteAll == 'true':
                allMyProducts = list(map(lambda x: db.child('users').child(userId).child('products').child(x).child('imagePath').get().val(), list(db.child('users').child(userId).child('products').get().val())))
                for i in allMyProducts:
                    storage.delete(i, userId)
                db.child('users').child(userId).child('products').remove()
                return {
                    'msg': 'All products have been requested to delete'
                }, 200
            else:
                getFilePath = db.child('users').child(userId).child('products').child(productId).child('imagePath').get().val()
                storage.delete(getFilePath, userId)
                db.child('users').child(userId).child('products').child(productId).remove()
                return {
                    'filePath': getFilePath,
                    'msg':'Product has been deleted'
                }, 200
    else:
            return {
                'status': 401,
                'msg': 'Unauthorized'
            }, 401

@app.route('/prediction', methods=['GET', 'POST'])
def prediction():
    ...

@app.route('/user', methods=['GET', 'PUT', 'DELETE'])
def currentUser():
    ...

@app.route('/get-url', methods=['GET'])
def getUrlPath():
    # print(storage.child(request.args.get('path')).get_url(uuid.uuid4()))
    # return storage.child(request.args.get('path')).get_url(uuid.uuid4())
    return {
        "res": len(db.child('users').child('VQk0MxKZnCg301nJrONHo1gZpZx2').child('products').get().val())
    }
    #len(list(db.child('users').child('VQk0MxKZnCg301nJrONHo1gZpZx2').child('products').get().val()))
# @app.route('/gets', methods=['GET'])
# def getters():
#     productId = request.args.get('id')
#     userId = request.args.get('user')
#     # return list(
#     #     filter(
#     #         lambda x: db.child('products').child(productId).get().val()
#     #         if x['ownerId'] == userId else 0,
#     #         db.child('products').order_by_child('ownerId').get().val()
#     #     )
#     # )
#     res = dict(
#             filter(
#                 lambda val: val[1]['ownerId'] == userId and val[0] == productId,
#                 list(db.child('products').get().val().items())
#             )
#         )
#     # return {"result": list(db.child('products').get().val().items()) }, 200
#     return {"result": res}, 200

@app.route('/all-products', methods=['GET'])
def allProducts():
    return list(
        map(lambda x: x[1],
            db.child('products').get().val().items()
        )
    )

@app.route('/reset-password', methods=['GET', 'POST']) # Need Token Auth...
def resetPassword():
    try:
        if request.method == 'GET':
            return {
                'status': {
                    'code': 200,
                    'msg': 'Unconfigured'
                }
            }, 200
        elif request.method == 'POST':
            return {
                'status': {
                    'code': 200,
                    'msg': 'Unconfigured'
                }
            }, 200
    except Exception as e:
        return {
            'status': {
                'code': 400,
                'msg': 'Bad Request',
                'errMsg': e
            }
        }, 400

if __name__ == '__main__':
    app.run(debug=True, host=os.getenv("HOST"), port=os.getenv("PORT"))