package org.satellite.progiple.satebackpacks.backpacks;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.satellite.progiple.satebackpacks.SateBackpacks;
import org.satellite.progiple.satebackpacks.other.Utils;
import org.satellite.progiple.satebackpacks.other.configs.BackPackConfig;

import java.util.List;
import java.util.UUID;

public class BackPack {
    private final String id;
    @Getter private final ItemStack item;

    @SuppressWarnings("deprecation")
    public BackPack(String id) {
        this.id = id;
        BackPackConfig config = SateBackpacks.getBPCfgs().get(this.id);
        ConfigurationSection itemSection = config.getSection("item");

        Material material = Material.STONE;
        String stringMaterial = itemSection.getString("material");
        if (stringMaterial != null && !stringMaterial.isEmpty()) material = Material.getMaterial(stringMaterial);

        List<String> lore = itemSection.getStringList("lore");
        lore.replaceAll(Utils::color);
        assert material != null;
        this.item = new ItemStack(material, 1);

        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(Utils.color(itemSection.getString("name")));
        meta.setLore(lore);
        this.item.setItemMeta(meta);

        if (material == Material.PLAYER_HEAD && itemSection.getKeys(false).contains("head")) {
            NBT.modify(this.item, nbt -> {
                nbt.setString("backpack-id", this.id);

                ReadWriteNBT skullOwnerCompound = nbt.getOrCreateCompound("SkullOwner");
                skullOwnerCompound.setUUID("Id", UUID.randomUUID());
                skullOwnerCompound.getOrCreateCompound("Properties")
                        .getCompoundList("textures")
                        .addCompound()
                        .setString("Value", itemSection.getString("head"));
            });
        }
    }

    public void give(Player player) {
        player.getInventory().addItem(this.item);
    }
}
