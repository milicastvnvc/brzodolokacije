import base64
from fastapi import FastAPI, File, Request, UploadFile
from typing import List
import io
import cv2
from imageio import imread
import matplotlib.pyplot as plt

app = FastAPI()

def detect_face(img):
    filter_path = r'haarcascade_frontalface_default.xml'
    face_cascade = cv2.CascadeClassifier(filter_path)
    face_rects = face_cascade.detectMultiScale(img,scaleFactor=1.2, minNeighbors=10) 
    if(len(face_rects)==0): 
        return False #nema lica
    return True #ima lica

def detect_face_for_profile_picture(img):
    filter_path = r'haarcascade_frontalface_default.xml'
    face_cascade = cv2.CascadeClassifier(filter_path)
    face_rects = face_cascade.detectMultiScale(img,scaleFactor=1.2, minNeighbors=10) 
    if(len(face_rects)==1): 
        return True #jedno lice - moze da se obajvi(samo ukoliko ima je slikana slika frontalno)
    return False #ima vise od jednog lica


def marker_face(img):
    filter_path = r'haarcascade_frontalface_default.xml'
    face_cascade = cv2.CascadeClassifier(filter_path)
    face_img = img.copy()
    face_rects = face_cascade.detectMultiScale(face_img,scaleFactor=1.2, minNeighbors=10)
    for (x,y,w,h) in face_rects:
        cv2.rectangle(face_img, (x,y), (x+w,y+h), (0,0,255), 5)
    return face_img

@app.post("/has-face-on-image/")
async def create_upload_file(files: List[UploadFile]):
    responseList = []
    for i, img in enumerate(files):
        image = imread(io.BytesIO(await img.read()))
        cv2_img = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)
        responseList.append({'picture': img.filename, 'hasFace': detect_face(cv2_img)})
        
    return responseList

@app.post("/has-one-face-on-image/")
async def has_one_face_on_image(request: Request):
    body = await request.json()
    img = body['base64']
    image = imread(io.BytesIO(base64.b64decode(img)))
    return detect_face_for_profile_picture(image)