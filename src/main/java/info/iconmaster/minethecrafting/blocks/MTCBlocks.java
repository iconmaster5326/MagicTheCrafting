package info.iconmaster.minethecrafting.blocks;

import java.util.function.Supplier;

import info.iconmaster.minethecrafting.MineTheCrafting;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MTCBlocks {
    private static final DeferredRegister<Block> BLOCK_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS,
            MineTheCrafting.MOD_ID);
    private static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS,
            MineTheCrafting.MOD_ID);

    private MTCBlocks() {
    }

    private static <T extends Block> RegistryObject<T> createRegistryObject(String name, Supplier<T> t) {
        RegistryObject<T> block = BLOCK_REGISTRY.register(name, t);
        ITEM_REGISTRY.register(name, () -> new BlockItem(block.get(), new Item.Properties().group(MineTheCrafting.ITEM_GROUP)));
        return block;
    }

    public static final RegistryObject<BlockManaTap> MANA_TAP = createRegistryObject("manatap",
            () -> new BlockManaTap());

    public static void register() {
        BLOCK_REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEM_REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
