import torch
import yaml
from torch.utils.data import random_split

def get_device() -> str:
    if torch.cuda.is_available():
        return "cuda"
    if torch.backends.mps.is_available():
        return "mps"
    
    return "cpu"

def load_config(config_path="config.yaml"):
    with open(config_path, "r") as f:
        config = yaml.safe_load(f)
    return config

def train_val_split(dataset, val_size = 0.2):
    dataset_size = len(dataset)
    val_size = int(val_size * dataset_size)
    train_size = dataset_size - val_size

    train_dataset, val_dataset = random_split(dataset,
                                            [train_size, val_size])
    return train_dataset, val_dataset
    