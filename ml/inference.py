import torch
import torch.nn as nn
from model.clf import Classifier
from utils import load_config, load_model
from dataset.dataset import PlantVillage
from torch.utils.data import Subset
import random
from torchvision.transforms import v2

transforms = v2.Compose([
    v2.ToImage(),
    v2.RandomResizedCrop(size=224, scale=(0.8, 1.0)), 
    v2.RandomHorizontalFlip(p=0.5), 
    v2.RandomRotation(degrees=15),
    v2.ColorJitter(brightness=0.2, contrast=0.2, saturation=0.2, hue=0.1),
    v2.ToDtype(torch.float32, scale=True),
    v2.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225]),
])



config = load_config()

model = Classifier()
model.transforms = transforms

load_model(model, "./saved_models/plant-village-v_b16-final.pt")

print(model.state_dict())
dataset = PlantVillage("./dataset/dist", model.transforms)
model.idx_to_class = dataset.idx_to_class

subset_size = 50
indices = random.sample(range(len(dataset)), subset_size)
subset = Subset(dataset, indices)

if __name__ == "__main__":
    image, image_idx, image_class = dataset[700]
    y = model.forward_inference(image)
    print(model.analyze_metrics(subset))
