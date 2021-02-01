package info.iconmaster.minethecrafting.containers;

import info.iconmaster.minethecrafting.MineTheCrafting;
import info.iconmaster.minethecrafting.items.MTCItems;
import info.iconmaster.minethecrafting.tes.TileEntitySpellcraftersDesk;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class ContainerSpellcraftersDesk extends MTCContainer {
    PlayerInventory playerInventory;
    public IInventory inventory;
    public TileEntitySpellcraftersDesk.Data teData;

    public static final int GUI_WIDTH = 176, GUI_HEIGHT = 166, INPUT_X = 19, INPUT_Y = 35, MANAS_X = 62, MANAS_Y = 26,
            OUTPUT_X = 140, OUTPUT_Y = 35;

    private ContainerSpellcraftersDesk(int windowID, PlayerInventory playerInventory, TileEntitySpellcraftersDesk te,
            PacketBuffer packet) {
        super(MTCContainers.SPELLCRAFTERS_DESK.get(), windowID);
        this.playerInventory = playerInventory;

        if (te == null) {
            // client side
            inventory = new Inventory(TileEntitySpellcraftersDesk.N_SLOTS);
            teData = new TileEntitySpellcraftersDesk.Data();
        } else {
            // server side
            inventory = te;
            teData = te.data;
        }

        inventory.openInventory(playerInventory.player);

        addSlot(new SlotFiltered(inventory, TileEntitySpellcraftersDesk.CARD_SLOT, INPUT_X, INPUT_Y,
                stack -> stack.getItem() == MTCItems.BLANK_CARD.get()));

        for (int i = 0; i < TileEntitySpellcraftersDesk.N_MANA_SLOTS; i++) {
            int row = i % 3, col = i / 3;
            addSlot(new SlotFiltered(inventory, TileEntitySpellcraftersDesk.FIRST_MANA_SLOT + i, MANAS_X + 18 * row,
                    MANAS_Y + 18 * col, stack -> ItemTags.getCollection()
                            .get(new ResourceLocation(MineTheCrafting.MOD_ID, "mana")).contains(stack.getItem())));
        }

        addSlot(new SlotOutputOnly(inventory, TileEntitySpellcraftersDesk.OUTPUT_SLOT, OUTPUT_X, OUTPUT_Y));

        addPlayerSlots(playerInventory, GUI_WIDTH, GUI_HEIGHT);
        trackIntArray(teData);
    }

    public static ContainerSpellcraftersDesk client(int windowID, PlayerInventory inventory,
            PacketBuffer packetBuffer) {
        return new ContainerSpellcraftersDesk(windowID, inventory, null, packetBuffer);
    }

    public static ContainerSpellcraftersDesk server(int windowID, PlayerInventory inventory,
            TileEntitySpellcraftersDesk te) {
        return new ContainerSpellcraftersDesk(windowID, inventory, te, null);
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }

    @Override
    protected IInventory getIInventory() {
        return inventory;
    }
}
