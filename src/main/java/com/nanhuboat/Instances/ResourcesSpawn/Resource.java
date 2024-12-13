package com.nanhuboat.Instances.ResourcesSpawn;

import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

public class Resource implements Serializable {
    public ItemStack item;
    public int amount;
    public int rate;
    public Resource(ItemStack item, int amount, int rate) {
        this.item = item;
        this.amount = amount;
        this.rate = rate;
    }
}
