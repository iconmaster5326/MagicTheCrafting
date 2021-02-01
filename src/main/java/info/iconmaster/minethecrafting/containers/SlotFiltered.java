package info.iconmaster.minethecrafting.containers;

import java.util.function.Predicate;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SlotFiltered extends Slot {
    private Predicate<ItemStack> allowed;

    public SlotFiltered(IInventory inventory, int slot, int x, int y, Predicate<ItemStack> allowed) {
        super(inventory, slot, x, y);
        this.allowed = allowed;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return allowed.test(stack);
    }
}
