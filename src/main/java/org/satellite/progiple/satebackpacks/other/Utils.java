package org.satellite.progiple.satebackpacks.other;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.satellite.progiple.satebackpacks.SateBackpacks;
import org.satellite.progiple.satebackpacks.other.configs.BackPackConfig;

import java.io.File;
import java.util.Objects;

@UtilityClass
public class Utils {
    public String color(String text) {
        return ChatColor.translateAlternateColorCodes('&' ,text);
    }

    public void loadBackPackConfigs() {
        File dir = new File(SateBackpacks.getPlugin().getDataFolder(), "backpacks");
        if (dir.exists() && dir.isDirectory()) {
            SateBackpacks.getBPCfgs().forEach((id, cfg) -> cfg.unloadRecipe());
            SateBackpacks.getBPCfgs().clear();
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                String name = file.getName();
                if (file.exists() && !file.isDirectory() && name.contains(".yml")) {
                    String patch = name.replace(".yml", "");
                    new BackPackConfig(patch).loadRecipe();
                }
            }
        }
    }
}
