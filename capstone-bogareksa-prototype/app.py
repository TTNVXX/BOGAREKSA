from flask import Flask, request, jsonify, send_file, send_from_directory
from werkzeug.utils import secure_filename
import os
from io import BytesIO
import cv2
import numpy as np
import re
from datetime import datetime
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing.image import img_to_array
from google.cloud import vision
from google.oauth2.service_account import Credentials

app = Flask(__name__)
app.config['ALLOWED_EXTENSIONS'] = {'png', 'jpg', 'jpeg'}
app.config['UPLOAD_FOLDER'] = 'static/uploads/'
app.config['MODEL_FILE'] = 'fine_tuned_detector.h5'
app.config['GOOGLE_CLOUD_KEY_FILE'] = 'advance-branch-407606-b4fb43b5b28c.json'

# Load the object detection model
model = load_model(app.config['MODEL_FILE'], compile=False)

# Initialize Google Cloud Vision API client
creds = Credentials.from_service_account_file(app.config['GOOGLE_CLOUD_KEY_FILE'])
vision_client = vision.ImageAnnotatorClient(credentials=creds)

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
            print("Detected date is not a valid date.")
            return None

    else:
        print("No date pattern found in the text.")
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
            print(f"Detected Text: {text}")

            # Extract and validate date from the text
            detected_date = extract_date_from_text(text)

            if detected_date:
                # Check if the detected date is expired
                current_date = datetime.now()
                if detected_date < current_date:
                    print("Detected date is expired.")
                    return jsonify({
                        "status": {
                            "code": 200,
                            "message": "Detected date is expired.",
                            "detected_date": detected_date.strftime('%Y-%m-%d')
                        },
                        "data": None,
                    }), 200
                else:
                    print("Detected date is valid.")
                    return BytesIO(cropped_image_bytes), jsonify({
                        "status": {
                            "code": 200,
                            "message": "Detected date is valid.",
                            "detected_date": detected_date.strftime('%Y-%m-%d')
                        },
                        "data": None,
                    })
            else:
                # Detected date is invalid
                return jsonify({
                    "status": {
                        "code": 200,
                        "message": "Detected date is not a valid date.",
                        "detected_date": None
                    },
                    "data": None,
                }), 200

        else:
            # No text detected
            print("No text detected.")
            return jsonify({
                "status": {
                    "code": 200,
                    "message": "No text detected.",
                    "detected_date": None
                },
                "data": None,
            }), 200

    except cv2.error as e:
        print(f"Error decoding image: {e}")
        return jsonify({
            "status": {
                "code": 400,
                "message": "Error decoding image. Please make sure the file is a valid image.",
                "detected_date": None
            },
            "data": None,
        }), 400

    except ValueError as e:
        print(f"Error: {e}")
        return jsonify({
            "status": {
                "code": 400,
                "message": "Empty file. Please upload a non-empty image file.",
                "detected_date": None
            },
            "data": None,
        }), 400

@app.route("/prediction", methods=["GET", "POST"])
def prediction_route():
    if request.method == "POST":
        if 'image' not in request.files:
            return jsonify({
                "status": {
                    "code": 400,
                    "message": "No file part"
                },
                "data": None,
            }), 400

        image = request.files["image"]
        print(f"Type of 'image': {type(image)}")

        if image.filename == '':
            return jsonify({
                "status": {
                    "code": 400,
                    "message": "No selected file"
                },
                "data": None,
            }), 400

        if image and allowed_file(image.filename):
            filename = secure_filename(image.filename)
            image.save(os.path.join(app.config["UPLOAD_FOLDER"], filename))
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
            return jsonify({
                "status": {
                    "code": 400,
                    "message": "Invalid file format. Please upload a JPG, JPEG, or PNG image."
                },
                "data": None,
            }), 400
    else:
        return jsonify({
            "status": {
                "code": 405,
                "message": "Method not allowed"
            },
            "data": None,
        }), 405

# Serve uploaded images
@app.route('/uploads')
def uploaded_file(filename):
    return send_from_directory(app.config['UPLOAD_FOLDER'], filename)

if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=int(os.environ.get("PORT", 8080)))
