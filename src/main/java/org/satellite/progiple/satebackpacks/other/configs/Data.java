package org.satellite.progiple.satebackpacks.other.configs;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.Configuration.Configuration;
import org.satellite.progiple.satebackpacks.SateBackpacks;

import java.io.File;

public class Data {
    private final Configuration config;
    public Data(String uuid) {
        this.config = new Configuration(new File(SateBackpacks.getPlugin().getDataFolder(), String.format("data/%s.yml", uuid)));
        SateBackpacks.getDataMap().put(uuid, this);
    }

    public ConfigurationSection getItems() {
        return this.config.getSection("items");
    }

    public void setItem(int slot, ItemStack item) {
        this.config.setItemStack(String.format("items.%s", slot), item);
    }

    public byte getOpenedSlots() {
        return (byte) this.config.getInt("openedSlots");
    }

    public void addSlot() {
        this.config.setInt("openedSlots", this.getOpenedSlots() + 1);
        this.save();
    }

    public void save() {
        this.config.save();
    }
}
