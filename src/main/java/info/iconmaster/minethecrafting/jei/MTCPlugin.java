package info.iconmaster.minethecrafting.jei;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import info.iconmaster.minethecrafting.Mana;
import info.iconmaster.minethecrafting.MineTheCrafting;
import info.iconmaster.minethecrafting.blocks.MTCBlocks;
import info.iconmaster.minethecrafting.recipes.RecipeManaTap;
import info.iconmaster.minethecrafting.screens.ScreenManaTap;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class MTCPlugin implements IModPlugin {
    public static final ResourceLocation ID = new ResourceLocation(MineTheCrafting.MOD_ID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new CategoryManaTap(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        Set<RecipeManaTapInstance> recipes = new HashSet<>();
        for (RecipeManaTap rawRecipe : Minecraft.getInstance().world.getRecipeManager()
                .getRecipesForType(RecipeManaTap.TYPE)) {
            for (Entry<ResourceLocation, Set<Mana>> entry : rawRecipe.biomeToMana.entrySet()) {
                recipes.add(new RecipeManaTapInstance(entry.getKey(), entry.getValue()));
            }
        }
        registry.addRecipes(recipes, CategoryManaTap.ID);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(MTCBlocks.MANA_TAP.get()), CategoryManaTap.ID);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registry) {
        registry.addRecipeClickArea(ScreenManaTap.class, ScreenManaTap.PROGRESS_X, ScreenManaTap.PROGRESS_Y,
                ScreenManaTap.PROGRESS_W, ScreenManaTap.PROGRESS_H, CategoryManaTap.ID);
    }
}
