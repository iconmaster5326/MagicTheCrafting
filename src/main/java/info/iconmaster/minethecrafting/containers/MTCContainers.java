package info.iconmaster.minethecrafting.containers;

import info.iconmaster.minethecrafting.MineTheCrafting;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MTCContainers {
    private static final DeferredRegister<ContainerType<?>> CONTAINER_REGISTRY = DeferredRegister
            .create(ForgeRegistries.CONTAINERS, MineTheCrafting.MOD_ID);

    private MTCContainers() {
    }

    public static final RegistryObject<ContainerType<ContainerManaTap>> MANA_TAP = CONTAINER_REGISTRY
            .register("manatap", () -> IForgeContainerType.create(ContainerManaTap::client));
    public static final RegistryObject<ContainerType<ContainerSpellcraftersDesk>> SPELLCRAFTERS_DESK = CONTAINER_REGISTRY
            .register("spellcrafters_desk", () -> IForgeContainerType.create(ContainerSpellcraftersDesk::client));
    public static final RegistryObject<ContainerType<ContainerArtificersTable>> ARTIFICERS_TABLE = CONTAINER_REGISTRY
            .register("artificers_table", () -> IForgeContainerType.create(ContainerArtificersTable::client));

    public static void register() {
        CONTAINER_REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
