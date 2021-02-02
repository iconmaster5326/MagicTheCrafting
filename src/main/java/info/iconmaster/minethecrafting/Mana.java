package info.iconmaster.minethecrafting;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import info.iconmaster.minethecrafting.items.MTCItems;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public enum Mana {
    WHITE("white", TextFormatting.WHITE), BLUE("blue", TextFormatting.BLUE), BLACK("black", TextFormatting.DARK_GRAY),
    RED("red", TextFormatting.RED), GREEN("green", TextFormatting.GREEN), COLORLESS("colorless", TextFormatting.GRAY);

    private final String resourcePrefix;
    private final TextFormatting color;

    private Mana(String resourcePrefix, TextFormatting color) {
        this.resourcePrefix = resourcePrefix;
        this.color = color;
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

    public ResourceLocation textureLocation() {
        return new ResourceLocation(MineTheCrafting.MOD_ID, "item/" + resourcePrefix + "_mana");
    }

    public ResourceLocation fullTextureLocation() {
        return new ResourceLocation(MineTheCrafting.MOD_ID, "textures/item/" + resourcePrefix + "_mana.png");
    }

    public ResourceLocation cardFrontTextureLocation() {
        return new ResourceLocation(MineTheCrafting.MOD_ID, "item/" + resourcePrefix + "_card_front");
    }

    public ResourceLocation fullCardFrontTextureLocation() {
        return new ResourceLocation(MineTheCrafting.MOD_ID, "textures/item/" + resourcePrefix + "_card_front.png");
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

    public String characterLangKey() {
        return "manachar." + MineTheCrafting.MOD_ID + "." + resourcePrefix;
    }

    public String shortName() {
        return resourcePrefix;
    }

    public String character() {
        return I18n.format(characterLangKey());
    }

    public TextFormatting color() {
        return color;
    }

    public static IFormattableTextComponent asTextComponent(Iterable<Mana> manas) {
        IFormattableTextComponent result = new StringTextComponent("");
        int nColorless = 0;
        boolean manasEmpty = true;

        for (Mana mana : manas) {
            manasEmpty = false;

            if (mana == COLORLESS) {
                nColorless++;
            } else {
                result.append(new TranslationTextComponent(mana.characterLangKey()).mergeStyle(mana.color()));
            }
        }

        if (manasEmpty || nColorless > 0) {
            result = new StringTextComponent(Integer.toString(nColorless)).mergeStyle(COLORLESS.color()).append(result);
        }

        return result;
    }

    public static Mana fromShortName(String shortName) {
        for (Mana value : values()) {
            if (value.shortName().equals(shortName)) {
                return value;
            }
        }
        return null;
    }
}
