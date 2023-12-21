# CH2-PS166 BOGAREKSA

# Exp Date Detection Model
Welcome to the Exp Date Detection repository! This project aims to detect expiration dates on images using the Object Detection VGG16 model. We have also integrated the Cloud Vision service to perform Optical Character Recognition (OCR) on the bounding boxes generated by the VGG16 model.

# Table of Contents
* [Info](#info)
* [Python Libraries](#python-libraries)
* [Contact](#contact)
* [Documentation](#documentation)
* [Source](#source)


# Info
Object Detection using VGG16:
We have trained an Object Detection model using the VGG16 architecture. This model can detect and draw bounding boxes around areas where expiration dates may be located.
OCR with Cloud Vision:
After obtaining the bounding boxes from the VGG16 model, we use the Cloud Vision service to perform OCR on the text within them. This helps us extract text, including expiration dates, from the images.

# Python Libraries
* [OpenCV](https://opencv.org/releases/)
* [NumPy](https://numpy.org/)
* [Pandas](https://pandas.pydata.org/)
* [TensorFlow](https://www.tensorflow.org/)
* [Keras](https://keras.io/)
* [Scikit Learn](https://scikit-learn.org/stable/)

# Contact
For any inquiries or feedback, feel free to reach out to us :
- leonvirmus17@gmail.com (Leonardus Virmus Danar)
- putuartoya@students.amikom.ac.id (I Putu Artoya)
- tegarfadillah1@gmail.com (Tegar Fadillah Zanestri)

# Documentation

## Reading Data:
The code begins by importing necessary libraries, including TensorFlow, Keras, OpenCV, and others.
The dataset is loaded from the specified path, which includes images and corresponding annotations in JSON format.
Images are preprocessed by resizing them to (224, 224) and converting them to NumPy arrays.
Bounding box coordinates are normalized to values between 0 and 1.

## Training:
VGG16, a pre-trained model on ImageNet, is loaded without its top layers.
The VGG16 layers are frozen to prevent their weights from being updated during training.
Additional layers are added to the model, including flattening, fully-connected layers with ReLU activation, and a final output layer with a sigmoid activation function (for bounding box coordinates).
The model is compiled using the mean squared error loss function and the Adam optimizer.
The model is trained using the provided training images, targets, and validation data.

## Testing:
The trained model is loaded.
Images specified in the testing file (or a single image if provided directly) are loaded and preprocessed.
Bounding box predictions are made using the trained model.
Predicted coordinates are scaled based on the original image dimensions.
The original image and the region defined by the predicted bounding box are displayed.

## Data Information:
An additional section shows how to visualize the annotated bounding boxes on images using OpenCV and Matplotlib.
A function (show_image_with_bounding_box) is defined to display images with bounding boxes based on annotations from the JSON file.

## Visualization:
Images from the dataset are visualized along with their annotated bounding boxes to provide an understanding of the dataset.

## Source:
* [Model](https://drive.google.com/drive/folders/1rOrBYEq_pdEl_xHz1DotmyOmGEx7rNBH?usp=drive_link) 
* [Dataset](https://drive.google.com/drive/folders/1nD2TZwt2oXS8G_bNXSx3XVPvOUZIkvtc?usp=drive_link)
