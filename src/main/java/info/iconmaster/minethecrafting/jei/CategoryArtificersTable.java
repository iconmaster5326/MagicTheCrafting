package info.iconmaster.minethecrafting.jei;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import info.iconmaster.minethecrafting.Mana;
import info.iconmaster.minethecrafting.MineTheCrafting;
import info.iconmaster.minethecrafting.blocks.MTCBlocks;
import info.iconmaster.minethecrafting.containers.ContainerArtificersTable;
import info.iconmaster.minethecrafting.recipes.RecipeArtificing;
import info.iconmaster.minethecrafting.recipes.SizedIngredient;
import info.iconmaster.minethecrafting.screens.ScreenArtificersTable;
import info.iconmaster.minethecrafting.tes.TileEntityArtificersTable;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class CategoryArtificersTable implements IRecipeCategory<RecipeArtificing> {
    public static final ResourceLocation ID = new ResourceLocation(MineTheCrafting.MOD_ID, "artificers_table");
    public static final int AREA_X = 22, AREA_Y = 10, AREA_W = 129, AREA_H = 127;

    private final IDrawable icon, bg;

    public CategoryArtificersTable(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(MTCBlocks.ARTIFICERS_TABLE.get()));
        this.bg = guiHelper.createDrawable(ScreenArtificersTable.BACKGROUND, AREA_X, AREA_Y, AREA_W, AREA_H);
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends RecipeArtificing> getRecipeClass() {
        return RecipeArtificing.class;
    }

    @Override
    public String getTitle() {
        return MTCBlocks.ARTIFICERS_TABLE.get().getTranslatedName().getString();
    }

    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(RecipeArtificing recipe, IIngredients ingredients) {
        List<Ingredient> inputs = new ArrayList<>();

        for (Entry<Mana, Integer> entry : recipe.mana.entrySet()) {
            inputs.add(
                    new SizedIngredient(Ingredient.fromStacks(new ItemStack(entry.getKey().item())), entry.getValue()));
        }
        for (int i = 0; i < TileEntityArtificersTable.N_MANA_SLOTS - recipe.mana.size(); i++) {
            inputs.add(Ingredient.EMPTY);
        }

        for (SizedIngredient input : recipe.inputs) {
            inputs.add(input);
        }

        ingredients.setOutput(VanillaTypes.ITEM, recipe.output);
        ingredients.setInputIngredients(inputs);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, RecipeArtificing recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

        for (int i = 0; i < TileEntityArtificersTable.N_MANA_SLOTS; i++) {
            itemStacks.init(TileEntityArtificersTable.FIRST_MANA_SLOT + i, true,
                    ContainerArtificersTable.MANA_POS[i][0] - AREA_X - 1,
                    ContainerArtificersTable.MANA_POS[i][1] - AREA_Y - 1);
        }

        for (int i = 0; i < TileEntityArtificersTable.N_INPUT_SLOTS; i++) {
            itemStacks.init(TileEntityArtificersTable.FIRST_INPUT_SLOT + i, true,
                    ContainerArtificersTable.INPUT_POS[i][0] - AREA_X - 1,
                    ContainerArtificersTable.INPUT_POS[i][1] - AREA_Y - 1);
        }

        itemStacks.init(TileEntityArtificersTable.OUTPUT_SLOT, false, ContainerArtificersTable.OUTPUT_X - AREA_X - 1,
                ContainerArtificersTable.OUTPUT_Y - AREA_Y - 1);

        itemStacks.set(ingredients);
    }
}
