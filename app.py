import pyrebase, json, os, random
from requests.exceptions import HTTPError
from flask import Flask, send_from_directory, request
from flask_restful import Api
from flask_jwt_extended import JWTManager, create_access_token, jwt_required, get_jwt_identity
from dotenv import load_dotenv, dotenv_values
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
# cred = credentials.Certificate("key/priv_key.json")
# firebase_admin.initialize_app(cred, {'storageBucket': 'side-project-404814.appspot.com'})

# model = load_model()

def generatePrivateUniqueId(length, adder=''):
    characters = 'qwertyuiopasdfghjklzxcvbnm' + '1234567890'
    unique_id = ''.join(random.choices(characters, k=length))
    if len(adder) > 0:
        return adder+"-"+unique_id
    else:
        return unique_id

def registerMethod(email, password):
    try:
        user = auth.create_user_with_email_and_password(email, password)
        registerDetail = auth.get_account_info(user['idToken'])['users'][0]
        userData = {
            'localId': registerDetail['localId'],
            'email': request.form['email'],
            'role': 1,
            'username': str(request.form['email']).split('@')[0] if not 'username' in request.form else request.form['username'],
            'registeredAt': datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        }
        userNameByEmail = True if not 'username' in request.form else False
        return {
            'msg': 'CREATED', 
            'desc': 'Successfully registered!', 
            'registerDetail': db.child('users').child(registerDetail['localId']).set(userData),
            'userNameByEmail': userNameByEmail
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
            'apiToken':apiToken,
            'loginDetail':loginDetail
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
            return registerMethod(email, password)
    except Exception as e:
            return {
                'status': {
                    'code': 400,
                    'msg': 'Bad Request',
                    'errMsg': str(e)
                }
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
    
    imageUrl = storage.child(imagePath).get_url(userId)
    data = {
        'imagePath': imagePath,
        'imageUrl': imageUrl,
        'name': name,
        'productId': productId,
        'ownedBy': userId,
        'price': int(price),
        'desc': desc
    }
    db.child('users').child(userId).child('products').child(productId).set(data)

    # return {
    #     'status': {
    #         'code': 200,
    #         'msg': "Product has been uploaded"
    #     },
    #     'data': data
    # }, 200
    return data

@app.route('/products', methods=['GET', 'POST', 'DELETE'])
@jwt_required()
async def products():
    encoded_id = get_jwt_identity()
    decoded_id = base64.urlsafe_b64decode(encoded_id)
    userId = (cipher_suite.decrypt(decoded_id).decode())
    if userId:
        if request.method == 'GET':
            productId = request.args.get('id')
            try:
                myProducts = list(map(lambda x: db.child('users').child(userId).child('products').child(x).get().val(), list(db.child('users').child(userId).child('products').get().val())))
                myProduct = db.child('users').child(userId).child('products').child(productId).get().val()
                if productId is None:
                    return {
                        'myProducts': myProducts,
                    }, 200
                else:
                    return {
                        'myProduct': myProduct,
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
      
        elif request.method == 'POST':
            result = await asyncio.gather(process_upload(request, userId))
            return {
                'status': {
                    'code': 201,
                    'msg': 'Product has been uploaded'
                },
                'data' : result[0]
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