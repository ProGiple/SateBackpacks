package org.satellite.progiple.satebackpacks.backpacks.menu;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.Menus.AMenu;
import org.satellite.progiple.satebackpacks.backpacks.menu.buttons.ClosedButton;
import org.satellite.progiple.satebackpacks.other.configs.Config;
import org.satellite.progiple.satebackpacks.other.configs.Data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class BMenu extends AMenu {
    private final ButtonSetter buttonSetter;
    private final Data data;
    public BMenu(Player player, ConfigurationSection section, Data data) {
        super(player, Objects.requireNonNull(section.getString("title")), (byte) (section.getInt("rows") * 9));
        this.data = data;
        this.buttonSetter = new ButtonSetter((byte) (section.getInt("rows")),
                (byte) section.getInt("starterSlot"), this.data.getOpenedSlots());
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        this.buttonSetter.insert(this.getInventory());
        new LootSetter(this.data.getItems()).insert(this.getInventory());
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        ItemStack itemStack = e.getCurrentItem();
        if (itemStack == null || itemStack.getType() == Material.AIR) return;

        ReadableNBT readableNBT = NBT.readNbt(itemStack);
        if (readableNBT.hasTag("backpack-id")) {
            e.getWhoClicked().sendMessage(Config.getMessages().get("storageIsLocked"));
            e.setCancelled(true);
            return;
        }

        if (Config.getList("config.disabledItems").contains(itemStack.getType().name())) {
            e.setCancelled(true);
            this.getPlayer().sendMessage(Config.getMessages().get("itemIsLocked"));
            return;
        }

        if (this.buttonSetter.getBuyButton() != null && itemStack.equals(this.buttonSetter.getBuyButton().getItemStack())) {
            e.setCancelled(true);
            if (this.buttonSetter.getBuyButton().onClick(this.getPlayer(), e.getClick(), this.getInventory())) {
                this.data.addSlot();
                for (ClosedButton item : this.buttonSetter.getItemList()) {
                    if (item.getSlot() == this.buttonSetter.getBuyButton().getSlot()) {
                        this.buttonSetter.getItemList().remove(item);
                        break;
                    }
                }
            }
            return;
        }

        for (ClosedButton item : this.buttonSetter.getItemList()) {
            if (item.getItemStack().equals(itemStack)) {
                e.setCancelled(true);
                item.onClick(this.getPlayer(), e.getClick(), this.getInventory());
                break;
            }
        }
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        Set<Byte> bytes = new HashSet<>();
        this.buttonSetter.getItemList().forEach(item -> bytes.add(item.getSlot()));
        if (this.buttonSetter.getBuyButton() != null) bytes.add(this.buttonSetter.getBuyButton().getSlot());

        for (byte i = 0; i < e.getInventory().getSize(); i++) {
            ItemStack item = e.getInventory().getItem(i);
            if (item == null || item.getType() == Material.AIR || bytes.contains(i)) {
                this.data.setItem(i, null);
                continue;
            }

            this.data.setItem(i, item);
        }
        this.data.save();
    }
}
