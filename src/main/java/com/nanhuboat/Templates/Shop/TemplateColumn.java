package com.nanhuboat.Templates.Shop;

import com.google.gson.JsonObject;
import com.nanhuboat.Templates.Preservable;

public class TemplateColumn implements Preservable {
    public String columnName;
    public String columnDescription;
    public ItemIcon icon;
    public TemplateColumn() { }
    public TemplateColumn(String columnName, String columnDescription, ItemIcon icon) {
        this.columnName = columnName;
        this.columnDescription = columnDescription;
        this.icon = icon;
    }
    @Override
    public Preservable read(JsonObject map) {
        TemplateColumn obj = new TemplateColumn();
        columnName = obj.columnName = map.get("columnName").getAsString();
        columnDescription = obj.columnDescription = map.get("columnDescription").getAsString();
        icon = obj.icon = (ItemIcon) new ItemIcon().read(map.get("icon").getAsJsonObject());
        return obj;
    }

    @Override
    public JsonObject write() {
        return null;
    }
}
