import torch
import torch.nn as nn
from model.clf import Classifier
from dataset.dataset import PlantVillage
from utils import  load_model
from torchvision.transforms import v2
import os
from torch.utils.data import Subset
import random

checkpoint_path = "./checkpoints/plant-village-v_b16-finetuned/plant-village-v_b16-finetuned_epoch_3.pt"
model = Classifier()
load_model(model, checkpoint_path, infernce_mode=False)


transforms = v2.Compose([
    v2.ToImage(),
    v2.RandomResizedCrop(size=224, scale=(0.8, 1.0)), 
    v2.RandomHorizontalFlip(p=0.5), 
    v2.RandomRotation(degrees=15),
    v2.ColorJitter(brightness=0.2, contrast=0.2, saturation=0.2, hue=0.1),
    v2.ToDtype(torch.float32, scale=True),
    v2.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225]),
])


dataset = PlantVillage("./dataset/dist", transforms)

model.transforms = transforms
model.idx_to_class = dataset.idx_to_class

print(model.state_dict())
subset_size = 150
indices = random.sample(range(len(dataset)), subset_size)
subset = Subset(dataset, indices)

print(model.analyze_metrics(subset))
