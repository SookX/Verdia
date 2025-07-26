import torch
import torch.nn as nn
from torchvision.models import vit_b_16

class Classifier(nn.Module):
    def __init__(self, num_classes = 39):
        super().__init__()
        self.model = vit_b_16(weights='IMAGENET1K_V1')
        in_features = self.model.heads.head.in_features
        self.model.heads.head = nn.Linear(in_features, num_classes)
    
    def forward(self, x):
        return self.model(x)

if __name__ == "__main__":
    model = Classifier()
    x = torch.rand(4, 3, 224, 224)
    y = model(x)
    pytorch_total_params = sum(p.numel() for p in model.parameters() if p.requires_grad)
    print(pytorch_total_params)