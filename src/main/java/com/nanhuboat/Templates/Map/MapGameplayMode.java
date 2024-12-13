package com.nanhuboat.Templates.Map;

import com.google.gson.JsonObject;
import com.nanhuboat.Templates.Preservable;

import java.util.HashSet;
import java.util.Objects;

public class MapGameplayMode implements Preservable {
    public String name;
    public String description;
    public MapGameplayMode() { }
    public MapGameplayMode(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MapGameplayMode that)) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    public static MapGameplayMode normal = new MapGameplayMode("起床战争 - 普通模式", "普通模式");
    public static MapGameplayMode exp = new MapGameplayMode("起床战争 - 经验模式", "经验模式");
    public static HashSet<MapGameplayMode> allMapGameplayModes = new HashSet<>() {{
        add(normal);
        add(exp);
    }};
    public static HashSet<MapGameplayMode> all() { return allMapGameplayModes; }
    public static void add(MapGameplayMode mapGameplayMode) { allMapGameplayModes.add(mapGameplayMode); }
    public static MapGameplayMode valueOf(String name) {
        for (MapGameplayMode mapGameplayMode : allMapGameplayModes) {
            if (mapGameplayMode.name.equals(name)) {
                return mapGameplayMode;
            }
        }
        return null;
    }

    @Override
    public Preservable read(JsonObject map) {
        MapGameplayMode obj = new MapGameplayMode();
        name = obj.name = map.get("name").getAsString();
        description = obj.description = map.get("description").getAsString();
        return obj;
    }

    @Override
    public JsonObject write() {
        JsonObject map = new JsonObject();
        map.addProperty("name", name);
        map.addProperty("description", description);
        return map;
    }

    @Override
    public String toString() {
        return write().toString();
    }
}
