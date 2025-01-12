package org.satellite.progiple.satebackpacks.backpacks.menu;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.novasparkle.lunaspring.Menus.Items.Item;
import org.satellite.progiple.satebackpacks.backpacks.menu.buttons.BuyButton;
import org.satellite.progiple.satebackpacks.backpacks.menu.buttons.ClosedButton;
import org.satellite.progiple.satebackpacks.other.configs.Config;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ButtonSetter {
    private final List<ClosedButton> itemList = new ArrayList<>();
    private BuyButton buyButton;

    public ButtonSetter(byte rows, byte starterSlot, byte openedSlots) {
        ConfigurationSection closedButtonSection = Config.getSection("items.CLOSED_SLOT");
        byte leftSlots = openedSlots;
        byte slots = (byte) (rows * 9);
        if (starterSlot > slots) return;

        for (byte i = starterSlot; i < slots; i++) {
            if (leftSlots > 0) {
                leftSlots--;
                continue;
            }

            if (this.buyButton == null) {
                this.buyButton = new BuyButton(i, slots);
                continue;
            }

            this.itemList.add(new ClosedButton(closedButtonSection, i));
        }
    }

    public void insert(Inventory inventory) {
        this.itemList.forEach(item -> item.insert(inventory));
        if (this.buyButton != null) this.buyButton.insert(inventory);
    }
}
