package com.nanhuboat.Templates.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nanhuboat.Templates.Preservable;
import com.nanhuboat.Templates.Team.TemplateTeam;

import java.util.Arrays;
import java.util.List;

public class TemplateMap implements Preservable {
    public String name;
    public String description;
    public String[] authors;
    public String version;
    public String date;
    public MapBattleMode battleMode;
    public MapGameplayMode gameplayMode;
    public List<TemplateTeam> teams;
    public TemplateMap() { }
    public TemplateMap(String name, String description, String[] authors, String version, String date, MapBattleMode battleMode, MapGameplayMode gameplayMode, List<TemplateTeam> teams) {
        this.name = name;
        this.description = description;
        this.authors = authors;
        this.version = version;
        this.date = date;
        this.battleMode = battleMode;
        this.gameplayMode = gameplayMode;
        this.teams = teams;
    }

    @Override
    public Preservable read(JsonObject map) {
        TemplateMap obj = new TemplateMap();
        name = obj.name = map.get("name").getAsString();
        description = obj.description = map.get("description").getAsString();
        authors = obj.authors = map.get("authors").getAsJsonArray().asList().stream().map(JsonElement::getAsString).toArray(String[]::new);
        version = obj.version = map.get("version").getAsString();
        date = obj.date = map.get("date").getAsString();
        battleMode = obj.battleMode = MapBattleMode.valueOf(map.get("battleMode").getAsString());
        gameplayMode = obj.gameplayMode = MapGameplayMode.valueOf(map.get("gameplayMode").getAsString());
        return obj;
    }

    @Override
    public JsonObject write() {
        JsonObject map = new JsonObject();
        map.addProperty("name", name);
        map.addProperty("description", description);
        map.add("authors", Arrays.stream(authors).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
        map.addProperty("version", version);
        map.addProperty("date", date);
        map.addProperty("battleMode", battleMode.toString());
        map.addProperty("gameplayMode", gameplayMode.toString());
        return map;
    }
}
