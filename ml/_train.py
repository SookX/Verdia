import torch
import logging
from tqdm import tqdm
import time
import os
import json
from _val import valid_step
from sklearn.metrics import accuracy_score


from sklearn.metrics import accuracy_score

def train_step(
    model, epochs, optimizer, loss_fn, scheduler,
    train_dataloader, val_dataloader, device,
    model_name, save_checkpoint_every, checkpoint_dir,
    start_epoch=0
):
    """
    Trains a PyTorch model and evaluates it on a validation set after each epoch.

    Args:
        model (torch.nn.Module): The model to be trained.
        epochs (int): Total number of training epochs.
        optimizer (torch.optim.Optimizer): Optimizer for model training.
        loss_fn (callable): Loss function.
        scheduler (torch.optim.lr_scheduler._LRScheduler): Learning rate scheduler.
        train_dataloader (DataLoader): DataLoader for the training set.
        val_dataloader (DataLoader): DataLoader for the validation set.
        device (torch.device): Device to use (e.g., 'cuda' or 'cpu').
        model_name (str): Name of the model (used for checkpoint naming).
        save_checkpoint_every (int): Save model checkpoint every N epochs.
        checkpoint_dir (str): Directory where checkpoints and logs will be saved.
        start_epoch (int, optional): Epoch to start training from (default is 0, used for resuming training).

    Saves:
        - Model checkpoints every `save_checkpoint_every` epochs.
        - JSON logs of batch-level training and validation losses.
        - JSON logs of training and validation accuracies per epoch.
    """
    model.train()
    torch.autograd.set_detect_anomaly(True)
    logging.info("Initializing model training...\n")

    all_train_batch_losses = []
    all_val_batch_losses = []
    train_accuracies = []
    val_accuracies = []

    for epoch in range(start_epoch, epochs):
        start_time = time.time()
        total_loss = 0.0
        batch_losses = []
        all_preds = []
        all_labels = []

        for image, label_idx, _ in tqdm(train_dataloader, desc=f"Epoch {epoch+1}/{epochs} - Training"):
            image = image.to(device)
            label_idx = label_idx.to(device)
            
            with torch.amp.autocast(device_type=device):
                preds = model(image)
                loss = loss_fn(preds, label_idx)

            optimizer.zero_grad()
            loss.backward()
            optimizer.step()

            batch_losses.append(loss.item())
            total_loss += loss.item()

            pred_labels = torch.argmax(preds, dim=1)
            all_preds.extend(pred_labels.cpu().numpy())
            all_labels.extend(label_idx.cpu().numpy())

        epoch_accuracy = accuracy_score(all_labels, all_preds)
        train_accuracies.append(epoch_accuracy)

        scheduler.step()
        elapsed = time.time() - start_time
        avg_train_loss = total_loss / len(train_dataloader)

        avg_val_loss, val_losses, val_accuracy = valid_step(model, loss_fn, val_dataloader, device)
        val_accuracies.append(val_accuracy)

        all_train_batch_losses.append(batch_losses)
        all_val_batch_losses.append(val_losses)

        print(
            f"Epoch {epoch+1}/{epochs} - "
            f"Train Loss: {avg_train_loss:.4f} - "
            f"Train Acc: {epoch_accuracy:.4f} - "
            f"{'Val Loss: {:.4f} - '.format(avg_val_loss) if avg_val_loss is not None else ''}"
            f"{'Val Acc: {:.4f} - '.format(val_accuracy) if val_accuracy is not None else ''}"
            f"Time: {elapsed:.2f}s"
        )

        if (epoch + 1) % save_checkpoint_every == 0:
            path = os.path.join(checkpoint_dir, model_name)
            os.makedirs(path, exist_ok=True)
            checkpoint_path = os.path.join(path, f"{model_name}_epoch_{epoch+1}.pt")
            torch.save({
                'epoch': epoch + 1,
                'model_state_dict': model.state_dict(),
                'optimizer_state_dict': optimizer.state_dict(),
                'scheduler_state_dict': scheduler.state_dict(),
                'train_loss': avg_train_loss,
                'val_loss': avg_val_loss,
                'train_acc': epoch_accuracy,
                'val_acc': val_accuracy,
            }, checkpoint_path)
            logging.info(f"Checkpoint saved at {checkpoint_path}")

    output_dir = os.path.join(checkpoint_dir, model_name)
    with open(os.path.join(output_dir, f"{model_name}_batch_train_losses.json"), "w") as f:
        json.dump(all_train_batch_losses, f)
        
    with open(os.path.join(output_dir, f"{model_name}_batch_val_losses.json"), "w") as f:
        json.dump(all_val_batch_losses, f)
    with open(os.path.join(output_dir, f"{model_name}_train_accuracies.json"), "w") as f:
        json.dump(train_accuracies, f)
    with open(os.path.join(output_dir, f"{model_name}_val_accuracies.json"), "w") as f:
        json.dump(val_accuracies, f)
