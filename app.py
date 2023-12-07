import pyrebase, json, datetime, os, random, firebase_admin
from time import sleep
from requests.exceptions import HTTPError
from flask import Flask, render_template, request, jsonify, redirect, url_for, session
from dotenv import load_dotenv, dotenv_values
from firebase_admin import credentials, db
import asyncio

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

def generatePrivateUniqueId(adder, length):
    characters = 'qwertyuiopasdfghjklzxcvbnm' + '1234567890'
    unique_id = ''.join(random.choices(characters, k=length))
    return adder+"-"+unique_id.lower()

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
        result = {
            'message':'CREATED', 
            'desc':'Successfully registered!', 
            'registerDetail': db.child('users').child(registerDetail['localId']).set(userData),
            'userNameByEmail': userNameByEmail
        }
        return result
    except HTTPError as e:
        errMsg = json.loads(e.strerror)['error']['message']
        if errMsg == 'EMAIL_EXISTS':
            return {'message':errMsg, 'desc':'Email is already existed!'}
        else:
            return {'message':errMsg}
        
def loginMethod(email, password):
    try:
        user = auth.sign_in_with_email_and_password(email, password)
        loginDetail = auth.get_account_info(user['idToken'])
        session['loggedIn'] = True
        session['userId'] = loginDetail['users'][0]['localId']
        return {'message':'OK', 'desc':'Successfully signed in!', 'loginDetail':loginDetail['users'][0]}
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

app = Flask(__name__)
app.secret_key = os.getenv('SECRET_KEY')

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

@app.route('/home')
def home():
    return render_template('index.html', title="Bogareksa Home")

@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method in ['GET', 'POST']:
        if request.method == 'GET':
            return render_template('login.html')
        elif request.method == 'POST':
            email = request.form['email']
            password = request.form['password']
            return loginMethod(email, password), 200
    else:
        return jsonify({
            'status': {
                'code': 400,
                'message': 'Nah, your request aren\'t processed!'
            },
            'data': 1,
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
                'message': 'Nah, your request aren\'t processed!'
            },
            'data': 1,
        }), 400

@app.route('/logout')
def logout():
   session.pop('loggedIn', None)
   session.pop('id', None)
   return redirect(url_for('login'))

@app.route('/auth_status')
def auth_status():
    if 'loggedIn' in session:
        return jsonify(
            {
                'status': {
                    'code': 200,
                    'message': 'Authenticated'
                },
                'userId': session['userId']
            }
        ), 200
    else:
        return jsonify(
            {
                'status': {
                    'code': 401,
                    'message': 'Unauthenticated'
                }
            }
        ), 401

async def process_upload(request, session):
    productId = generatePrivateUniqueId('productId', 5)
    uploadedFile = request.form['uploadedFile']
    name = request.form['name']
    price = request.form['price']
    desc = "" if not 'desc' in request.form else request.form['desc']

    imagePath = imageMethod(uploadedFile)
    imageUrl = storage.child(imagePath).get_url(session['userId'])
    data = db.child('products').child(productId).set({
        'imagePath': imagePath,
        'imageUrl': imageUrl,
        'name': name,
        'productId': productId,
        'ownedBy': session['userId'],
        'price': price,
        'desc': desc
    })
    # lastIdByUser = 0 if db.child('users').child(session['userId']).child('productsOwned').get().val() == None else db.child('users').child(session['userId']).child('productsOwned').get().val().__len__()
    db.child('users').child(session['userId']).child('productsOwned').child(productId).set(productId)

    return {
        'status': {
            'code': 200,
            'message': "Product has been uploaded"
        },
        'data': data
    }

@app.route('/products', methods=['GET', 'POST'])
async def products():
    # lastId = db.child('products').child('lastId').get().val()
    if 'loggedIn' in session:
        # lastIdByUser = 0 if db.child('users').child(session['userId']).child('productsOwned').get().val() == None else db.child('users').child(session['userId']).child('productsOwned').get().val().__len__()
        if request.method == 'GET':
            productId = request.args.get('id')
            myProducts = list(map(lambda x: db.child('products').child(x).get().val(), db.child('users').child(session['userId']).child('productsOwned').get().val()))
            if productId is None:
                return jsonify({
                    "myProducts": myProducts,
                }), 200
            else:
                return jsonify({
                    "myProduct": list(filter(lambda x: x['productId'] == int(productId), myProducts)),
                }), 200
            
        elif request.method == 'POST':
            result = await asyncio.gather(process_upload(request, session))

            return jsonify(result[0]), 201
            # uploadedFile = request.form['uploadedFile']
            # name = request.form['name']
            # desc = "" if not 'desc' in request.form else request.form['desc']

            # imagePath = imageMethod(uploadedFile)
            # imageUrl = storage.child(imagePath).get_url(session['userId'])
            # data = db.child('products').child(f'productId-{lastId+1}').set({
            #     'imagePath':imagePath,
            #     'imageUrl':imageUrl,
            #     'name':name,
            #     'productId': lastId+1,
            #     'ownedBy':session['userId'],
            #     'desc':desc
            # })
            # db.child('products').child('lastId').set(lastId+1)
            # db.child('users').child(session['userId']).child('productsOwned').child(lastIdByUser).set(f'productId-{lastId+1}')
            
            # return jsonify({
            #     'status' : {
            #         'code' : 200,
            #         'message': "Product has been uploaded"
            #     },
            #     'data': data
            # }), 201
            
    else:
        return redirect(url_for('auth_status'))
    
if __name__ == '__main__':
    app.run(debug=True, host=os.getenv("HOST"), port=os.getenv("PORT"))