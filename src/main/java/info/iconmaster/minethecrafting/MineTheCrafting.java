package info.iconmaster.minethecrafting;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import info.iconmaster.minethecrafting.containers.MTCContainers;
import info.iconmaster.minethecrafting.blocks.MTCBlocks;
import info.iconmaster.minethecrafting.items.MTCItems;
import info.iconmaster.minethecrafting.screens.ScreenManaTap;
import info.iconmaster.minethecrafting.tes.MTCTileEntities;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(MineTheCrafting.MOD_ID)
@EventBusSubscriber(modid = MineTheCrafting.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class MineTheCrafting {
    public static final String MOD_ID = "minethecrafting";
    public static final Logger LOGGER = LogManager.getLogger();

    public MineTheCrafting() {
        MTCBlocks.register();
        MTCTileEntities.register();
        MTCContainers.register();
        MTCItems.register();

        ManaTapRegistry.registerDefualtBiomes();
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void setupClient(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(MTCContainers.MANA_TAP.get(), ScreenManaTap::new);
    }
}
