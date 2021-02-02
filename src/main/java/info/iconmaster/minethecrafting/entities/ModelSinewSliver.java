package info.iconmaster.minethecrafting.entities;

import info.iconmaster.minethecrafting.MineTheCrafting;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelSinewSliver extends AnimatedGeoModel<EntitySinewSliver> {
    @Override
    public ResourceLocation getModelLocation(EntitySinewSliver entity) {
        return new ResourceLocation(MineTheCrafting.MOD_ID, "geo/" + EntitySinewSliver.ID + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySinewSliver entity) {
        return new ResourceLocation(MineTheCrafting.MOD_ID, "textures/entity/" + EntitySinewSliver.ID + ".png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntitySinewSliver entity) {
        return new ResourceLocation(MineTheCrafting.MOD_ID, "animations/" + EntitySinewSliver.ID + ".animation.json");
    }
}