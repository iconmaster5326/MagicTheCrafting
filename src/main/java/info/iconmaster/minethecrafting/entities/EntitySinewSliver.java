package info.iconmaster.minethecrafting.entities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.AttributeModifierMap.MutableAttribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class EntitySinewSliver extends EntitySummoned implements IAnimatable {
    public static final String ID = "sinew_sliver";

    public EntitySinewSliver(World world) {
        this(world, null);
    }

    public EntitySinewSliver(World world, PlayerEntity summoner) {
        super(MTCEntities.SINEW_SLIVER.get(), world, summoner);
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        MutableAttribute map = AttributeModifierMap.createMutableAttribute();
        map.createMutableAttribute(Attributes.MAX_HEALTH, 20.0);
        map.createMutableAttribute(Attributes.FOLLOW_RANGE, 32.0);
        map.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.0);
        map.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.7);
        map.createMutableAttribute(Attributes.ATTACK_DAMAGE, 1.0);
        map.createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 1.0);
        map.createMutableAttribute(Attributes.ATTACK_SPEED, 1.0);
        map.createMutableAttribute(Attributes.ARMOR, 0.0);
        map.createMutableAttribute(Attributes.ARMOR_TOUGHNESS, 0.0);
        map.createMutableAttribute(ForgeMod.ENTITY_GRAVITY.get(), 1.0);
        map.createMutableAttribute(ForgeMod.SWIM_SPEED.get(), 0.7);
        GlobalEntityTypeAttributes.put(MTCEntities.SINEW_SLIVER.get(), map.create());
        return map;
    }

    private static AnimationBuilder ANIM_IDLE = new AnimationBuilder().addAnimation("animation." + ID + ".idle", true);
    private static AnimationBuilder ANIM_MOVE = new AnimationBuilder().addAnimation("animation." + ID + ".move", true);

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new EntitySummoned.GoalMoveToSummoner(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, getAttributeValue(Attributes.ATTACK_SPEED), false));
        this.goalSelector.addGoal(3,
                new NearestAttackableTargetGoal<LivingEntity>(this, LivingEntity.class, true, true));
        this.goalSelector.addGoal(4, new RandomWalkingGoal(this, getAttributeValue(Attributes.MOVEMENT_SPEED)));
        this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
    }

    @Override
    public boolean canAttack(LivingEntity entity) {
        PlayerEntity summoner = getSummoner();
        return entity != summoner && (entity instanceof MonsterEntity
                || (summoner != null && (summoner.getLastAttackedEntity() == entity)));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (getMotion().length() > 1) {
            event.getController().setAnimation(ANIM_MOVE);
        } else {
            event.getController().setAnimation(ANIM_IDLE);
        }

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(
                new AnimationController<EntitySinewSliver>(this, "controller", 20, this::predicate));
    }

    private AnimationFactory animFactory = new AnimationFactory(this);

    @Override
    public AnimationFactory getFactory() {
        return animFactory;
    }
}
