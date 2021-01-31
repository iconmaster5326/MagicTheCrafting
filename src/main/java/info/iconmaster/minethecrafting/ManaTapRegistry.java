package info.iconmaster.minethecrafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class ManaTapRegistry {
    private ManaTapRegistry() {
    }

    private static Map<String, List<Mana>> biomeToMana = new HashMap<>();

    public static void register(String biome, List<Mana> mana) {
        biomeToMana.put(biome, mana);
    }

    public static List<Mana> getMana(Biome biome) {
        return biomeToMana.getOrDefault(biome.getRegistryName().toString(), new ArrayList<>());
    }

    public static List<Mana> getMana(World world, BlockPos pos) {
        return getMana(world.getBiome(pos));
    }

    public static void registerDefualtBiomes() {
        register("minecraft:ocean", Arrays.asList(Mana.BLUE));
        register("minecraft:deep_ocean", Arrays.asList(Mana.BLUE));
        register("minecraft:frozen_ocean", Arrays.asList(Mana.BLUE));
        register("minecraft:deep_frozen_ocean", Arrays.asList(Mana.BLUE));
        register("minecraft:cold_ocean", Arrays.asList(Mana.BLUE));
        register("minecraft:deep_cold_ocean", Arrays.asList(Mana.BLUE));
        register("minecraft:lukewarm_ocean", Arrays.asList(Mana.BLUE));
        register("minecraft:deep_lukewarm_ocean", Arrays.asList(Mana.BLUE));
        register("minecraft:warm_ocean", Arrays.asList(Mana.BLUE));
        register("minecraft:deep_warm_ocean", Arrays.asList(Mana.BLUE));
        register("minecraft:river", Arrays.asList(Mana.BLUE));
        register("minecraft:frozen_river", Arrays.asList(Mana.BLUE));
        register("minecraft:beach", Arrays.asList(Mana.BLUE));
        register("minecraft:stone_shore", Arrays.asList(Mana.BLUE));
        register("minecraft:snowy_beach", Arrays.asList(Mana.BLUE));
        register("minecraft:forest", Arrays.asList(Mana.GREEN));
        register("minecraft:wooded_hills", Arrays.asList(Mana.GREEN));
        register("minecraft:flower_forest", Arrays.asList(Mana.GREEN));
        register("minecraft:birch_forest", Arrays.asList(Mana.GREEN));
        register("minecraft:birch_forest_hills", Arrays.asList(Mana.GREEN));
        register("minecraft:tall_birch_forest", Arrays.asList(Mana.GREEN));
        register("minecraft:tall_birch_hills", Arrays.asList(Mana.GREEN));
        register("minecraft:dark_forest", Arrays.asList(Mana.GREEN, Mana.BLACK));
        register("minecraft:dark_forest_hills", Arrays.asList(Mana.GREEN, Mana.BLACK));
        register("minecraft:jungle", Arrays.asList(Mana.GREEN));
        register("minecraft:jungle_hills", Arrays.asList(Mana.GREEN));
        register("minecraft:modified_jungle", Arrays.asList(Mana.GREEN));
        register("minecraft:jungle_edge", Arrays.asList(Mana.GREEN));
        register("minecraft:modified_jungle_edge", Arrays.asList(Mana.GREEN));
        register("minecraft:bamboo_jungle", Arrays.asList(Mana.GREEN, Mana.WHITE));
        register("minecraft:bamboo_jungle_hills", Arrays.asList(Mana.GREEN, Mana.WHITE));
        register("minecraft:taiga", Arrays.asList(Mana.GREEN));
        register("minecraft:taiga_hills", Arrays.asList(Mana.GREEN));
        register("minecraft:taiga_mountains", Arrays.asList(Mana.GREEN, Mana.RED));
        register("minecraft:snowy_taiga", Arrays.asList(Mana.GREEN));
        register("minecraft:snowy_taiga_hills", Arrays.asList(Mana.GREEN));
        register("minecraft:snowy_taiga_mountains", Arrays.asList(Mana.GREEN, Mana.RED));
        register("minecraft:giant_tree_taiga", Arrays.asList(Mana.GREEN));
        register("minecraft:giant_tree_taiga_hills", Arrays.asList(Mana.GREEN));
        register("minecraft:giant_spruce_taiga", Arrays.asList(Mana.GREEN));
        register("minecraft:giant_spruce_taiga_hills", Arrays.asList(Mana.GREEN));
        register("minecraft:mushroom_fields", Arrays.asList(Mana.GREEN, Mana.BLACK, Mana.BLUE));
        register("minecraft:mushroom_field_shore", Arrays.asList(Mana.GREEN, Mana.BLACK, Mana.BLUE));
        register("minecraft:swamp", Arrays.asList(Mana.BLACK));
        register("minecraft:swamp_hills", Arrays.asList(Mana.BLACK));
        register("minecraft:savanna", Arrays.asList(Mana.WHITE));
        register("minecraft:savanna_plateau", Arrays.asList(Mana.WHITE));
        register("minecraft:shattered_savanna", Arrays.asList(Mana.WHITE, Mana.RED));
        register("minecraft:shattered_savanna_plateau", Arrays.asList(Mana.WHITE, Mana.RED));
        register("minecraft:plains", Arrays.asList(Mana.WHITE));
        register("minecraft:sunflower_plains", Arrays.asList(Mana.WHITE));
        register("minecraft:desert", Arrays.asList(Mana.COLORLESS));
        register("minecraft:desert_hills", Arrays.asList(Mana.COLORLESS));
        register("minecraft:desert_lakes", Arrays.asList(Mana.COLORLESS));
        register("minecraft:snowy_tundra", Arrays.asList(Mana.BLUE));
        register("minecraft:snowy_mountains", Arrays.asList(Mana.BLUE, Mana.RED));
        register("minecraft:ice_spikes", Arrays.asList(Mana.BLUE, Mana.RED));
        register("minecraft:mountains", Arrays.asList(Mana.RED));
        register("minecraft:wooded_mountains", Arrays.asList(Mana.RED, Mana.GREEN));
        register("minecraft:gravelly_mountains", Arrays.asList(Mana.RED));
        register("minecraft:modified_gravelly_mountains", Arrays.asList(Mana.RED));
        register("minecraft:mountain_edge", Arrays.asList(Mana.RED));
        register("minecraft:badlands", Arrays.asList(Mana.COLORLESS));
        register("minecraft:badlands_plateau", Arrays.asList(Mana.COLORLESS));
        register("minecraft:modified_badlands_plateau", Arrays.asList(Mana.COLORLESS));
        register("minecraft:wooded_badlands_plateau", Arrays.asList(Mana.COLORLESS));
        register("minecraft:modified_wooded_badlands_plateau", Arrays.asList(Mana.COLORLESS));
        register("minecraft:eroded_badlands", Arrays.asList(Mana.COLORLESS));
    }
}
