package info.iconmaster.minethecrafting.entities;

import info.iconmaster.minethecrafting.MineTheCrafting;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = MineTheCrafting.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class MTCEntities {
    private static final DeferredRegister<EntityType<?>> ENTITY_REGISTRY = DeferredRegister
            .create(ForgeRegistries.ENTITIES, MineTheCrafting.MOD_ID);

    private MTCEntities() {
    }

    public static final RegistryObject<EntityType<EntitySinewSliver>> SINEW_SLIVER = ENTITY_REGISTRY.register(
            EntitySinewSliver.ID,
            () -> EntityType.Builder
                    .<EntitySinewSliver>create((type, world) -> new EntitySinewSliver(world),
                            EntityClassification.MONSTER)
                    .setShouldReceiveVelocityUpdates(true).build(EntitySinewSliver.ID));

    public static void register() {
        ENTITY_REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    public static void registerEntityAttributes(final FMLCommonSetupEvent event) {
        EntitySinewSliver.registerAttributes();
    }
}
