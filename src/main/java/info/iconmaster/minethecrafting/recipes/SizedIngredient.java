package info.iconmaster.minethecrafting.recipes;

import java.util.Arrays;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;

public class SizedIngredient extends Ingredient {
    public final Ingredient ingredient;
    public final int count;

    public SizedIngredient(Ingredient ingredient, int count) {
        super(Arrays.<IItemList>asList().stream());
        this.ingredient = ingredient;
        this.count = count;
    }

    @Override
    public boolean test(ItemStack stack) {
        return ingredient.test(stack) && stack.getCount() >= count;
    }

    @Override
    public ItemStack[] getMatchingStacks() {
        return ingredient.getMatchingStacks();
    }

    public static SizedIngredient read(PacketBuffer packet) {
        Ingredient ingredient = Ingredient.read(packet);
        int count = packet.readByte();
        return new SizedIngredient(ingredient, count);
    }

    public static SizedIngredient deserialize(JsonElement json) {
        Ingredient ingredient = Ingredient.deserialize(json);
        int count = 1;
        if (json.isJsonObject() && json.getAsJsonObject().get("count") != null) {
            count = json.getAsJsonObject().get("count").getAsInt();
        }
        return new SizedIngredient(ingredient, count);
    }

    @Override
    public JsonElement serialize() {
        JsonElement serialized = ingredient.serialize();
        if (count != 1) {
            if (serialized.isJsonObject()) {
                serialized.getAsJsonObject().addProperty("count", count);
            } else {
                JsonObject newJSON = new JsonObject();
                newJSON.add("item", serialized);
                newJSON.addProperty("count", count);
                serialized = newJSON;
            }
        }
        return serialized;
    }

    // @Override // WHY IS THE WRITE METHOD FINAL??
    public void writeSizedIngredient(PacketBuffer buffer) {
        ingredient.write(buffer);
        buffer.writeByte(count);
    }
}
