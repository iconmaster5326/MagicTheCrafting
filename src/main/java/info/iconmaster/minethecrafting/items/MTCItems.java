package info.iconmaster.minethecrafting.items;

import java.util.Arrays;

import info.iconmaster.minethecrafting.Mana;
import info.iconmaster.minethecrafting.MineTheCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MTCItems {
    private MTCItems() {
    }

    private static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS,
            MineTheCrafting.MOD_ID);

    public static final RegistryObject<Item> WHITE_MANA = ITEM_REGISTRY.register("white_mana",
            () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
    public static final RegistryObject<Item> BLUE_MANA = ITEM_REGISTRY.register("blue_mana",
            () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
    public static final RegistryObject<Item> BLACK_MANA = ITEM_REGISTRY.register("black_mana",
            () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
    public static final RegistryObject<Item> RED_MANA = ITEM_REGISTRY.register("red_mana",
            () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
    public static final RegistryObject<Item> GREEN_MANA = ITEM_REGISTRY.register("green_mana",
            () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
    public static final RegistryObject<Item> COLORLESS_MANA = ITEM_REGISTRY.register("colorless_mana",
            () -> new Item(new Item.Properties().group(ItemGroup.MISC)));

    public static final RegistryObject<Item> BLANK_CARD = ITEM_REGISTRY.register("blank_card",
            () -> new Item(new Item.Properties().group(ItemGroup.MISC)));

    public static final RegistryObject<Item> CARD_LIGHTNING_BOLT = ITEM_REGISTRY.register("card_lightning_bolt",
            () -> new CardLightningBolt());

    public static void register() {
        ITEM_REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
