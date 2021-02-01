package info.iconmaster.minethecrafting.tes;

import javax.annotation.Nullable;

import info.iconmaster.minethecrafting.containers.ContainerArtificersTable;
import info.iconmaster.minethecrafting.containers.InventoryArtificing;
import info.iconmaster.minethecrafting.registry.ArtificingRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public class TileEntityArtificersTable extends LockableLootTileEntity implements ITickableTileEntity {
    public static class Data implements IIntArray {
        public int progress;

        @Override
        public int get(int arg0) {
            return progress;
        }

        @Override
        public void set(int arg0, int arg1) {
            progress = arg1;
        }

        @Override
        public int size() {
            return 1;
        }
    }

    public static final int FIRST_MANA_SLOT = 0, N_MANA_SLOTS = 5, FIRST_INPUT_SLOT = 5, N_INPUT_SLOTS = 4,
            OUTPUT_SLOT = 9, N_SLOTS = 10, MAX_PROGRESS = 100;

    private NonNullList<ItemStack> slots = NonNullList.<ItemStack>withSize(N_SLOTS, ItemStack.EMPTY);
    private InventoryArtificing craftingInventory = new InventoryArtificing(this);
    public Data data = new Data();

    public TileEntityArtificersTable() {
        super(MTCTileEntities.ARTIFICERS_TABLE.get());
    }

    public ArtificingRecipe getCurrentRecipe() {
        for (ArtificingRecipe recipe : world.getRecipeManager().getRecipes(ArtificingRecipe.TYPE, craftingInventory,
                world)) {
            if (recipe.matches(craftingInventory, world)) {
                return recipe;
            }
        }
        return null;
    }

    @Override
    public void tick() {
        ArtificingRecipe recipe = getCurrentRecipe();
        if (recipe == null) {
            data.progress = 0;
        } else {
            ItemStack potentialOutput = recipe.getRecipeOutput();
            ItemStack currentOutSlotContents = slots.get(OUTPUT_SLOT);
            if (currentOutSlotContents.isEmpty() || (currentOutSlotContents.getItem() == potentialOutput.getItem()
                    && currentOutSlotContents.getCount() + potentialOutput.getCount() <= currentOutSlotContents
                            .getMaxStackSize())) {
                data.progress++;
                if (data.progress >= MAX_PROGRESS) {
                    data.progress = 0;
                    ItemStack result = recipe.getCraftingResult(craftingInventory);

                    if (currentOutSlotContents.isEmpty()) {
                        slots.set(OUTPUT_SLOT, result);
                    } else {
                        currentOutSlotContents.setCount(currentOutSlotContents.getCount() + result.getCount());
                    }
                }
            }
        }
    }

    @Override
    public int getSizeInventory() {
        return N_SLOTS;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return slots;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        for (int i = 0; i < N_SLOTS; i++) {
            slots.set(i, items.get(i));
        }
    }

    @Override
    protected Container createMenu(int windowID, PlayerInventory inventory) {
        return ContainerArtificersTable.server(windowID, inventory, this);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return getBlockState().getBlock().getTranslatedName();
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);

        this.slots = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);

        if (!this.checkLootAndRead(compound)) {
            ItemStackHelper.loadAllItems(compound, this.slots);
        }

        data.progress = compound.getInt("progress");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);

        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.slots);
        }

        compound.putInt("progress", data.progress);

        return compound;
    }

    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tag = getUpdateTag();
        return new SUpdateTileEntityPacket(this.pos, 0, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT tag = pkt.getNbtCompound();
        BlockState state = world.getBlockState(pos);
        handleUpdateTag(state, tag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = new CompoundNBT();
        write(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        read(state, tag);
    }
}
