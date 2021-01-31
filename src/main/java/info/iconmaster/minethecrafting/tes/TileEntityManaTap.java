package info.iconmaster.minethecrafting.tes;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import info.iconmaster.minethecrafting.Mana;
import info.iconmaster.minethecrafting.ManaTapRegistry;
import info.iconmaster.minethecrafting.containers.ContainerManaTap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public class TileEntityManaTap extends LockableLootTileEntity implements ITickableTileEntity {
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

    public static final int N_SLOTS = 3, MAX_PROGRESS = 60;

    private NonNullList<ItemStack> outputs = NonNullList.<ItemStack>withSize(N_SLOTS, ItemStack.EMPTY);
    private Random rng = new Random();
    public Data data = new Data();

    public TileEntityManaTap() {
        super(MTCTileEntities.MANA_TAP.get());
    }

    @Override
    public void tick() {
        if (data.progress < MAX_PROGRESS) {
            data.progress++;
        } else {
            List<Mana> mana = manaGeneratable();
            if (!mana.isEmpty()) {
                Item toOutput = mana.get(rng.nextInt(mana.size())).item();
                for (int slot = 0; slot < N_SLOTS; slot++) {
                    ItemStack stack = outputs.get(slot);
                    if (stack == ItemStack.EMPTY || stack.getCount() <= 0) {
                        data.progress -= MAX_PROGRESS;
                        outputs.set(slot, new ItemStack(toOutput));
                        break;
                    } else if (stack.getItem() == toOutput && stack.getCount() < stack.getMaxStackSize()) {
                        data.progress -= MAX_PROGRESS;
                        stack.setCount(stack.getCount() + 1);
                        break;
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
        return outputs;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        outputs.set(0, items.get(0));
        outputs.set(1, items.get(1));
        outputs.set(2, items.get(2));
    }

    @Override
    protected Container createMenu(int windowID, PlayerInventory inventory) {
        return ContainerManaTap.server(windowID, inventory, this);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return getBlockState().getBlock().getTranslatedName();
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);

        this.outputs = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);

        if (!this.checkLootAndRead(compound)) {
            ItemStackHelper.loadAllItems(compound, this.outputs);
        }

        compound.putInt("progress", data.progress);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);

        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.outputs);
        }

        data.progress = compound.getInt("progress");

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

    public List<Mana> manaGeneratable() {
        return ManaTapRegistry.getMana(world, pos);
    }
}
