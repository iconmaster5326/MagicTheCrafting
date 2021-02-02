package info.iconmaster.minethecrafting.entities;

import java.util.UUID;

import info.iconmaster.minethecrafting.MineTheCrafting;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
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

    public static class GoalMoveToSummoner extends Goal {
        private static final float MIN_DIST = 5.0f, MAX_DIST = 10.0f;
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

    
}
