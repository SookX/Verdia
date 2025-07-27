from fastapi import FastAPI
from pydantic import BaseModel
import requests
from PIL import Image
import numpy as np
import base64
from io import BytesIO
import torch
import matplotlib.pyplot as plt
import matplotlib.cm as cm
import os
from dotenv import load_dotenv
from torchvision.transforms import v2
from utils import load_model
from model.clf import Classifier
import json


def llm(plant: str, disease: str, confidence: float):
    API_URL = "https://router.huggingface.co/novita/v3/openai/chat/completions"
    headers = {
        "Authorization": f"Bearer {os.getenv('HUGGING_FACE')}",
    }

    conf_percent = confidence * 100 if confidence <= 1 else confidence

    if disease.lower() == "healthy" or disease.lower() == "none":
        prompt = (
            f"The plant is {plant} and appears healthy with a confidence of {conf_percent:.1f}%.\n"
            "Please provide a detailed analysis on how to protect this plant from common diseases, "
            "including preventive measures, environmental tips, and general best practices."
        )
    else:
        prompt = (
            f"The plant is {plant} and is diagnosed with {disease} disease with a confidence of {conf_percent:.1f}%. \n"
            "Please provide a detailed analysis including:\n"
            "1. Description of the disease.\n"
            "2. Recommended steps to treat and remove the disease.\n"
            "3. Tips on how to prevent recurrence.\n"
            "4. Any additional care advice."
        )

    def query(payload):
        response = requests.post(API_URL, headers=headers, json=payload)
        response.raise_for_status()
        return response.json()

    response = query({
        "messages": [
            {
                "role": "system",
                "content": (
                    "You are a professional plant pathologist and agronomist. Provide clear, structured, "
                    "and detailed advice for plant disease management and plant health maintenance."
                )
            },
            {
                "role": "user",
                "content": prompt
            }
        ],
        "model": "meta-llama/llama-3.3-70b-instruct"
    })

    generated_text = response.get("choices", [{}])[0].get("message", {}).get("content", "")
    return generated_text


transforms = v2.Compose([
    v2.RandomResizedCrop(size=224, scale=(0.8, 1.0)), 
     v2.ToDtype(torch.float32, scale=True),
     v2.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225]),
])

clf = Classifier()
load_model(clf, "./checkpoints/plant-village-effb1-finetuned/plant-village-effb1-finetuned_epoch_1.pt", infernce_mode=False)

load_path = "./saved_models/idx_to_class.json"
with open(load_path, "r") as f:
    idx_to_class = json.load(f)

print(f"Loaded idx_to_class: {idx_to_class}")

class AnalysisModel(BaseModel):
    url: str



app = FastAPI()
@app.post("/api/service/analysis")
def analysis(item: AnalysisModel):
    def _get_confidence(probs):
        if probs > 0.9:
            return "High"
        elif probs > 0.5:
            return "Medium"
        else:
            return "Low"


        
    response = requests.get(item.url)
    image = Image.open(BytesIO(response.content)).convert("RGB")
    image_resized = image.resize((256, 256), Image.BILINEAR)
    image_np = np.array(image_resized, dtype=np.float32)
    image_tensor = torch.tensor(image_np).permute(2, 0, 1).unsqueeze(0)
    pred_probs, preds, _ = clf.forward_inference(image_tensor)
    pred_probs = pred_probs.item()
    preds = preds.item()
    pred_class = idx_to_class[preds]
    name, disease = pred_class.split("_", 1)
    
    response  = {
        "plant": name,
        "disease": disease,
        "confidence": _get_confidence(pred_probs),
        "analysis": llm(name, disease, pred_probs)
    }

    return response