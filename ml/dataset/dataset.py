import torch
from torch.utils.data import Dataset
from .data_downloader import load_dataset
from .data_preprocessing import get_image, idx_to_class
from torchvision.transforms import v2

class PlantVillage(Dataset):
    """
    A PyTorch Dataset class for the PlantVillage dataset using Deep Lake.

    This dataset loads plant disease images and their corresponding labels,
    applies optional transforms, and returns the processed image, label index,
    and label name.

    Attributes:
        path_to_dataset (str): Path to the local Deep Lake dataset.
        ds (deeplake.Dataset): Loaded Deep Lake dataset.
        idx_to_class (dict): Mapping from label index to class name.
        transforms (callable, optional): Transformations to apply to images.
    """
    def __init__(self, path_to_dataset, transforms=None):
        """
        Initializes the PlantVillage dataset.

        Args:
            path_to_dataset (str): Path to the saved dataset directory.
            transforms (callable, optional): Optional transforms to apply to the images.
        """
        self.path_to_dataset = path_to_dataset
        self.ds = load_dataset(path_to_dataset)
        self.idx_to_class = idx_to_class(self.ds)
        self.transforms = transforms

    def __len__(self):
        """
        Returns:
            int: Number of samples in the dataset.
        """
        return len(self.ds)

    def __getitem__(self, index):
        """
        Retrieves a sample from the dataset.

        Args:
            index (int): Index of the sample to retrieve.

        Returns:
            tuple: (transformed image tensor, label index tensor, label name string)
        """
        image, label_idx, label_name = get_image(self.ds, index, self.idx_to_class)
        image = torch.from_numpy(image).permute(2, 0, 1)
        label_idx = torch.asarray(label_idx).long()
        if self.transforms:
            image = self.transforms(image)
        return image, label_idx, label_name



if __name__ == "__main__":
    dataset = PlantVillage("./dist/")
    image, label_idx, label_name = dataset[0]
    print(image.shape)
