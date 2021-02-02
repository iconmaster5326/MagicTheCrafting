package info.iconmaster.minethecrafting.jei;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import info.iconmaster.minethecrafting.Mana;
import info.iconmaster.minethecrafting.MineTheCrafting;
import info.iconmaster.minethecrafting.blocks.MTCBlocks;
import info.iconmaster.minethecrafting.containers.ContainerSpellcraftersDesk;
import info.iconmaster.minethecrafting.items.ItemCard;
import info.iconmaster.minethecrafting.items.MTCItems;
import info.iconmaster.minethecrafting.screens.ScreenSpellcraftersDesk;
import info.iconmaster.minethecrafting.tes.TileEntitySpellcraftersDesk;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CategorySpellcraftersDesk implements IRecipeCategory<ItemCard> {
    public static final ResourceLocation ID = new ResourceLocation(MineTheCrafting.MOD_ID, "spellcrafters_desk");
    public static final int AREA_X = 16, AREA_Y = 22, AREA_W = 143, AREA_H = 42;

    private final IDrawable icon, bg;

    public CategorySpellcraftersDesk(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(MTCBlocks.SPELLCRAFTERS_DESK.get()));
        this.bg = guiHelper.createDrawable(ScreenSpellcraftersDesk.BACKGROUND, AREA_X, AREA_Y, AREA_W, AREA_H);
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends ItemCard> getRecipeClass() {
        return ItemCard.class;
    }

    @Override
    public String getTitle() {
        return MTCBlocks.SPELLCRAFTERS_DESK.get().getTranslatedName().getString();
    }

    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    private static Collection<ItemStack> getInputs(ItemCard recipe) {
        Map<Mana, ItemStack> manaMap = new EnumMap<>(Mana.class);
        for (Mana mana : recipe.manaCost) {
            manaMap.putIfAbsent(mana, new ItemStack(mana.item(), 1));
        }
        return manaMap.values();
    }

    @Override
    public void setIngredients(ItemCard recipe, IIngredients ingredients) {
        List<ItemStack> inputs = new ArrayList<>();
        inputs.add(new ItemStack(MTCItems.BLANK_CARD.get()));
        inputs.addAll(getInputs(recipe));
        ingredients.setInputs(VanillaTypes.ITEM, inputs);
        ingredients.setOutput(VanillaTypes.ITEM, new ItemStack(recipe));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ItemCard recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

        itemStacks.init(TileEntitySpellcraftersDesk.CARD_SLOT, true, ContainerSpellcraftersDesk.INPUT_X - AREA_X - 1,
                ContainerSpellcraftersDesk.INPUT_Y - AREA_Y - 1);

        int i = 0;
        for (ItemStack mana : getInputs(recipe)) {
            int row = i % 3, col = i / 3;
            itemStacks.init(TileEntitySpellcraftersDesk.FIRST_MANA_SLOT + i, true,
                    ContainerSpellcraftersDesk.MANAS_X + 18 * row - AREA_X - 1,
                    ContainerSpellcraftersDesk.MANAS_Y + 18 * col - AREA_Y - 1);
            i++;
        }

        itemStacks.init(TileEntitySpellcraftersDesk.OUTPUT_SLOT, false,
                ContainerSpellcraftersDesk.OUTPUT_X - AREA_X - 1, ContainerSpellcraftersDesk.OUTPUT_Y - AREA_Y - 1);

        itemStacks.set(ingredients);
    }
}
