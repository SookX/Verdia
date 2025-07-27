import deeplake
import os

def download_dataset(path_to_save):
    """
    Downloads the PlantVillage dataset from Activeloop Hub and saves it locally.

    Parameters:
        path_to_save (str): Local directory where the dataset should be saved.
    """
    deeplake.deepcopy("hub://activeloop/plantvillage-without-augmentation", path_to_save)

def load_dataset(path_to_save):
    """
    Loads a locally saved Deep Lake dataset.

    Parameters:
        path_to_save (str): Path to the local dataset directory.

    Returns:
        deeplake.Dataset: The loaded Deep Lake dataset object.
    """
    return deeplake.load(path_to_save)


if __name__ == '__main__':
    path_to_save = "./dist/"
    download_dataset(path_to_save)


