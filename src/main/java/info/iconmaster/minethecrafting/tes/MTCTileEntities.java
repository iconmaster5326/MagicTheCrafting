package info.iconmaster.minethecrafting.tes;

import info.iconmaster.minethecrafting.MineTheCrafting;
import info.iconmaster.minethecrafting.blocks.MTCBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MTCTileEntities {
    private static final DeferredRegister<TileEntityType<?>> TE_REGISTRY = DeferredRegister
            .create(ForgeRegistries.TILE_ENTITIES, MineTheCrafting.MOD_ID);

    public static final RegistryObject<TileEntityType<TileEntityManaTap>> MANA_TAP = TE_REGISTRY.register("manatap",
            () -> TileEntityType.Builder.create(TileEntityManaTap::new, MTCBlocks.MANA_TAP.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntitySpellcraftersDesk>> SPELLCRAFTERS_DESK = TE_REGISTRY
            .register("spellcrafters_desk", () -> TileEntityType.Builder
                    .create(TileEntitySpellcraftersDesk::new, MTCBlocks.SPELLCRAFTERS_DESK.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityArtificersTable>> ARTIFICERS_TABLE = TE_REGISTRY
            .register("artificers_table", () -> TileEntityType.Builder
                    .create(TileEntityArtificersTable::new, MTCBlocks.ARTIFICERS_TABLE.get()).build(null));

    private MTCTileEntities() {
    }

    public static void register() {
        TE_REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
