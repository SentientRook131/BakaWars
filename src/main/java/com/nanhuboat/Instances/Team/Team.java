package com.nanhuboat.Instances.Team;

import com.nanhuboat.Instances.ResourcesSpawn.ResourcesSpawnPoint;
import com.nanhuboat.Instances.Shop.Shop;
import com.nanhuboat.Instances.Shop.ShopVillager;
import com.nanhuboat.Templates.Team.TemplateTeam;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Team extends TemplateTeam {
    public Location bedLocation;
    public Location respawnLocation;
    public Material symbolBed;
    public Material symbolWool;
    public List<ResourcesSpawnPoint> points;
    public List<Shop> shop;
    public List<ShopVillager> villagers;
    public List<Player> players;
    public Team(String name, Location bedLocation, Location respawnLocation, Material symbolBed, Material symbolWool, List<ResourcesSpawnPoint> points, List<Shop> shop) {
        super();
        this.teamDisplayName = name;
        this.teamName = name;
        this.bedLocation = bedLocation;
        this.respawnLocation = respawnLocation;
        this.symbolBed = symbolBed;
        this.symbolWool = symbolWool;
        this.points = points;
        this.shop = shop;
        this.players = new ArrayList<>();
    }
    public void addPlayer(Player player) {
        players.add(player);
    }
    public void removePlayer(Player player) {
        players.remove(player);
    }
    public void start() {
        for (Player player : players) player.teleport(respawnLocation);
        bedLocation.getWorld().setBlockData(bedLocation, Material.RED_BED.createBlockData());
        for (Shop shop : shop) {
            ShopVillager villager = new ShopVillager(shop);
            villager.init();
            villagers.add(villager);
        }
        for (ResourcesSpawnPoint point : points) {
            point.init();
        }
    }
}
