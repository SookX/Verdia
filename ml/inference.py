import torch
import torch.nn as nn
from model.clf import Classifier
from dataset.dataset import PlantVillage
from torchvision.transforms import v2
import json
from utils import load_model

transforms = v2.Compose([
    v2.RandomResizedCrop(size=224, scale=(0.8, 1.0)), 
     v2.ToDtype(torch.float32, scale=True),
     v2.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225]),
])


clf = Classifier()
load_model(clf, "./checkpoints/plant-village-effb1-finetuned/plant-village-effb1-finetuned_epoch_1.pt", infernce_mode=False)
dataset = PlantVillage("./dataset/dist")
idx_to_class = dataset.idx_to_class

save_path = "./saved_models/idx_to_class.json"
with open(save_path, "w") as f:
    json.dump(idx_to_class, f)

print(f"Saved idx_to_class to {save_path}")

image, label_idx, label_name = dataset[1500]
print(image.shape)
print(label_idx)
print(clf.forward_inference(image.unsqueeze(0), transforms))