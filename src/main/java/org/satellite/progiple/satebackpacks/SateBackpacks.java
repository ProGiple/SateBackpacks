package org.satellite.progiple.satebackpacks;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.novasparkle.lunaspring.Events.MenuHandler;
import org.satellite.progiple.satebackpacks.backpacks.events.OpenEvent;
import org.satellite.progiple.satebackpacks.backpacks.events.PlaceEvent;
import org.satellite.progiple.satebackpacks.backpacks.events.PutEvent;
import org.satellite.progiple.satebackpacks.other.Utils;
import org.satellite.progiple.satebackpacks.other.Vault;
import org.satellite.progiple.satebackpacks.other.configs.BackPackConfig;
import org.satellite.progiple.satebackpacks.other.configs.Config;
import org.satellite.progiple.satebackpacks.other.configs.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class SateBackpacks extends JavaPlugin {
    @Getter private static SateBackpacks plugin;
    @Getter private static Map<String, BackPackConfig> BPCfgs = new HashMap<>();
    @Getter private static Map<String, Data> dataMap = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;
        plugin.saveDefaultConfig();

        if (Config.getBool("config.loadExample")) plugin.saveResource("backpacks/example_backpack.yml", false);
        Utils.loadBackPackConfigs();

        Command command = new Command();
        Objects.requireNonNull(plugin.getCommand("satebackpacks")).setExecutor(command);
        Objects.requireNonNull(plugin.getCommand("satebackpacks")).setTabCompleter(command);
        if (!Vault.setupEconomy()) {
            System.out.println("Плагин не может работать без Vault!");
            Bukkit.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        this.reg(new MenuHandler());
        this.reg(new OpenEvent());
        this.reg(new PlaceEvent());
        this.reg(new PutEvent());
    }

    @Override
    public void onDisable() {
    }

    private void reg(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
    }
}
