package info.iconmaster.minethecrafting.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryArtificing implements IInventory {
    private IInventory inventory;

    public InventoryArtificing(IInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public ItemStack decrStackSize(int arg0, int arg1) {
        return inventory.decrStackSize(arg0, arg1);
    }

    @Override
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int arg0) {
        return inventory.getStackInSlot(arg0);
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity arg0) {
        return inventory.isUsableByPlayer(arg0);
    }

    @Override
    public void markDirty() {
        inventory.markDirty();
    }

    @Override
    public ItemStack removeStackFromSlot(int arg0) {
        return inventory.removeStackFromSlot(arg0);
    }

    @Override
    public void setInventorySlotContents(int arg0, ItemStack arg1) {
        inventory.setInventorySlotContents(arg0, arg1);
    }

}
