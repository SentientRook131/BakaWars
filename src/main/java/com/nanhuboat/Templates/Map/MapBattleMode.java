package com.nanhuboat.Templates.Map;

import com.google.gson.JsonObject;
import com.nanhuboat.Templates.Preservable;

import java.util.HashSet;
import java.util.Objects;

public class MapBattleMode implements Preservable {
    public String name;
    public int teamPlayersAmount;
    public int teamsAmount;
    public MapBattleMode() { }
    public MapBattleMode(String name, int teamPlayersAmount, int teamsAmount) {
        this.name = name;
        this.teamPlayersAmount = teamPlayersAmount;
        this.teamsAmount = teamsAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MapBattleMode that)) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    public static MapBattleMode v1_2 = new MapBattleMode("1v1", 1, 2);
    public static MapBattleMode v2_8 = new MapBattleMode("2v2v2v2v2v2v2v2", 2, 8);
    public static MapBattleMode v3_4 = new MapBattleMode("3v3v3v3", 3, 4);
    public static MapBattleMode v4_2 = new MapBattleMode("4v4", 4, 2);
    public static MapBattleMode v4_4 = new MapBattleMode("4v4v4v4", 4, 4);
    public static MapBattleMode v8_4 = new MapBattleMode("8v8v8v8", 8, 4);
    public static MapBattleMode v16_4 = new MapBattleMode("16v16v16v16", 16, 2);
    public static MapBattleMode v1_64 = new MapBattleMode("1v_64", 1, 64);
    public static MapBattleMode v2_32 = new MapBattleMode("2v_32", 2, 32);
    public static MapBattleMode v32_2 = new MapBattleMode("32v32", 32, 2);
    public static HashSet<MapBattleMode> allMapBattleModes = new HashSet<>() {{
        add(v1_2);
        add(v2_8);
        add(v3_4);
        add(v4_2);
        add(v4_4);
        add(v8_4);
        add(v16_4);
        add(v1_64);
        add(v2_32);
        add(v32_2);
    }};
    public static HashSet<MapBattleMode> all() { return allMapBattleModes; }
    public static void add(MapBattleMode mapBattleMode) { allMapBattleModes.add(mapBattleMode); }
    public static MapBattleMode valueOf(String name) {
        for (MapBattleMode mapBattleMode : allMapBattleModes) {
            if (mapBattleMode.name.equals(name)) {
                return mapBattleMode;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return write().toString();
    }

    @Override
    public Preservable read(JsonObject map) {
        MapBattleMode obj = new MapBattleMode();
        name = obj.name = map.get("name").getAsString();
        teamPlayersAmount = obj.teamPlayersAmount = map.get("teamPlayersAmount").getAsInt();
        return obj;
    }

    @Override
    public JsonObject write() {
        JsonObject map = new JsonObject();
        map.addProperty("name", name);
        map.addProperty("teamPlayersAmount", teamPlayersAmount);
        return map;
    }
}
