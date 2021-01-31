package info.iconmaster.minethecrafting.models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelLoader;

public class CardLoader implements IModelLoader<CardModel> {
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public CardModel read(JsonDeserializationContext jsonCtx, JsonObject json) {
        return new CardModel(new ResourceLocation(json.get("art").getAsString()));
    }
}
