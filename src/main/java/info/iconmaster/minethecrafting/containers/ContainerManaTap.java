package info.iconmaster.minethecrafting.containers;

import java.util.ArrayList;
import java.util.List;

import info.iconmaster.minethecrafting.Mana;
import info.iconmaster.minethecrafting.screens.ScreenManaTap;
import info.iconmaster.minethecrafting.tes.TileEntityManaTap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketBuffer;

public class ContainerManaTap extends MTCContainer {
    PlayerInventory playerInventory;
    public IInventory inventory;
    public TileEntityManaTap.Data teData;
    public List<Mana> manaGeneratable;

    private ContainerManaTap(int windowID, PlayerInventory playerInventory, TileEntityManaTap te,
            PacketBuffer packet) {
        super(MTCContainers.MANA_TAP.get(), windowID);
        this.playerInventory = playerInventory;

        if (te == null) {
            // client side
            inventory = new Inventory(TileEntityManaTap.N_SLOTS);
            teData = new TileEntityManaTap.Data();
            manaGeneratable = new ArrayList<>();

            int n_mana = packet.readByte();
            for (int i = 0; i < n_mana; i++) {
                manaGeneratable.add(Mana.values()[packet.readByte()]);
            }
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

        addPlayerSlots(playerInventory, ScreenManaTap.WIDTH, ScreenManaTap.HEIGHT);
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
    protected IInventory getIInventory() {
        return inventory;
    }
}
