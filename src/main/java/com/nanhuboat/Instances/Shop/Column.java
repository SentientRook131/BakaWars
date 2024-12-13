package com.nanhuboat.Instances.Shop;

import com.nanhuboat.Templates.Shop.ItemIcon;
import com.nanhuboat.Templates.Shop.TemplateColumn;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public class Column extends TemplateColumn {
    public List<Commodity> commodities;
    public Column(List<Commodity> commodities, ItemStack itemStack, String name, String description) {
        super();
        this.columnName = name;
        this.columnDescription = description;
        this.icon = new ItemIcon(itemStack.getType(), Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName(), String.join("\n", Objects.requireNonNull(itemStack.getItemMeta().getLore())), false);
        this.commodities = commodities;
    }
}
