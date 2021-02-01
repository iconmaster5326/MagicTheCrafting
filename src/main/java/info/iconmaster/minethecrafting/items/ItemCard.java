package info.iconmaster.minethecrafting.items;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import info.iconmaster.minethecrafting.Mana;
import info.iconmaster.minethecrafting.MineTheCrafting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class ItemCard extends Item {
    public final int tier;
    public final Mana color;
    public final List<Mana> manaCost;
    public static final Set<ItemCard> ALL_CARDS = new HashSet<>();

    protected ItemCard(int tier, Mana color, List<Mana> manaCost) {
        super(new Item.Properties().maxStackSize(1).group(MineTheCrafting.ITEM_GROUP));
        this.tier = tier;
        this.color = color;
        this.manaCost = manaCost;
        ALL_CARDS.add(this);
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation cardArt() {
        ResourceLocation modelLocation = new ResourceLocation(MineTheCrafting.MOD_ID,
                "models/item/" + getRegistryName().getPath() + ".json");
        try {
            InputStream modelFile = Minecraft.getInstance().getResourceManager().getResource(modelLocation)
                    .getInputStream();
            Gson gson = new Gson();
            JsonObject modelJSON = gson.fromJson(new InputStreamReader(modelFile), JsonObject.class);
            return new ResourceLocation(modelJSON.get("art").getAsString());
        } catch (IOException | JsonParseException e) {
            MineTheCrafting.LOGGER.warn("could not load card model: " + modelLocation.toString());
            return null;
        }
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        setManaConsumed(stack, 0);
        player.setActiveHand(hand);
        return ActionResult.resultConsume(stack);
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity livingEntity, int timeLeft) {
        if (!(livingEntity instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity player = (PlayerEntity) livingEntity;

        if (!canCast(stack)) {
            int manaConsumed = getManaConsumed(stack);
            int timeInUse = getUseDuration(stack) - timeLeft + 1;
            if (timeInUse % getTicksNeededToConsumeOneMana(stack) == 0) {
                Mana toConsume = manaCost.get(manaConsumed);
                ItemStack manaStack = null;
                int manaSlot = -1;

                for (int slot = 0; slot < player.inventory.getSizeInventory(); slot++) {
                    ItemStack candidateStack = player.inventory.getStackInSlot(slot);
                    Mana mana = Mana.fromItem(candidateStack.getItem());
                    if (mana != null && candidateStack.getCount() > 0 && mana.usableToPayFor().contains(toConsume)) {
                        manaStack = candidateStack;
                        manaSlot = slot;
                        break;
                    }
                }

                if (manaStack != null) {
                    if (manaStack.getCount() == 1) {
                        player.inventory.removeStackFromSlot(manaSlot);
                    } else {
                        manaStack.setCount(manaStack.getCount() - 1);
                    }

                    manaConsumed++;
                    setManaConsumed(stack, manaConsumed);

                    if (player.world.isRemote) {
                        ResourceLocation soundEvent;
                        if (manaConsumed == manaCost.size()) {
                            soundEvent = new ResourceLocation(MineTheCrafting.MOD_ID, "spell_ready");
                        } else {
                            soundEvent = new ResourceLocation(MineTheCrafting.MOD_ID, "spell_consume_mana");
                        }
                        player.world.playSound(player, player.getPosition(), new SoundEvent(soundEvent),
                                SoundCategory.PLAYERS, 0.5f, 0.5f + 0.25f * manaConsumed);
                    }
                }
            }
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity player, int timeLeft) {
        if (canCast(stack) && player instanceof PlayerEntity) {
            // fire off the spell
            if (!player.world.isRemote) {
                player.world.playSound(null, player.getPosition(),
                        new SoundEvent(new ResourceLocation(MineTheCrafting.MOD_ID, "spell_cast")),
                        SoundCategory.PLAYERS, 0.5f, 1.0f);
            }

            fireSpell(stack, world, (PlayerEntity) player);
        }

        setManaConsumed(stack, 0);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity player, int slot, boolean selected) {
        if (!selected && DATA_MAP.containsKey(stack)) {
            setManaConsumed(stack, 0);
        }
    }

    private static final Map<ItemStack, Integer> DATA_MAP = new HashMap<>();

    public int getManaConsumed(ItemStack stack) {
        if (DATA_MAP.containsKey(stack)) {
            return DATA_MAP.get(stack);
        } else {
            return 0;
        }
    }

    public void setManaConsumed(ItemStack stack, int n) {
        if (stack.getTag() == null) {
            stack.setTag(new CompoundNBT());
        }

        if (n == 0) {
            DATA_MAP.remove(stack);
        } else {
            DATA_MAP.put(stack, n);
        }
    }

    public int getTicksNeededToConsumeOneMana(ItemStack stack) {
        return 20;
    }

    public boolean canCast(ItemStack stack) {
        return getManaConsumed(stack) >= manaCost.size();
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return canCast(stack);
    }

    public abstract void fireSpell(ItemStack stack, World world, PlayerEntity player);

    @Override
    public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);

        tooltip.add(new TranslationTextComponent(
                "tooltip." + MineTheCrafting.MOD_ID + "." + getRegistryName().getPath() + ".1")
                        .append(new StringTextComponent(" - ")).append(Mana.asTextComponent(manaCost)));
        tooltip.add(new TranslationTextComponent(
                "tooltip." + MineTheCrafting.MOD_ID + "." + getRegistryName().getPath() + ".2")
                        .mergeStyle(TextFormatting.DARK_GRAY));
    }
}
