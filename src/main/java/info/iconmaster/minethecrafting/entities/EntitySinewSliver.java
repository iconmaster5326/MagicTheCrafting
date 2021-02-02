package info.iconmaster.minethecrafting.entities;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.AttributeModifierMap.MutableAttribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class EntitySinewSliver extends CreatureEntity implements IAnimatable {
    public static final String ID = "sinew_sliver";

    protected EntitySinewSliver(World world) {
        super(MTCEntities.SINEW_SLIVER.get(), world);
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        MutableAttribute map = AttributeModifierMap.createMutableAttribute();
        map.createMutableAttribute(Attributes.MAX_HEALTH, 20.0);
        map.createMutableAttribute(Attributes.FOLLOW_RANGE, 32.0);
        map.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.0);
        map.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.7);
        map.createMutableAttribute(Attributes.ATTACK_DAMAGE, 1.0);
        GlobalEntityTypeAttributes.put(MTCEntities.SINEW_SLIVER.get(), map.create());
        return map;
    }

    @Override
    public void travel(Vector3d target) {
        // TODO
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation." + ID + ".idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<EntitySinewSliver>(this, "controller", 0, this::predicate));
    }

    private AnimationFactory animFactory = new AnimationFactory(this);

    @Override
    public AnimationFactory getFactory() {
        return animFactory;
    }
}
