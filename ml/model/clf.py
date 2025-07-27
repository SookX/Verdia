import torch
import torch.nn as nn
from torchvision.models import vit_b_16
from sklearn.metrics import accuracy_score, precision_score, recall_score, f1_score

class Classifier(nn.Module):
    """
    Vision Transformer (ViT) based image classification model.

    This model uses a pre-trained ViT-B/16 backbone from torchvision and replaces
    the final classification head to match the desired number of output classes.

    Args:
        num_classes (int): Number of output classes for classification. Default is 39.
    """

    def __init__(self, idx_to_class = None, transforms = None, num_classes = 39):
        super().__init__()
        self.model = vit_b_16(weights='IMAGENET1K_V1')
        in_features = self.model.heads.head.in_features
        self.model.heads.head = nn.Linear(in_features, num_classes)
        self.idx_to_class = idx_to_class # For inference
        self.transforms = transforms # For inference
    
    def forward(self, x):
        return self.model(x)
    
    def forward_inference(self, image):
        self.eval()
        if self.transforms:
            image = self.transforms(image)
        if image.dim() == 3:
            image = image.unsqueeze(0)

        with torch.inference_mode():
            y = self.forward(image)
            probs = torch.softmax(y, dim=1)
            preds = torch.argmax(probs, dim =1)
            pred_probs = probs[torch.arange(len(preds)), preds]
        
        pred_class = None
        if self.idx_to_class:
            pred_class = [self.idx_to_class[idx.item()] for idx in preds]

        return pred_probs, preds, pred_class

    def analyze_metrics(self, dataset):
        self.eval()
        y_true = []
        y_pred = []
        with torch.inference_mode():
            for image, label, _ in dataset:
                _, preds, _ = self.forward_inference(image)

            y_true.append(label if isinstance(label, int) else label.item())
            y_pred.append(preds.item())
         
        return {
            'accuracy': accuracy_score(y_true, y_pred),
            'precision': precision_score(y_true, y_pred, average='macro', zero_division=0),
            'recall': recall_score(y_true, y_pred, average='macro', zero_division=0),
            'f1': f1_score(y_true, y_pred, average='macro', zero_division=0)
        }

            


if __name__ == "__main__":
    model = Classifier()
    x = torch.rand(3, 224, 224)
    y = model(x)
    y = model.forward_inference(x)
    pytorch_total_params = sum(p.numel() for p in model.parameters() if p.requires_grad)
    print(pytorch_total_params)