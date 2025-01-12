package org.satellite.progiple.satebackpacks.backpacks.menu.buttons;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.novasparkle.lunaspring.Menus.Items.Item;

public class ClosedButton extends Item implements Button {
    public ClosedButton(ConfigurationSection section, int slot) {
        super(section, slot);
        this.setAmount(1);
    }

    @Override
    public boolean onClick(Player player, ClickType clickType, Inventory inventory) {
        return false;
    }
}
