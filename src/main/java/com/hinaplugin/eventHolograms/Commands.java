package com.hinaplugin.eventHolograms;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender.hasPermission("eventholograms.commands.reload")){
            EventHolograms.plugin.reloadConfig();
            EventHolograms.config = EventHolograms.plugin.getConfig();
            commandSender.sendMessage(MiniMessage.miniMessage().deserialize("<green>config.ymlを再読み込みしました．"));
        }
        return true;
    }
}
