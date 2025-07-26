import os
import random
import matplotlib.pyplot as plt
from .data_downloader import load_dataset

def idx_to_class(dataset):
    """
    Creates a mapping from class indices to class names.

    Parameters:
        dataset (deeplake.Dataset): The loaded Deep Lake dataset.

    Returns:
        dict: A dictionary mapping class indices (int) to class names (str).
    """
    class_names = dataset.labels.info['class_names']
    classes = {}
    for idx, name in enumerate(class_names):
        classes[idx] = name
    return classes

def get_image(dataset, idx, idx_to_class):    
    """
    Retrieves an image and its label from the dataset.

    Parameters:
        dataset (deeplake.Dataset): The Deep Lake dataset.
        idx (int): Index of the sample to retrieve.
        idx_to_class (dict): Dictionary mapping label indices to class names.

    Returns:
        tuple: (image as NumPy array, label index, label name as string)
    """
    sample = dataset[idx]
    image = sample["images"].numpy()
    label_idx = sample["labels"].numpy()[0]
    label_name = idx_to_class[label_idx]
    return image, label_idx, label_name

def plot_random_sample(dataset, idx_to_class):
    """
    Plots a random image from the dataset with its corresponding label.

    Parameters:
        dataset (deeplake.Dataset): The dataset to sample from.
        idx_to_class (dict): Mapping from label indices to class names.
    """
    def _get_random_sample(dataset, idx_to_class):
        random_idx = random.randint(0, len(dataset) - 1)
        return get_image(dataset, random_idx, idx_to_class)

    image, _, label_name = _get_random_sample(dataset, idx_to_class)
    plt.imshow(image)
    plt.title(label_name)
    plt.axis('off')
    plt.show()

if __name__ == "__main__":

    saved_path = "./dist/"
    ds = load_dataset(saved_path)
    classes = idx_to_class(ds)
    print(classes)
    plot_random_sample(ds, classes)
