package org.satellite.progiple.satebackpacks;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.satellite.progiple.satebackpacks.backpacks.BackPack;
import org.satellite.progiple.satebackpacks.other.Utils;
import org.satellite.progiple.satebackpacks.other.configs.Config;

import java.util.ArrayList;
import java.util.List;

public class Command implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length >= 1) {
            if (commandSender.hasPermission("satebackpacks.admin")) {
                if (strings[0].equals("give")) {
                    if (strings.length >= 3) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(strings[1]);
                        if (offlinePlayer != null && offlinePlayer.isOnline()) {
                            BackPack backPack = new BackPack(strings[2]);
                            backPack.give((Player) offlinePlayer);
                        }
                        else commandSender.sendMessage(Config.getMessages().get("playerIsOffline"));
                    }
                    else commandSender.sendMessage(Config.getMessages().get("noArgs"));
                }
                else {
                    Config.reload();
                    Config.loadMessages();
                    Utils.loadBackPackConfigs();
                    commandSender.sendMessage(Config.getMessages().get("reloadPlugin"));
                }
            }
            else commandSender.sendMessage(Config.getMessages().get("noPerm"));
        }
        else commandSender.sendMessage(Config.getMessages().get("noArgs"));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> list = new ArrayList<>();
        if (strings.length == 1) {
            list.add("reload");
            list.add("give");
        }
        else if (strings.length == 2 && strings[0].equals("give")) {
            Bukkit.getOnlinePlayers().forEach(player -> list.add(player.getName()));
        }
        else if (strings.length == 3 && strings[0].equals("give")) {
            SateBackpacks.getBPCfgs().forEach((id, cfg) -> list.add(id));
        }
        return list;
    }
}
