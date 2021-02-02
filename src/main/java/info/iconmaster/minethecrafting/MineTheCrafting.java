package info.iconmaster.minethecrafting;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import info.iconmaster.minethecrafting.blocks.MTCBlocks;
import info.iconmaster.minethecrafting.containers.MTCContainers;
import info.iconmaster.minethecrafting.entities.MTCEntities;
import info.iconmaster.minethecrafting.entities.RendererSinewSliver;
import info.iconmaster.minethecrafting.items.ItemCard;
import info.iconmaster.minethecrafting.items.MTCItems;
import info.iconmaster.minethecrafting.models.CardLoader;
import info.iconmaster.minethecrafting.recipes.RecipeArtificing;
import info.iconmaster.minethecrafting.recipes.RecipeManaTap;
import info.iconmaster.minethecrafting.screens.ScreenArtificersTable;
import info.iconmaster.minethecrafting.screens.ScreenManaTap;
import info.iconmaster.minethecrafting.screens.ScreenSpellcraftersDesk;
import info.iconmaster.minethecrafting.tes.MTCTileEntities;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.geckolib3.GeckoLib;

@Mod(MineTheCrafting.MOD_ID)
@EventBusSubscriber(modid = MineTheCrafting.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class MineTheCrafting {
    public static final String MOD_ID = "minethecrafting";
    public static final Logger LOGGER = LogManager.getLogger();

    public MineTheCrafting() {
        GeckoLib.initialize();
        
        MTCBlocks.register();
        MTCTileEntities.register();
        MTCContainers.register();
        MTCItems.register();
        MTCEntities.register();
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void setupClient(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(MTCContainers.MANA_TAP.get(), ScreenManaTap::new);
        ScreenManager.registerFactory(MTCContainers.SPELLCRAFTERS_DESK.get(), ScreenSpellcraftersDesk::new);
        ScreenManager.registerFactory(MTCContainers.ARTIFICERS_TABLE.get(), ScreenArtificersTable::new);

        RenderingRegistry.registerEntityRenderingHandler(MTCEntities.SINEW_SLIVER.get(), RendererSinewSliver::new);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerModelLoaders(final ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(MOD_ID, "card_loader"), new CardLoader());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerTextures(final TextureStitchEvent.Pre event) {
        if (event.getMap().getTextureLocation().equals(PlayerContainer.LOCATION_BLOCKS_TEXTURE)) {
            for (Mana mana : Mana.values()) {
                event.addSprite(mana.cardFrontTextureLocation());
            }

            for (ItemCard card : ItemCard.ALL_CARDS) {
                ResourceLocation art = card.cardArt();
                if (art != null) {
                    event.addSprite(art);
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerRecipeSerializers(Register<IRecipeSerializer<?>> event) {
        Registry.register(Registry.RECIPE_TYPE, RecipeArtificing.ID, RecipeArtificing.TYPE);
        event.getRegistry().register(RecipeArtificing.SERIALIZER);

        Registry.register(Registry.RECIPE_TYPE, RecipeManaTap.ID, RecipeManaTap.TYPE);
        event.getRegistry().register(RecipeManaTap.SERIALIZER);
    }

    public static final ItemGroup ITEM_GROUP = (new ItemGroup(MOD_ID) {
        ItemStack randomMana = null;

        @Override
        public ItemStack createIcon() {
            if (randomMana == null) {
                randomMana = new ItemStack(Mana.values()[new Random().nextInt(Mana.values().length)].item());
            }
            return randomMana;
        }
    });
}
