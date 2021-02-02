package info.iconmaster.minethecrafting.entities;

import java.util.UUID;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntitySummoned extends CreatureEntity {
    private UUID summoner;

    public EntitySummoned(EntityType<? extends EntitySummoned> type, World world, PlayerEntity summoner) {
        this(type, world, summoner == null ? null : summoner.getUniqueID());
    }

    public EntitySummoned(EntityType<? extends EntitySummoned> type, World world, UUID summoner) {
        super(type, world);
        this.summoner = summoner;
    }

    @Override
    public void readAdditional(CompoundNBT nbt) {
        super.readAdditional(nbt);
        this.summoner = nbt.getUniqueId("summoner");
    }

    @Override
    public void writeAdditional(CompoundNBT nbt) {
        super.writeAdditional(nbt);
        nbt.putUniqueId("summoner", summoner);
    }

    public PlayerEntity getSummoner() {
        return world.getPlayerByUuid(summoner);
    }

    public UUID getSummonerID() {
        return summoner;
    }

    public void setSummoner(PlayerEntity player) {
        summoner = player.getUniqueID();
    }

    public void setSummoner(UUID player) {
        summoner = player;
    }

    @Override
    public boolean canAttack(LivingEntity entity) {
        if (entity == this || entity == null) {
            return false;
        }

        if (entity.getClassification(false) == EntityClassification.MONSTER) {
            return true;
        }

        PlayerEntity summoner = getSummoner();
        if (summoner != null) {
            if (entity == summoner) {
                return false;
            }

            if (summoner.getLastAttackedEntity() == entity) {
                return true;
            }

            DamageSource dmg = summoner.getLastDamageSource();
            if (dmg != null && dmg.getTrueSource() == entity) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean preventDespawn() {
        return summoner != null;
    }

    public static class GoalMoveToSummoner extends Goal {
        public static final float MIN_DIST = 5.0f, MAX_DIST = 10.0f;
        private EntitySummoned entity;

        public GoalMoveToSummoner(EntitySummoned entity) {
            this.entity = entity;
        }

        @Override
        public boolean shouldExecute() {
            PlayerEntity summoner = entity.getSummoner();
            return summoner != null && entity.getDistance(summoner) > MAX_DIST;
        }

        @Override
        public boolean shouldContinueExecuting() {
            PlayerEntity summoner = entity.getSummoner();
            return summoner != null && entity.getDistance(summoner) > MIN_DIST;
        }

        @Override
        public boolean isPreemptible() {
            return true;
        }

        @Override
        public void tick() {
            entity.navigator.tryMoveToEntityLiving(entity.getSummoner(),
                    entity.getAttributeValue(Attributes.MOVEMENT_SPEED));
        }

        @Override
        public void startExecuting() {

        }

        @Override
        public void resetTask() {
            entity.navigator.clearPath();
        }
    }

    public static class GoalMoveToAttackers extends NearestAttackableTargetGoal<LivingEntity> {
        public GoalMoveToAttackers(EntitySummoned entity) {
            super(entity, LivingEntity.class, true, true);
            this.targetEntitySelector = new EntityPredicate().allowFriendlyFire().setLineOfSiteRequired()
                    .setUseInvisibilityCheck().setCustomPredicate(target -> {
                        if (!entity.canAttack(target)) {
                            return false;
                        }

                        PlayerEntity summoner = entity.getSummoner();
                        if (summoner != null && target.getDistance(summoner) >= GoalMoveToSummoner.MAX_DIST) {
                            return false;
                        }

                        return true;
                    });
        }
    }
}
