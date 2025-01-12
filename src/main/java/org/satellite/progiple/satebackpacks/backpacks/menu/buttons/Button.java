package org.satellite.progiple.satebackpacks.backpacks.menu.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

public interface Button {
    boolean onClick(Player player, ClickType clickType, Inventory inventory);
}
