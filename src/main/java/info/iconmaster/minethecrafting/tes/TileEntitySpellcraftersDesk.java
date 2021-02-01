package info.iconmaster.minethecrafting.tes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import info.iconmaster.minethecrafting.Mana;
import info.iconmaster.minethecrafting.containers.ContainerSpellcraftersDesk;
import info.iconmaster.minethecrafting.items.ItemCard;
import info.iconmaster.minethecrafting.items.MTCItems;
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
import net.minecraft.util.WeightedList;
import net.minecraft.util.text.ITextComponent;

public class TileEntitySpellcraftersDesk extends LockableLootTileEntity implements ITickableTileEntity {
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

    public static final int CARD_SLOT = 0, FIRST_MANA_SLOT = 1, N_MANA_SLOTS = 6, OUTPUT_SLOT = 7, N_SLOTS = 8,
            TICKS_PER_MANA = 20, MANA_CONSUMED = 6, MAX_PROGRESS = TICKS_PER_MANA * (MANA_CONSUMED + 1);

    private NonNullList<ItemStack> slots = NonNullList.<ItemStack>withSize(N_SLOTS, ItemStack.EMPTY);
    private List<Mana> manaConsumed = new ArrayList<>();
    public Data data = new Data();

    public TileEntitySpellcraftersDesk() {
        super(MTCTileEntities.SPELLCRAFTERS_DESK.get());
    }

    @Override
    public void tick() {
        if (slots.get(CARD_SLOT).getCount() > 0 && slots.get(OUTPUT_SLOT).getCount() == 0) {
            if (data.progress >= MAX_PROGRESS) {
                // produce card
                data.progress = 0;
                ItemCard result = getRandomCard(manaConsumed);
                slots.set(OUTPUT_SLOT, new ItemStack(result));
                manaConsumed.clear();
                ItemStack blankCards = slots.get(CARD_SLOT);

                if (blankCards.getCount() == 1) {
                    slots.set(CARD_SLOT, ItemStack.EMPTY);
                } else {
                    blankCards.setCount(blankCards.getCount() - 1);
                }
            } else if (data.progress >= TICKS_PER_MANA * (manaConsumed.size() + 1)) {
                // consume mana
                WeightedList<Integer> possibleSlots = new WeightedList<>();

                for (int i = 0; i < N_MANA_SLOTS; i++) {
                    int slot = FIRST_MANA_SLOT + i;
                    if (slots.get(slot).getCount() > 0) {
                        possibleSlots.func_226313_a_(slot, 1);
                    }
                }

                if (!possibleSlots.func_234005_b_()) {
                    data.progress++;
                    int slot = possibleSlots.func_226318_b_(rng);
                    ItemStack stack = slots.get(slot);
                    manaConsumed.add(Mana.fromItem(stack.getItem()));

                    if (stack.getCount() == 1) {
                        slots.set(slot, ItemStack.EMPTY);
                    } else {
                        stack.setCount(stack.getCount() - 1);
                    }
                }
            } else {
                data.progress++;
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
        return ContainerSpellcraftersDesk.server(windowID, inventory, this);
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

        compound.putInt("progress", data.progress);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);

        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.slots);
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

    private static final Random rng = new Random();

    public static WeightedList<ItemCard> getCardsCraftable(Iterable<Mana> manas) {
        WeightedList<ItemCard> result = new WeightedList<>();
        result.func_226313_a_(MTCItems.CARD_LIGHTNING_BOLT.get(), 1);
        return result;
    }

    public static ItemCard getRandomCard(Iterable<Mana> manas) {
        return getCardsCraftable(manas).func_226318_b_(rng);
    }
}
