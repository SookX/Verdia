import torch
import logging
from tqdm import tqdm
import time
import os
import json
from _val import valid_step


def train_step(model, epochs, optimizer, loss_fn, scheduler, train_dataloader, val_dataloader, device, model_name, save_checkpoint_every, checkpoint_dir, start_epoch = 0):
    model.train()
    torch.autograd.set_detect_anomaly(True)
    logging.info("Initializing model training...\n")

    all_train_batch_losses = []
    all_val_batch_losses = []

    for epoch in range(start_epoch, epochs):
        start_time = time.time()
        total_loss = 0.0
        batch_losses = []

        for image, label_idx, _ in tqdm(train_dataloader, desc=f"Epoch {epoch+1}/{epochs} - Training"):
            image = image.to(device)
            label_idx = label_idx.to(device)
            
            with torch.amp.autocast(device):
                preds = model(image)
                loss = loss_fn(preds, label_idx)

                batch_losses.append(loss.item())

            optimizer.zero_grad()
            loss.backward()
            optimizer.step()
            total_loss += loss.item()

        scheduler.step()
        elapsed = time.time() - start_time
        avg_train_loss  = total_loss / len(train_dataloader)
        avg_val_loss, val_losses = valid_step(model, loss_fn, val_dataloader, device)
        all_train_batch_losses.append(batch_losses)
        all_val_batch_losses.append(val_losses)
        print(
            f"Epoch {epoch+1}/{epochs} - "
            f"Train Loss: {avg_train_loss:.4f} - "
            f"{'Val Loss: {:.4f} - '.format(avg_val_loss) if avg_val_loss is not None else ''}"
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
            }, checkpoint_path)
            logging.info(f"Checkpoint saved at {checkpoint_path}")

    with open(os.path.join(checkpoint_dir, model_name, f"{model_name}_batch_train_losses.json"), "w") as f:
        json.dump(all_train_batch_losses, f)

    with open(os.path.join(checkpoint_dir, model_name, f"{model_name}_batch_val_losses.json"), "w") as f:
        json.dump(all_val_batch_losses, f)
