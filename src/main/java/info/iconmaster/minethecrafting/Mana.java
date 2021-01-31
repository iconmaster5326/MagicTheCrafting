package info.iconmaster.minethecrafting;

import info.iconmaster.minethecrafting.items.MTCItems;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public enum Mana {
    WHITE("white"), BLUE("blue"), BLACK("black"), RED("red"), GREEN("green");

    private final String resourcePrefix;

    private Mana(String resourcePrefix) {
        this.resourcePrefix = resourcePrefix;
    }

    public Item item() {
        switch (this) {
            case WHITE:
                return MTCItems.WHITE_MANA.get();
            case BLUE:
                return MTCItems.BLUE_MANA.get();
            case BLACK:
                return MTCItems.BLACK_MANA.get();
            case RED:
                return MTCItems.RED_MANA.get();
            case GREEN:
                return MTCItems.GREEN_MANA.get();
        }

        return null;
    }

    public ResourceLocation texture() {
        return new ResourceLocation(MineTheCrafting.MOD_ID + ":textures/item/" + resourcePrefix + "_mana.png");
    }
}
