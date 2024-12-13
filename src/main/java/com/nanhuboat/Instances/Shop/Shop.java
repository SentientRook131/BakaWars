package com.nanhuboat.Instances.Shop;

import com.nanhuboat.Templates.Shop.TemplateShop;
import org.bukkit.Location;

import java.util.List;

public class Shop extends TemplateShop {
    public List<Column> columns;
    public Location location;
    public Shop(List<Column> columns, Location location) {
        this.columns = columns;
        this.location = location;
    }
}
