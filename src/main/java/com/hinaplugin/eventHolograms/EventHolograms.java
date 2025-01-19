package com.hinaplugin.eventHolograms;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class EventHolograms extends JavaPlugin {
    public static EventHolograms plugin;
    public static FileConfiguration config;
    private JDA jda;

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
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .setAutoReconnect(true)
                    .setEnableShutdownHook(false)
                    .enableCache(CacheFlag.MEMBER_OVERRIDES)
                    .enableIntents(GatewayIntent.GUILD_MESSAGES)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                    .build()
                    .awaitReady();

            new Expansion().register();
        }catch (Exception exception){
            this.getLogger().severe(exception.getMessage());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (jda != null){
            jda.shutdownNow();
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
