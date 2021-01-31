package info.iconmaster.minethecrafting.models;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import info.iconmaster.minethecrafting.items.ItemCard;
import info.iconmaster.minethecrafting.items.MTCItems;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.geometry.IModelGeometry;

public class CardModel implements IModelGeometry<CardModel> {
    public ResourceLocation cardArt;

    public CardModel(ResourceLocation cardArt) {
        this.cardArt = cardArt;
    }

    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery,
            Function<RenderMaterial, TextureAtlasSprite> textureLoader, IModelTransform transform,
            ItemOverrideList overrides, ResourceLocation modelLocation) {
        return new ItemLayerModel(ImmutableList
                .of(new RenderMaterial(PlayerContainer.LOCATION_BLOCKS_TEXTURE, MTCItems.BLANK_CARD.getId()))).bake(
                        owner, bakery, textureLoader, transform,
                        new OverrideHandler(this, owner, bakery, textureLoader, transform, modelLocation),
                        modelLocation);
    }

    @Override
    public Collection<RenderMaterial> getTextures(IModelConfiguration owner,
            Function<ResourceLocation, IUnbakedModel> modelLoader, Set<Pair<String, String>> errors) {
        return Arrays.asList();
    }

    private static final class OverrideHandler extends ItemOverrideList {

        private final CardModel model;
        private final ModelBakery bakery;
        private final IModelConfiguration owner;
        private final Function<RenderMaterial, TextureAtlasSprite> textureLoader;
        private final IModelTransform transform;
        private final ResourceLocation modelLocation;

        public OverrideHandler(CardModel model, IModelConfiguration owner, ModelBakery bakery,
                Function<RenderMaterial, TextureAtlasSprite> textureLoader, IModelTransform transform,
                ResourceLocation modelLocation) {
            super();
            this.model = model;
            this.owner = owner;
            this.bakery = bakery;
            this.textureLoader = textureLoader;
            this.modelLocation = modelLocation;
            this.transform = transform;
        }

        @Nonnull
        @Override
        public IBakedModel getOverrideModel(@Nonnull IBakedModel originalModel, @Nonnull ItemStack stack,
                @Nullable ClientWorld world, @Nullable LivingEntity entityIn) {
            ItemCard card = (ItemCard) stack.getItem();
            return new ItemLayerModel(ImmutableList.of(
                    new RenderMaterial(PlayerContainer.LOCATION_BLOCKS_TEXTURE, card.color.cardFrontTextureLocation()),
                    new RenderMaterial(PlayerContainer.LOCATION_BLOCKS_TEXTURE, model.cardArt))).bake(owner, bakery,
                            textureLoader, transform, ItemOverrideList.EMPTY, modelLocation);
        }
    }
}
