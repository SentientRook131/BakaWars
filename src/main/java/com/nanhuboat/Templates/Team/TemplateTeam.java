package com.nanhuboat.Templates.Team;

import com.google.gson.JsonObject;
import com.nanhuboat.Templates.Preservable;
import org.bukkit.ChatColor;

public class TemplateTeam implements Preservable {
    public int maxPlayerAmount = 4;
    public String teamDisplayName = "南湖船";
    public String teamName = "NanHuBoat";
    public ChatColor color = ChatColor.RED;
    public TemplateTeam() { }
    public TemplateTeam(String teamDisplayName, String teamName, ChatColor color, int maxPlayerAmount) {
        this.teamDisplayName = teamDisplayName;
        this.teamName = teamName;
        this.color = color;
        this.maxPlayerAmount = maxPlayerAmount;
    }

    @Override
    public Preservable read(JsonObject map) {
        TemplateTeam obj = new TemplateTeam();
        teamDisplayName = obj.teamDisplayName = map.get("teamDisplayName").getAsString();
        teamName = obj.teamName = map.get("teamName").getAsString();
        color = obj.color = ChatColor.valueOf(map.get("color").getAsString());
        maxPlayerAmount = obj.maxPlayerAmount = map.get("maxPlayerAmount").getAsInt();
        return obj;
    }

    @Override
    public JsonObject write() {
        JsonObject map = new JsonObject();
        map.addProperty("teamDisplayName", teamDisplayName);
        map.addProperty("teamName", teamName);
        map.addProperty("color", color.name());
        map.addProperty("maxPlayerAmount", maxPlayerAmount);
        return map;
    }
}
