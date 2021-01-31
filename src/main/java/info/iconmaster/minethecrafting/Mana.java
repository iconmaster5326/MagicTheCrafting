package info.iconmaster.minethecrafting;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.iconmaster.minethecrafting.items.MTCItems;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public enum Mana {
    WHITE("white"), BLUE("blue"), BLACK("black"), RED("red"), GREEN("green"), COLORLESS("colorless");

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
            case COLORLESS:
                return MTCItems.COLORLESS_MANA.get();
        }

        return null;
    }

    public ResourceLocation texture() {
        return new ResourceLocation(MineTheCrafting.MOD_ID + ":textures/item/" + resourcePrefix + "_mana.png");
    }

    public static Mana fromItem(Item item) {
        if (item == MTCItems.WHITE_MANA.get()) {
            return WHITE;
        } else if (item == MTCItems.BLUE_MANA.get()) {
            return BLUE;
        } else if (item == MTCItems.BLACK_MANA.get()) {
            return BLACK;
        } else if (item == MTCItems.RED_MANA.get()) {
            return RED;
        } else if (item == MTCItems.GREEN_MANA.get()) {
            return GREEN;
        } else if (item == MTCItems.COLORLESS_MANA.get()) {
            return COLORLESS;
        } else {
            return null;
        }
    }

    private static final Map<Mana, List<Mana>> usableToPayForMap = new EnumMap<Mana, List<Mana>>(Mana.class) {
        {
            put(WHITE, Arrays.asList(WHITE, COLORLESS));
            put(BLUE, Arrays.asList(BLUE, COLORLESS));
            put(BLACK, Arrays.asList(BLACK, COLORLESS));
            put(RED, Arrays.asList(RED, COLORLESS));
            put(GREEN, Arrays.asList(GREEN, COLORLESS));
            put(COLORLESS, Arrays.asList(COLORLESS));
        }
    };

    public List<Mana> usableToPayFor() {
        return usableToPayForMap.get(this);
    }
}
