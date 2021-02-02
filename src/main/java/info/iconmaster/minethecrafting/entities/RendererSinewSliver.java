package info.iconmaster.minethecrafting.entities;

import info.iconmaster.minethecrafting.MineTheCrafting;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RendererSinewSliver extends GeoEntityRenderer<EntitySinewSliver> {
    public RendererSinewSliver(EntityRendererManager manager) {
        super(manager, new ModelSinewSliver());
    }

    @Override
    public ResourceLocation getEntityTexture(EntitySinewSliver entity) {
        return new ResourceLocation(MineTheCrafting.MOD_ID, "textures/entity/sinew_sliver.png");
    }
}
