package info.iconmaster.minethecrafting.containers;

import info.iconmaster.minethecrafting.MineTheCrafting;
import info.iconmaster.minethecrafting.tes.TileEntityArtificersTable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class ContainerArtificersTable extends MTCContainer {
    PlayerInventory playerInventory;
    public IInventory inventory;
    public TileEntityArtificersTable.Data teData;

    public static final int GUI_WIDTH = 176, GUI_HEIGHT = 236, OUTPUT_X = 77, OUTPUT_Y = 66,
            HEIGHT_OFFSET = (GUI_HEIGHT - 166) / 2;
    public static final int[][] MANA_POS = new int[][] { new int[] { 77, 15 }, new int[] { 131, 53 },
            new int[] { 112, 112 }, new int[] { 44, 112 }, new int[] { 26, 53 }, };
    public static final int[][] INPUT_POS = new int[][] { new int[] { 49, 42 }, new int[] { 107, 43 },
            new int[] { 107, 89 }, new int[] { 49, 89 }, };

    private ContainerArtificersTable(int windowID, PlayerInventory playerInventory, TileEntityArtificersTable te,
            PacketBuffer packet) {
        super(MTCContainers.ARTIFICERS_TABLE.get(), windowID);
        this.playerInventory = playerInventory;

        if (te == null) {
            // client side
            inventory = new Inventory(TileEntityArtificersTable.N_SLOTS);
            teData = new TileEntityArtificersTable.Data();
        } else {
            // server side
            inventory = te;
            teData = te.data;
        }

        inventory.openInventory(playerInventory.player);

        for (int i = 0; i < TileEntityArtificersTable.N_MANA_SLOTS; i++) {
            addSlot(new SlotFiltered(inventory, TileEntityArtificersTable.FIRST_MANA_SLOT + i, MANA_POS[i][0],
                    MANA_POS[i][1] - HEIGHT_OFFSET, stack -> ItemTags.getCollection()
                            .get(new ResourceLocation(MineTheCrafting.MOD_ID, "mana")).contains(stack.getItem())));
        }

        for (int i = 0; i < TileEntityArtificersTable.N_INPUT_SLOTS; i++) {
            addSlot(new Slot(inventory, TileEntityArtificersTable.FIRST_INPUT_SLOT + i, INPUT_POS[i][0],
                    INPUT_POS[i][1] - HEIGHT_OFFSET));
        }

        addSlot(new SlotOutputOnly(inventory, TileEntityArtificersTable.OUTPUT_SLOT, OUTPUT_X,
                OUTPUT_Y - HEIGHT_OFFSET));

        addPlayerSlots(playerInventory, GUI_WIDTH, GUI_HEIGHT - HEIGHT_OFFSET);
        trackIntArray(teData);
    }

    public static ContainerArtificersTable client(int windowID, PlayerInventory inventory, PacketBuffer packetBuffer) {
        return new ContainerArtificersTable(windowID, inventory, null, packetBuffer);
    }

    public static ContainerArtificersTable server(int windowID, PlayerInventory inventory,
            TileEntityArtificersTable te) {
        return new ContainerArtificersTable(windowID, inventory, te, null);
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
