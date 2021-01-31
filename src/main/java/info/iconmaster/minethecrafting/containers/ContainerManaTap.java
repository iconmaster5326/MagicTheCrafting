package info.iconmaster.minethecrafting.containers;

import java.util.ArrayList;
import java.util.List;

import info.iconmaster.minethecrafting.Mana;
import info.iconmaster.minethecrafting.ManaTapRegistry;
import info.iconmaster.minethecrafting.screens.ScreenManaTap;
import info.iconmaster.minethecrafting.tes.TileEntityManaTap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class ContainerManaTap extends Container {
    public IInventory inventory;
    public TileEntityManaTap.Data teData;
    public List<Mana> manaGeneratable;

    private ContainerManaTap(int windowID, PlayerInventory playerInventory, TileEntityManaTap te,
            PacketBuffer packetBuffer) {
        super(MTCContainers.MANA_TAP.get(), windowID);

        if (te == null) {
            // client side
            inventory = new Inventory(TileEntityManaTap.N_SLOTS);
            teData = new TileEntityManaTap.Data();
            // TODO: actually pass packets around
            manaGeneratable = ManaTapRegistry.getMana(playerInventory.player.world,
                    playerInventory.player.getPosition());
        } else {
            // server side
            inventory = te;
            teData = te.data;
            manaGeneratable = te.manaGeneratable();
        }

        inventory.openInventory(playerInventory.player);

        for (int n = 0; n < TileEntityManaTap.N_SLOTS; n++) {
            addSlot(new SlotOutputOnly(inventory, n, ScreenManaTap.OUTPUTS_X + n * 18, ScreenManaTap.OUTPUTS_Y));
        }

        int leftCol = (ScreenManaTap.WIDTH - 162) / 2 + 1;

        for (int playerInvRow = 0; playerInvRow < 3; playerInvRow++) {
            for (int playerInvCol = 0; playerInvCol < 9; playerInvCol++) {
                addSlot(new Slot(playerInventory, playerInvCol + playerInvRow * 9 + 9, leftCol + playerInvCol * 18,
                        ScreenManaTap.HEIGHT - (4 - playerInvRow) * 18 - 10));
            }

        }

        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
            addSlot(new Slot(playerInventory, hotbarSlot, leftCol + hotbarSlot * 18, ScreenManaTap.HEIGHT - 24));
        }

        trackIntArray(teData);
    }

    public static ContainerManaTap client(int windowID, PlayerInventory inventory, PacketBuffer packetBuffer) {
        return new ContainerManaTap(windowID, inventory, null, packetBuffer);
    }

    public static ContainerManaTap server(int windowID, PlayerInventory inventory, TileEntityManaTap te) {
        return new ContainerManaTap(windowID, inventory, te, null);
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < TileEntityManaTap.N_SLOTS) {
                if (!this.mergeItemStack(itemstack1, TileEntityManaTap.N_SLOTS, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, TileEntityManaTap.N_SLOTS, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.inventory.closeInventory(playerIn);
    }
}
