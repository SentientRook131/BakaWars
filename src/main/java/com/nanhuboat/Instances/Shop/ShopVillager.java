package com.nanhuboat.Instances.Shop;

import com.nanhuboat.bakaWars;
import org.bukkit.Bukkit;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class ShopVillager implements Listener {
    public ShopInventory inventory;
    public Villager hook;
    public ShopVillager(Shop shop) {
        this.inventory = new ShopInventory(shop);
    }
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, JavaPlugin.getPlugin(bakaWars.class));
        hook = Objects.requireNonNull(inventory.shop.location.getWorld()).spawn(inventory.shop.location, Villager.class);
        hook.setCustomName(inventory.shop.shopName);
        hook.setCustomNameVisible(true);
        hook.setInvulnerable(true);
        hook.setAI(false);
        hook.setCollidable(false);
        hook.setSilent(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().equals(hook)) {
            inventory.show(e.getPlayer());
        }
    }
}
