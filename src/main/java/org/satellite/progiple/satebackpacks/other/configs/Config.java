package org.satellite.progiple.satebackpacks.other.configs;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.Configuration.IConfig;
import org.satellite.progiple.satebackpacks.SateBackpacks;
import org.satellite.progiple.satebackpacks.other.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class Config {
    @Getter private final Map<String, String> messages = new HashMap<>();
    private final IConfig config;
    static {
        config = new IConfig(SateBackpacks.getPlugin());
        Config.loadMessages();
    }

    public void reload() {
        config.reload(SateBackpacks.getPlugin());
    }

    public String getStr(String path) {
        return config.getString(path);
    }

    public boolean getBool(String path) {
        return config.getBoolean(path);
    }

    public List<String> getList(String path) {
        return config.getStringList(path);
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public ConfigurationSection getSection(String path) {
        return config.getSection(path);
    }

    public void loadMessages() {
        Config.getMessages().clear();
        ConfigurationSection section = Config.getSection("messages");
        for (String key : section.getKeys(false)) {
            String message = section.getString(key);
            Config.getMessages().put(key, Utils.color(message));
        }
    }
}
