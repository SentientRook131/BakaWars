package com.nanhuboat.Instances.Game;

import com.nanhuboat.bakaWars;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class OpenInterfaceToChooseTeamListener implements Listener {
    public Inventory inventory;
    public Game game;
    public OpenInterfaceToChooseTeamListener(Inventory inventory, Game game) {
        Bukkit.getServer().getPluginManager().registerEvents(this, JavaPlugin.getPlugin(bakaWars.class));
        this.inventory = inventory;
        this.game = game;
    }
    @EventHandler
    public void choose(InventoryClickEvent e) {
        if (e.getInventory().equals(inventory)) {
            if (e.getSlot() == 0) {
                new ChooseTeamInterfaceListener((Player) e.getWhoClicked(), game);
            }
        }
    }
}
