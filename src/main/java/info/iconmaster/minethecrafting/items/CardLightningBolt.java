package info.iconmaster.minethecrafting.items;

import java.util.Arrays;

import info.iconmaster.minethecrafting.Mana;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class CardLightningBolt extends ItemCard {
    public CardLightningBolt() {
        super(0, Arrays.asList(Mana.RED), Arrays.asList(Mana.RED));
    }

    @Override
    public void fireSpell(ItemStack stack, World world, PlayerEntity player) {
        RayTraceResult lookingAt = rayTrace(world, player, FluidMode.NONE);
        if (lookingAt != null && lookingAt.getType() != RayTraceResult.Type.MISS) {
            LightningBoltEntity bolt = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, world);
            double x = lookingAt.getHitVec().getX();
            double y = lookingAt.getHitVec().getY();
            double z = lookingAt.getHitVec().getZ();
            bolt.setPositionAndUpdate(x, y, z);
            world.addEntity(bolt);
        }
    }
}
