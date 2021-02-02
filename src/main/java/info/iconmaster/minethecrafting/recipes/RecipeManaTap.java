package info.iconmaster.minethecrafting.recipes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import info.iconmaster.minethecrafting.Mana;
import info.iconmaster.minethecrafting.MineTheCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class RecipeManaTap implements IRecipe<RecipeManaTap.Inventory> {
    public static final ResourceLocation ID = new ResourceLocation(MineTheCrafting.MOD_ID, "manatap");

    public final ResourceLocation id;
    public final Map<ResourceLocation, Set<Mana>> biomeToMana;

    public RecipeManaTap(ResourceLocation id, Map<ResourceLocation, Set<Mana>> biomeToMana) {
        this.id = id;
        this.biomeToMana = biomeToMana;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(Inventory inventory) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
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
        return false;
    }

    public static class Type implements IRecipeType<RecipeManaTap> {
        @Override
        public String toString() {
            return ID.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<RecipeManaTap> {
        public Serializer() {
            this.setRegistryName(ID);
        }

        @Override
        public RecipeManaTap read(ResourceLocation id, JsonObject json) {
            Map<ResourceLocation, Set<Mana>> biomeToMana = new HashMap<>();
            for (Entry<String, JsonElement> entry : json.get("biomes").getAsJsonObject().entrySet()) {
                biomeToMana.put(new ResourceLocation(entry.getKey()),
                        StreamSupport.stream(entry.getValue().getAsJsonArray().spliterator(), false)
                                .map(e -> Mana.fromShortName(e.getAsString())).collect(Collectors.toSet()));
            }
            return new RecipeManaTap(id, biomeToMana);
        }

        @Override
        public RecipeManaTap read(ResourceLocation id, PacketBuffer packet) {
            Map<ResourceLocation, Set<Mana>> biomeToMana = new HashMap<>();
            int nEntries = packet.readInt();
            for (int i = 0; i < nEntries; i++) {
                String biome = packet.readString();
                int nMana = packet.readByte();
                Set<Mana> manas = new HashSet<>();
                for (int j = 0; j < nMana; j++) {
                    manas.add(Mana.values()[packet.readByte()]);
                }
                biomeToMana.put(new ResourceLocation(biome), manas);
            }
            return new RecipeManaTap(id, biomeToMana);
        }

        @Override
        public void write(PacketBuffer packet, RecipeManaTap recipe) {
            packet.writeInt(recipe.biomeToMana.size());
            for (Entry<ResourceLocation, Set<Mana>> entry : recipe.biomeToMana.entrySet()) {
                packet.writeString(entry.getKey().toString());
                packet.writeByte(entry.getValue().size());
                for (Mana mana : entry.getValue()) {
                    packet.writeByte(mana.ordinal());
                }
            }
        }
    }

    public static class Inventory extends net.minecraft.inventory.Inventory {

    }

    public static final Type TYPE = new Type();
    public static final Serializer SERIALIZER = new Serializer();

    public static Set<Mana> getManaForBiome(World world, ResourceLocation biome) {
        for (RecipeManaTap recipe : world.getRecipeManager().getRecipesForType(RecipeManaTap.TYPE)) {
            if (recipe.biomeToMana.containsKey(biome)) {
                return recipe.biomeToMana.get(biome);
            }
        }
        return new HashSet<>();
    }

    public static Set<Mana> getManaForBiome(World world, Biome biome) {
        return getManaForBiome(world, biome.getRegistryName());
    }

    public static Set<Mana> getManaForBiome(World world, BlockPos pos) {
        return getManaForBiome(world, world.getBiome(pos));
    }
}
