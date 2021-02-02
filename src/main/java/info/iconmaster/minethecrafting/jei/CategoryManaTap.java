package info.iconmaster.minethecrafting.jei;

import java.awt.Color;
import java.util.stream.Collectors;

import com.mojang.blaze3d.matrix.MatrixStack;

import info.iconmaster.minethecrafting.MineTheCrafting;
import info.iconmaster.minethecrafting.blocks.MTCBlocks;
import info.iconmaster.minethecrafting.screens.ScreenManaTap;
import info.iconmaster.minethecrafting.tes.TileEntityManaTap;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CategoryManaTap implements IRecipeCategory<RecipeManaTapInstance> {
    public static final ResourceLocation ID = new ResourceLocation(MineTheCrafting.MOD_ID, "manatap");
    public static final int AREA_X = 3, AREA_Y = 8, AREA_W = 170, AREA_H = 60, TEXT_X = AREA_W / 2, TEXT_Y = 52;

    private final IDrawable icon, bg;

    public CategoryManaTap(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(MTCBlocks.MANA_TAP.get()));
        this.bg = guiHelper.createDrawable(ScreenManaTap.BACKGROUND, AREA_X, AREA_Y, AREA_W, AREA_H);
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends RecipeManaTapInstance> getRecipeClass() {
        return RecipeManaTapInstance.class;
    }

    @Override
    public String getTitle() {
        return MTCBlocks.MANA_TAP.get().getTranslatedName().getString();
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
    public void setIngredients(RecipeManaTapInstance recipe, IIngredients ingredients) {
        ingredients.setOutputs(VanillaTypes.ITEM,
                recipe.mana.stream().map(mana -> new ItemStack(mana.item())).collect(Collectors.toList()));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, RecipeManaTapInstance recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

        for (int i = 0; i < TileEntityManaTap.N_SLOTS; i++) {
            itemStacks.init(i, false, ScreenManaTap.OUTPUTS_X + 18 * i - AREA_X - 1,
                    ScreenManaTap.OUTPUTS_Y - AREA_Y - 1);
        }

        itemStacks.set(ingredients);
    }

    @Override
    public void draw(RecipeManaTapInstance recipe, MatrixStack ms, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, ms, mouseX, mouseY);

        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        String text = recipe.biome.toString();
        fontRenderer.drawString(ms, text, TEXT_X - fontRenderer.getStringWidth(text) / 2, TEXT_Y, Color.BLACK.getRGB());
    }
}
