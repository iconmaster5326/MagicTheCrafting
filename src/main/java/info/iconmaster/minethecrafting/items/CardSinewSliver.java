package info.iconmaster.minethecrafting.items;

import java.util.Arrays;

import info.iconmaster.minethecrafting.Mana;
import info.iconmaster.minethecrafting.entities.EntitySinewSliver;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class CardSinewSliver extends ItemCard {
    public CardSinewSliver() {
        super(0, Arrays.asList(Mana.WHITE), Arrays.asList(Mana.WHITE, Mana.COLORLESS));
    }

    @Override
    public void fireSpell(ItemStack stack, World world, PlayerEntity player) {
        RayTraceResult lookingAt = rayTrace(world, player, FluidMode.NONE);
        if (lookingAt != null && lookingAt.getType() != RayTraceResult.Type.MISS) {
            EntitySinewSliver summon = new EntitySinewSliver(world, player);
            double x = lookingAt.getHitVec().getX();
            double y = lookingAt.getHitVec().getY();
            double z = lookingAt.getHitVec().getZ();
            summon.setPositionAndUpdate(x, y, z);
            world.addEntity(summon);
        }
    }
}
