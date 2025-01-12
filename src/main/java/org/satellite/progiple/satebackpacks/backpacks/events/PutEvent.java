package org.satellite.progiple.satebackpacks.backpacks.events;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.satellite.progiple.satebackpacks.other.configs.Config;

public class PutEvent implements Listener {
    @EventHandler
    public void onPut(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;

        if (Config.getList("config.disabledStorage").contains(e.getInventory().getType().name())) {
            ReadableNBT readableNBT = NBT.readNbt(item);
            if (!readableNBT.hasTag("backpack-id")) return;

            e.getWhoClicked().sendMessage(Config.getMessages().get("storageIsLocked"));
            e.setCancelled(true);
        }
    }
}
