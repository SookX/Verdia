import torch
from tqdm import tqdm

def valid_step(model, loss_fn, val_dataloader, device):
    model.eval()
    total_loss = 0.0
    val_losses = []

    for image, label_idx, _ in tqdm(val_dataloader):
        image = image.to(device)
        label_idx = label_idx.to(device)
            
        with torch.amp.autocast(device):
            preds = model(image)
            loss = loss_fn(preds, label_idx)
            val_losses.append(loss.item())

        total_loss += loss.item()
    
    return total_loss / len(val_dataloader), val_losses