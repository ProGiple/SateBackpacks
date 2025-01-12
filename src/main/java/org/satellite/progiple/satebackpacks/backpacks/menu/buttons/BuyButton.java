package org.satellite.progiple.satebackpacks.backpacks.menu.buttons;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.novasparkle.lunaspring.Menus.Items.Item;
import org.satellite.progiple.satebackpacks.other.Utils;
import org.satellite.progiple.satebackpacks.other.Vault;
import org.satellite.progiple.satebackpacks.other.configs.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BuyButton extends Item implements Button {
    private boolean isActive = false;
    private final byte maxSlot;
    public BuyButton(int slot, byte maxSlot) {
        super(Config.getSection("items.BUY_SLOT"), slot);
        this.setAmount(1);
        this.maxSlot = maxSlot;
    }

    @Override
    public boolean onClick(Player player, ClickType clickType, Inventory inventory) {
        if (!this.isActive) this.reActive("SUCCESSFUL_SLOT", true, inventory);
        else {
            if (clickType.isRightClick()) this.reActive("BUY_SLOT", false, inventory);
            else if (clickType.isLeftClick()) {
                if (Vault.getEconomy().getBalance(player) >= Config.getInt("config.buySlotCost")) {
                    Vault.getEconomy().withdrawPlayer(player, Config.getInt("config.buySlotCost"));
                    this.reActive("BUY_SLOT", false, inventory);
                    byte slot = (byte) (this.getSlot() + 1);
                    this.remove(inventory);

                    if (slot < this.maxSlot) this.insert(inventory, slot);
                    player.sendMessage(Config.getMessages().get("buySlot"));
                    player.playSound(player.getLocation(), Sound.valueOf(Config.getStr("config.buySound")), 1, 1);
                    return true;
                }
                else {
                    player.sendMessage(Config.getMessages().get("noMoney"));
                    player.playSound(player.getLocation(), Sound.valueOf(Config.getStr("config.errorSound")), 1, 1);
                }
            }
        }
        return false;
    }

    public void reActive(String id, boolean newState, Inventory inventory) {
        this.isActive = newState;
        ConfigurationSection section = Config.getSection(String.format("items.%s", id));
        Material material = section.getKeys(false).contains("material") ?
                Material.getMaterial(Objects.requireNonNull(section.getString("material"))) : Material.STONE;

        List<String> lore = new ArrayList<>(section.getStringList("lore"));
        lore.replaceAll(Utils::color);
        this.setAll(material, 1, Utils.color(section.getString("displayName")), lore, false);
        this.insert(inventory);
    }
}
