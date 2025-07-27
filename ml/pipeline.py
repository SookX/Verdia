import torch
import torch.nn as nn

from torch.nn import CrossEntropyLoss
from torch.optim import Adam
from torch.optim.lr_scheduler import StepLR
from torchvision.transforms import v2
import random

from torch.utils.data import DataLoader, Subset


from model.clf import Classifier
from dataset.dataset import PlantVillage

from utils import get_device, load_config
from utils import train_val_split
from _train import train_step

DEVICE = get_device()
print(f"[INFO] Training will run on: {DEVICE.upper()}\n")

config = load_config()

MODEL_NAME = str(config["model"]["model_name"])

EPOCHS = int(config["training"]["epochs"])
BATCH_SIZE = int(config["training"]["batch_size"])
LEARNING_RATE = float(config["training"]["learning_rate"])

SAVE_CHECKPOINT_EVERY = int(config["logging"]["save_checkpoint_every"])
CHECKPOINT_DIR = str(config["logging"]["checkpoint_dir"])


# Define data augmentation and preprocessing pipeline
transforms = v2.Compose([
#     v2.ToImage(),
    v2.RandomResizedCrop(size=224, scale=(0.8, 1.0)), 
#     v2.RandomHorizontalFlip(p=0.5), 
#     v2.RandomRotation(degrees=15),
#     v2.ColorJitter(brightness=0.2, contrast=0.2, saturation=0.2, hue=0.1),
     v2.ToDtype(torch.float32, scale=True),
     v2.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225]),
])


dataset = PlantVillage("./dataset/dist", transforms=transforms)

train_dataset, val_dataset = train_val_split(dataset)

train_dataloader = DataLoader(train_dataset, BATCH_SIZE, True, num_workers=0)
val_dataloader = DataLoader(val_dataset, BATCH_SIZE, False, num_workers=0)
print("[INFO] All data loaders have been successfully initialized.")

model = Classifier(len(dataset.idx_to_class), transforms).to(DEVICE)
loss_fn = CrossEntropyLoss()
optimizer = Adam(model.parameters(), LEARNING_RATE)
scheduler = StepLR(optimizer, step_size=5, gamma=0.5)


train_step(model, EPOCHS, optimizer, loss_fn, scheduler, train_dataloader, val_dataloader, DEVICE, MODEL_NAME, SAVE_CHECKPOINT_EVERY, CHECKPOINT_DIR)