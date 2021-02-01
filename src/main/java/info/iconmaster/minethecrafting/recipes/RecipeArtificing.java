package info.iconmaster.minethecrafting.recipes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import info.iconmaster.minethecrafting.Mana;
import info.iconmaster.minethecrafting.MineTheCrafting;
import info.iconmaster.minethecrafting.tes.TileEntityArtificersTable;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class RecipeArtificing implements IRecipe<RecipeArtificing.Inventory> {
    public static final ResourceLocation ID = new ResourceLocation(MineTheCrafting.MOD_ID, "artificing");

    public final ResourceLocation id;
    public Map<Mana, Integer> mana;
    public Set<SizedIngredient> inputs;
    public ItemStack output;

    public RecipeArtificing(ResourceLocation id, Map<Mana, Integer> mana, Set<SizedIngredient> inputs,
            ItemStack output) {
        this.id = id;
        this.mana = mana;
        this.inputs = inputs;
        this.output = output;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getCraftingResult(Inventory inventory) {
        for (Entry<Mana, Integer> entry : mana.entrySet()) {
            for (int i = 0; i < TileEntityArtificersTable.N_MANA_SLOTS; i++) {
                int slot = TileEntityArtificersTable.FIRST_MANA_SLOT + i;
                ItemStack stack = inventory.getStackInSlot(slot);

                if (Mana.fromItem(stack.getItem()) == entry.getKey() && stack.getCount() >= entry.getValue()) {
                    if (stack.getCount() == entry.getValue()) {
                        inventory.removeStackFromSlot(slot);
                    } else {
                        stack.setCount(stack.getCount() - entry.getValue());
                    }

                    break;
                }
            }
        }

        for (SizedIngredient input : inputs) {
            for (int i = 0; i < TileEntityArtificersTable.N_INPUT_SLOTS; i++) {
                int slot = TileEntityArtificersTable.FIRST_INPUT_SLOT + i;
                ItemStack stack = inventory.getStackInSlot(slot);

                if (input.test(stack)) {
                    if (stack.getCount() == input.count) {
                        inventory.removeStackFromSlot(slot);
                    } else {
                        stack.setCount(stack.getCount() - input.count);
                    }

                    break;
                }
            }
        }

        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output;
    }

    @Override
    public Serializer getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Type getType() {
        return TYPE;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        for (Entry<Mana, Integer> entry : mana.entrySet()) {
            boolean found = false;
            for (int i = 0; i < TileEntityArtificersTable.N_MANA_SLOTS; i++) {
                int slot = TileEntityArtificersTable.FIRST_MANA_SLOT + i;
                if (Mana.fromItem(inventory.getStackInSlot(slot).getItem()) == entry.getKey()
                        && inventory.getStackInSlot(slot).getCount() >= entry.getValue()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }

        for (int i = 0; i < TileEntityArtificersTable.N_MANA_SLOTS; i++) {
            int slot = TileEntityArtificersTable.FIRST_MANA_SLOT + i;
            ItemStack stack = inventory.getStackInSlot(slot);
            if (!stack.isEmpty() && !mana.containsKey(Mana.fromItem(stack.getItem()))) {
                return false;
            }
        }

        for (Ingredient input : inputs) {
            boolean found = false;
            for (int i = 0; i < TileEntityArtificersTable.N_INPUT_SLOTS; i++) {
                int slot = TileEntityArtificersTable.FIRST_INPUT_SLOT + i;
                if (input.test(inventory.getStackInSlot(slot))) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }

        for (int i = 0; i < TileEntityArtificersTable.N_INPUT_SLOTS; i++) {
            int slot = TileEntityArtificersTable.FIRST_INPUT_SLOT + i;
            ItemStack stack = inventory.getStackInSlot(slot);
            if (!stack.isEmpty() && inputs.stream().noneMatch(input -> input.test(stack))) {
                return false;
            }
        }

        return true;
    }

    public static class Type implements IRecipeType<RecipeArtificing> {
        @Override
        public String toString() {
            return ID.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<RecipeArtificing> {
        public Serializer() {
            this.setRegistryName(ID);
        }

        @Override
        public RecipeArtificing read(ResourceLocation id, JsonObject json) {
            Map<Mana, Integer> manas = new HashMap<>();
            for (Entry<String, JsonElement> entry : json.get("mana").getAsJsonObject().entrySet()) {
                manas.put(Mana.fromShortName(entry.getKey()), entry.getValue().getAsInt());
            }

            Set<SizedIngredient> inputs = new HashSet<>();
            for (JsonElement input : json.get("inputs").getAsJsonArray()) {
                inputs.add(SizedIngredient.deserialize(input));
            }

            return new RecipeArtificing(id, manas, inputs,
                    ShapedRecipe.deserializeItem(json.get("output").getAsJsonObject()));
        }

        @Override
        public RecipeArtificing read(ResourceLocation id, PacketBuffer packet) {
            Map<Mana, Integer> manas = new HashMap<>();
            int nMana = packet.readByte();
            for (int i = 0; i < nMana; i++) {
                int manaOrdinal = packet.readByte();
                int manaCount = packet.readByte();
                manas.put(Mana.values()[manaOrdinal], manaCount);
            }

            Set<SizedIngredient> inputs = new HashSet<>();
            int nInputs = packet.readByte();
            for (int i = 0; i < nInputs; i++) {
                inputs.add(SizedIngredient.read(packet));
            }

            return new RecipeArtificing(id, manas, inputs, packet.readItemStack());
        }

        @Override
        public void write(PacketBuffer packet, RecipeArtificing recipe) {
            packet.writeByte(recipe.mana.size());
            for (Entry<Mana, Integer> entry : recipe.mana.entrySet()) {
                packet.writeByte(entry.getKey().ordinal());
                packet.writeByte(entry.getValue());
            }
            packet.writeByte(recipe.inputs.size());
            for (SizedIngredient input : recipe.inputs) {
                input.writeSizedIngredient(packet);
            }
            packet.writeItemStack(recipe.output);
        }
    }

    public static class Inventory extends ProxyInventory {
        public Inventory(IInventory inventory) {
            super(inventory);
        }
    }

    public static final Type TYPE = new Type();
    public static final Serializer SERIALIZER = new Serializer();
}
