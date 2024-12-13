package com.nanhuboat.Instances.Shop;

import com.nanhuboat.Templates.Shop.ItemIcon;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ShopInventory implements Listener {
    public Shop shop;
    public Inventory inventory;
    public int chosenColumn = -1;
    public ShopInventory(Shop shop) {
        this.shop = shop;
    }
    public void show(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, shop.shopName);
        for (int i = 0;i < shop.columns.size();i++) {
            Column column = shop.columns.get(i);
            ItemIcon icon = column.icon;
            ItemStack stack = new ItemStack(icon.material);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(icon.name);
            meta.setLore(List.of(icon.description.split("\n")));
            stack.setItemMeta(meta);
            inventory.setItem(i, stack);
        }
        for (int i = 9;i < 18;i++) {
            inventory.setItem(i, new ItemStack(Material.RED_STAINED_GLASS_PANE));
        }
        this.inventory = inventory;
        player.openInventory(inventory);
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().equals(inventory)) {
            int slot = e.getSlot();
            if (slot < 9) {
                chosenColumn = slot;
            } else if (slot >= 18) {
                if (chosenColumn != -1) {
                    Column column = shop.columns.get(chosenColumn);
                    Commodity commodity = column.commodities.get(slot - 18);
                    commodity.buy((Player) e.getWhoClicked());
                }
            }
        }
    }
}
