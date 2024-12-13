package com.nanhuboat.Templates.Shop;

import com.google.gson.JsonObject;
import com.nanhuboat.Templates.Preservable;
import org.bukkit.Material;

public class ItemIcon implements Preservable {
    public Material material;
    public String name;
    public String description;
    public boolean isEnchanted;
    public ItemIcon() { }
    public ItemIcon(Material material, String name, String description, boolean isEnchanted) {
        this.material = material;
        this.name = name;
        this.description = description;
        this.isEnchanted = isEnchanted;
    }

    @Override
    public Preservable read(JsonObject map) {
        ItemIcon obj = new ItemIcon();
        material = obj.material = Material.valueOf(map.get("material").getAsString());
        name = obj.name = map.get("name").getAsString();
        description = obj.description = map.get("description").getAsString();
        isEnchanted = obj.isEnchanted = map.get("isEnchanted").getAsBoolean();
        return obj;
    }

    @Override
    public JsonObject write() {
        JsonObject map = new JsonObject();
        map.addProperty("material", material.name());
        map.addProperty("name", name);
        map.addProperty("description", description);
        map.addProperty("isEnchanted", isEnchanted);
        return map;
    }
}
