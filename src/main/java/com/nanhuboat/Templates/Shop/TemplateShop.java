package com.nanhuboat.Templates.Shop;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nanhuboat.Templates.Preservable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TemplateShop implements Preservable {
    public String shopName;
    public String shopType;
    public List<TemplateColumn> columns;
    public TemplateShop() { }
    public TemplateShop(String shopName, String shopType, List<TemplateColumn> columns) {
        this.shopName = shopName;
        this.shopType = shopType;
        this.columns = columns;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TemplateShop that)) return false;
        return Objects.equals(shopName, that.shopName) && Objects.equals(shopType, that.shopType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shopName, shopType);
    }

    @Override
    public Preservable read(JsonObject map) {
        TemplateShop shop = new TemplateShop();
        shopName = shop.shopName = map.get("shopName").getAsString();
        shopType = shop.shopType = map.get("shopType").getAsString();
        columns = shop.columns = Stream.of(map.get("columns").getAsJsonArray())
                .map(e -> (TemplateColumn) new TemplateColumn().read(e.getAsJsonObject()))
                .collect(Collectors.toList());
        return shop;
    }

    @Override
    public JsonObject write() {
        JsonObject map = new JsonObject();
        map.addProperty("shopName", shopName);
        map.addProperty("shopType", shopType);
        map.add("columns", columns.stream().map(TemplateColumn::write).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
        return map;
    }
}
