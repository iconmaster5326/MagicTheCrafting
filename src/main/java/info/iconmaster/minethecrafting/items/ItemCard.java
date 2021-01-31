package info.iconmaster.minethecrafting.items;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import info.iconmaster.minethecrafting.Mana;
import info.iconmaster.minethecrafting.MineTheCrafting;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemCard extends Item {
    public final Mana color;
    public final List<Mana> manaCost;
    public static final Set<ItemCard> ALL_CARDS = new HashSet<>();

    public ItemCard(Mana color, List<Mana> manaCost) {
        super(new Item.Properties().group(ItemGroup.MISC));
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
}
