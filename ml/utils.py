import torch
import yaml
from torch.utils.data import random_split

def get_device() -> str:
    """
    Determines the best available device for PyTorch operations.

    Returns:
        str: One of "cuda", "mps", or "cpu", depending on hardware availability.
    """
    if torch.cuda.is_available():
        return "cuda"
    if torch.backends.mps.is_available():
        return "mps"
    
    return "cpu"

def load_config(config_path="config.yaml"):
    """
    Loads a YAML configuration file.

    Args:
        config_path (str): Path to the YAML configuration file (default is "config.yaml").

    Returns:
        dict: Parsed configuration parameters as a dictionary.
    """
    with open(config_path, "r") as f:
        config = yaml.safe_load(f)
    return config

def load_model(model, model_name, infernce_mode = True, device='cpu'):
    checkpoint = torch.load(model_name, map_location=device, weights_only= False)
    model.load_state_dict(checkpoint['model_state_dict'])

    if infernce_mode == True:
        model.transforms = checkpoint['transforms']
        model.idx_to_class = checkpoint['idx_to_class']
    model.to(device)
    model.eval() 

def train_val_split(dataset, val_size=0.2):
    """
    Splits a dataset into training and validation subsets.

    Args:
        dataset (torch.utils.data.Dataset): The full dataset to split.
        val_size (float): Proportion of the dataset to use for validation (default is 0.2).

    Returns:
        tuple: (train_dataset, val_dataset) after random split.
    """
    dataset_size = len(dataset)
    val_size = int(val_size * dataset_size)
    train_size = dataset_size - val_size
    train_dataset, val_dataset = random_split(dataset, [train_size, val_size])
    return train_dataset, val_dataset
