package com.nanhuboat.Instances.Game;

import com.nanhuboat.Instances.Team.Team;
import com.nanhuboat.bakaWars;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class ChooseTeamInterfaceListener implements Listener {
    public Inventory inventory;
    public Player player;
    public Game game;
    public ChooseTeamInterfaceListener(Player player, Game game) {
        Bukkit.getServer().getPluginManager().registerEvents(this, JavaPlugin.getPlugin(bakaWars.class));
        this.player = player;
        this.game = game;
        this.inventory = Bukkit.createInventory(null, 9, "选择队伍");
        for (Team team : game.teams) {
            if (team.equals(game.players.get(player).team)) continue;
            ItemStack stack = new ItemStack(team.symbolBed);
            ItemMeta meta = stack.getItemMeta();
            meta.setItemName(team.teamDisplayName);
            stack.setItemMeta(meta);
            inventory.addItem(stack);
        }
    }
    @EventHandler
    public void choose(InventoryClickEvent e) {
        if (e.getInventory().equals(inventory) && e.getCurrentItem() != null) {
            for (Team team : game.teams) {
                if (team.symbolBed.equals(e.getCurrentItem().getType())) {
                    game.playerJoinTheTeam(player, team);
                }
            }
        }
    }
}
