import torch
from tqdm import tqdm
from sklearn.metrics import accuracy_score

def valid_step(model, loss_fn, val_dataloader, device):
    """
    Performs a validation step over the entire validation set.

    Args:
        model (nn.Module): The trained model.
        loss_fn (callable): Loss function.
        val_dataloader (DataLoader): Validation DataLoader.
        device (torch.device): Device to run validation on.

    Returns:
        tuple:
            - float: Average validation loss.
            - list: Per-batch validation losses.
            - float: Overall validation accuracy.
    """
    model.eval()
    total_loss = 0.0
    val_losses = []
    all_preds = []
    all_labels = []

    with torch.no_grad():
        for image, label_idx, _ in tqdm(val_dataloader, desc="Validating"):
            image = image.to(device)
            label_idx = label_idx.to(device)

            with torch.amp.autocast(device_type=device):
                preds = model(image)
                loss = loss_fn(preds, label_idx)

            val_losses.append(loss.item())
            total_loss += loss.item()

            pred_labels = torch.argmax(preds, dim=1)
            all_preds.extend(pred_labels.cpu().numpy())
            all_labels.extend(label_idx.cpu().numpy())

    avg_loss = total_loss / len(val_dataloader)
    accuracy = accuracy_score(all_labels, all_preds)

    return avg_loss, val_losses, accuracy
