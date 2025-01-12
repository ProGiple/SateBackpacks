package org.satellite.progiple.satebackpacks.backpacks.events;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.Menus.MenuManager;
import org.satellite.progiple.satebackpacks.SateBackpacks;
import org.satellite.progiple.satebackpacks.backpacks.menu.BMenu;
import org.satellite.progiple.satebackpacks.other.configs.Config;
import org.satellite.progiple.satebackpacks.other.configs.Data;

import java.util.UUID;

public class OpenEvent implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItem();
        if (item == null || item.getType() == Material.AIR) return;

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            ReadableNBT readableNBT = NBT.readNbt(item);
            if (!readableNBT.hasTag("backpack-id")) return;

            if (item.getAmount() > 1 && Config.getBool("config.disableStacking")) {
                item.setAmount(item.getAmount() - 1);
                ItemStack newItem = item.clone();
                newItem.setAmount(1);
                player.getInventory().addItem(newItem);
                item = newItem;
            }

            String id = NBT.get(item, nbt -> (String) nbt.getString("backpack-id"));
            String uuid = UUID.randomUUID().toString();
            if (readableNBT.hasTag("backpack-uuid"))
                uuid = NBT.get(item, nbt -> (String) nbt.getString("backpack-uuid"));
            else {
                String finalUuid = uuid;
                NBT.modify(item, nbt -> {
                    nbt.setString("backpack-uuid", finalUuid);
                });
            }

            Data data = SateBackpacks.getDataMap().containsKey(uuid) ? SateBackpacks.getDataMap().get(uuid) : new Data(uuid);
            MenuManager.openInventory(player, new BMenu(player,
                    SateBackpacks.getBPCfgs().get(id).getSection("menu"), data));
        }
    }
}
