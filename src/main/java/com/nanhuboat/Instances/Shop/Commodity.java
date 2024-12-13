package com.nanhuboat.Instances.Shop;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class Commodity {
    public ItemStack goods;
    public Consumer<Player> buy;
    public Commodity(ItemStack goods, Consumer<Player> buy) {
        this.goods = goods;
        this.buy = buy;
    }
    public void buy(Player player) { buy.accept(player); }
}
