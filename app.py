import pyrebase, json, datetime, os, random
from time import sleep
from requests.exceptions import HTTPError
from flask import Flask, render_template, request, jsonify, redirect, url_for
from flask_restful import Api, Resource
from flask_jwt_extended import JWTManager, create_access_token, jwt_required, get_jwt_identity
from dotenv import load_dotenv, dotenv_values
import firebase_admin
from firebase_admin import credentials, db, storage
from cryptography.fernet import Fernet
import base64
from flask_cors import CORS
import asyncio
from werkzeug.utils import secure_filename

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

cred = credentials.Certificate("key/priv_key.json")
firebase_admin.initialize_app(cred, {'storageBucket': 'side-project-404814.appspot.com'})

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
            'role': int(request.form['role']),
            'username': str(request.form['email']).split('@')[0] if not 'username' in request.form else request.form['username'],
            'registeredAt': datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        }
        userNameByEmail = True if not 'username' in request.form else False
        return {
            'message': 'CREATED', 
            'desc': 'Successfully registered!', 
            'registerDetail': db.child('users').child(registerDetail['localId']).set(userData),
            'userNameByEmail': userNameByEmail
        }
    except HTTPError as e:
        errMsg = json.loads(e.strerror)['error']['message']
        if errMsg == 'EMAIL_EXISTS':
            return {'message':errMsg, 'desc':'Email is already existed!'}
        else:
            return {'message':errMsg}

def loginMethod(email, password):
    try:
        user = auth.sign_in_with_email_and_password(email, password)
        loginDetail = auth.get_account_info(user['idToken'])['users'][0]
        del loginDetail['providerUserInfo']
        encrypted_id = cipher_suite.encrypt(loginDetail['localId'].encode())
        encoded_id = base64.urlsafe_b64encode(encrypted_id).decode()
        apiToken = create_access_token(identity=encoded_id)
        return {
            'message': 'OK', 
            'desc': 'Successfully signed in!',
            'apiToken':apiToken,
            'loginDetail':loginDetail
        }
    except HTTPError as e:
        errMsg = json.loads(e.strerror)['error']['message']
        if errMsg == 'INVALID_LOGIN_CREDENTIALS':
            return {'message':errMsg, 'desc':'Password is incorrect!'}
        else:
            return {'message':errMsg}

def imageMethod(fileName): # UPLOAD
    cloudStorageFormat = f"images/{datetime.datetime.now().strftime('%Y%m%d-%H%M%S')}{os.path.splitext(fileName)[1].lower()}"
    storage.child(cloudStorageFormat).put(fileName)
    return cloudStorageFormat

# API ROUTES

app = Flask(__name__)
app.config['JWT_SECRET_KEY'] = Fernet.generate_key()
cipher_suite = Fernet(app.config['JWT_SECRET_KEY'])

api = Api(app)
jwt = JWTManager(app)
CORS(app)

@app.route('/')
def host():
    return jsonify(
        {
            'status': {
                'code': 200,
                'message': 'Flask REST API is working!'
            }
        }
    ), 200

# @app.route('/home')
# def home():
#     return render_template('index.html', title="Bogareksa Home")

@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method in ['GET', 'POST']:
        if request.method == 'GET':
            return jsonify({
                'status': {
                    'code': 200,
                    'message': 'Login route is working'
                }
            }), 200
        elif request.method == 'POST':
            email = request.form['email']
            password = request.form['password']
            return loginMethod(email, password), 200
    else:
        return jsonify({
            'status': {
                'code': 400,
                'message': 'Nah, your request aren\'t processed!'
            }
        }), 400

@app.route('/register', methods=['GET', 'POST'])
def register():
    if request.method in ['GET', 'POST']:
        if request.method == 'GET':
            return render_template('register.html')
        elif request.method == 'POST':
            email = request.form['email']
            password = request.form['password']
            return registerMethod(email, password), 201
    else:
        return jsonify({
            'status': {
                'code': 400,
                'message': 'Bad Request'
            }
        }), 400

BLACKLIST = set()

# Token blacklisting callback
# @jwt.token_in_blocklist_loader
# def check_if_token_in_blocklist(decrypted_token):
#     jti = decrypted_token['jti']
#     return jti in BLACKLIST

# # Route to logout and blacklist the token
# @app.route('/logout', methods=['POST'])
# @jwt_required()
# def logout():
#     jti = get_jwt_identity()
#     BLACKLIST.add(jti)
#     return jsonify({"msg": "Successfully logged out"})

# @app.route('/auth_status')
# def auth_status():
#     if 'loggedIn':
#         return jsonify(
#             {
#                 'status': {
#                     'code': 200,
#                     'message': 'Authenticated'
#                 },
#                 'apiToken': get_jwt_identity()
#             }
#         ), 200
#     else:
#         return jsonify(
#             {
#                 'status': {
#                     'code': 401,
#                     'message': 'Unauthenticated'
#                 }
#             }
#         ), 401

async def process_upload(request, userId):
    productId = generatePrivateUniqueId(length=5)
    uploadedFile = request.files['uploadedFile']
    name = request.form['name']
    price = request.form['price']
    desc = "" if not 'desc' in request.form else request.form['desc']

    current_datetime = datetime.datetime.now().strftime("%Y%m%d_%H%M%S")
    file_extension = uploadedFile.filename.split('.')[-1]
    new_filename = f"{current_datetime}.{file_extension}"

    # Upload image to Firebase Storage with the new filename
    imagePath = f"images/{secure_filename(new_filename)}"
    storage.child(imagePath).put(uploadedFile)

    # Get the URL of the uploaded image
    imageUrl = storage.child(imagePath).get_url(userId)
    data = db.child('products').child(f"productId-{productId}").set({
        'imagePath': imagePath,
        'imageUrl': imageUrl,
        'name': name,
        'productId': productId,
        'ownedBy': userId,
        'price': int(price),
        'desc': desc
    })
    db.child('users').child(userId).child('productsOwned').child(productId).set(productId)

    return {
        'status': {
            'code': 200,
            'message': "Product has been uploaded"
        },
        'data': data
    }

@app.route('/products', methods=['GET', 'POST'])
@jwt_required()
async def products():
    encoded_id = get_jwt_identity()
    decoded_id = base64.urlsafe_b64decode(encoded_id)
    userId = (cipher_suite.decrypt(decoded_id).decode())
    if userId:
        if request.method == 'GET':
            productId = request.args.get('id')
            myProducts = list(map(lambda x: db.child('products').child(f"productId-{x}").get().val(), db.child('users').child(userId).child('productsOwned').get().val()))
            myProduct = list(filter(lambda x: x['productId'] == productId, myProducts))[0] if productId is not None else []
            if productId is None:
                return jsonify({
                    "myProducts": myProducts,
                }), 200
            else:
                return jsonify({
                    "myProduct": myProduct,
                }), 200
      
        elif request.method == 'POST':
            result = await asyncio.gather(process_upload(request, userId))

            return jsonify(result[0]), 201            
    else:
        return redirect(url_for('auth_status'))

@app.route("/upload-image", methods=["GET", "POST"])
def upload_image():
    userId = "STdZUZOxxbPG1dJ8bK6i6ywboez1"
    if request.method == "POST":
        if request.files:
            productId = generatePrivateUniqueId(length=5)
            uploadedFile = request.files['uploadedFile']
            name = request.form['name']
            price = request.form['price']
            desc = "" if not 'desc' in request.form else request.form['desc']

            imagePath = f"images/{secure_filename(uploadedFile.filename)}"
            image_blob = storage.bucket().blob(imagePath)
            image_blob.upload_from_file(uploadedFile)

            # Get the URL of the uploaded image
            imageUrl = image_blob.public_url
            db.child('products').child(f"productId-{productId}").set({
                'imagePath': imagePath,
                'imageUrl': imageUrl,
                'name': name,
                'productId': productId,
                'ownedBy': userId,
                'price': int(price),
                'desc': desc
            })
            db.child('users').child(userId).child('productsOwned').child(productId).set(productId)
            return redirect(request.url)
    return render_template("upload_image.html")
    
if __name__ == '__main__':
    app.run(debug=True, host=os.getenv("HOST"), port=os.getenv("PORT"))