package com.nanhuboat.Instances.ResourcesSpawn;

import com.nanhuboat.Instances.Team.Team;
import com.nanhuboat.bakaWars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Serializable;
import java.util.List;

public class ResourcesSpawnPoint implements Serializable {
    public String name;
    public Location location;
    public Team owner;
    public List<Resource> resources;
    public ResourcesSpawnPoint(String name, Location location, Team owner, List<Resource> resources) {
        this.name = name;
        this.location = location;
        this.owner = owner;
        this.resources = resources;
    }
    public void init() {
        for (Resource resource : resources) {
            Bukkit.getServer().getScheduler().runTaskTimer(JavaPlugin.getPlugin(bakaWars.class), () -> {
                for (int i = 0;i < resource.amount;i++) location.getWorld().dropItem(location, resource.item);
            }, resource.rate, resource.rate);
        }
    }
}
