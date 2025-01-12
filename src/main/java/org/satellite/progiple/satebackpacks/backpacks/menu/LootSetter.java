package org.satellite.progiple.satebackpacks.backpacks.menu;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class LootSetter {
    private final Map<Byte, ItemStack> itemStackMap = new HashMap<>();
    public LootSetter(ConfigurationSection section) {
        if (section == null) return;
        for (String key : section.getKeys(false)) {
            ItemStack item = section.getItemStack(key);
            if (item == null) continue;
            this.itemStackMap.put(Byte.parseByte(key), item);
        }
    }

    public void insert(Inventory inventory) {
        this.itemStackMap.forEach(inventory::setItem);
    }
}
