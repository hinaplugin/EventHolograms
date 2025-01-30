package com.hinaplugin.eventHolograms;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class EventHolograms extends JavaPlugin {
    public static EventHolograms plugin;
    public static FileConfiguration config;
    private JDA jda;
    private Expansion expansion;

    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
            plugin = this;

            this.loadConfiguration();

            config = this.getConfig();

            final String token = config.getString("token", null);
            if (token == null){
                this.getServer().getPluginManager().disablePlugin(this);
                return;
            }

            jda = JDABuilder.createDefault(token)
                    .setAutoReconnect(true)
                    .setEnableShutdownHook(false)
                    .enableCache(CacheFlag.SCHEDULED_EVENTS)
                    .enableIntents(GatewayIntent.SCHEDULED_EVENTS)
                    .build()
                    .awaitReady();

            expansion = new Expansion();
            expansion.register();

            final PluginCommand command = this.getCommand("eventreload");
            if (command != null){
                command.setExecutor(new Commands());
            }
        }catch (Exception exception){
            this.getLogger().severe(exception.getMessage());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (expansion != null){
            expansion.unregister();
        }
        if (jda != null){
            jda.shutdown();
            try {
                jda.awaitShutdown();
            }catch (Exception ignored){
            }
        }
    }

    private void loadConfiguration(){
        final File configFile = new File(this.getDataFolder(), "config.xml");
        if (!configFile.exists()){
            this.saveDefaultConfig();
        }
    }

    public JDA getJda(){ return jda; }
}
