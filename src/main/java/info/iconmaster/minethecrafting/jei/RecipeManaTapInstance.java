package info.iconmaster.minethecrafting.jei;

import java.util.Set;

import info.iconmaster.minethecrafting.Mana;
import net.minecraft.util.ResourceLocation;

public class RecipeManaTapInstance {
    public final ResourceLocation biome;
    public final Set<Mana> mana;

    public RecipeManaTapInstance(ResourceLocation biome, Set<Mana> set) {
        this.biome = biome;
        this.mana = set;
    }
}
