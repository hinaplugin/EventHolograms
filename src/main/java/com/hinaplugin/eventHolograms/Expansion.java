package com.hinaplugin.eventHolograms;

import com.google.common.collect.Lists;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ScheduledEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class Expansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "events";
    }

    @Override
    public @NotNull String getAuthor() {
        return "hina2113";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.20.4";
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params){
        if (params.equalsIgnoreCase("list")){
            final String guildId = EventHolograms.config.getString("server-id");
            if (guildId == null){
                return "&cDiscordサーバーのIDを設定してください．";
            }

            final Guild guild = EventHolograms.plugin.getJda().getGuildById(guildId);
            if (guild == null){
                return "&cDiscordサーバーを取得できませんでした．";
            }

            final List<ScheduledEvent> events = guild.getScheduledEvents();
            final List<String> eventsName = Lists.newArrayList();
            final List<String> filter = EventHolograms.config.getStringList("filter");
            if (!EventHolograms.config.getString("title", "").isEmpty()){
                eventsName.add(EventHolograms.config.getString("title", ""));
            }

            final String eventColor = EventHolograms.config.getString("event-color", "&a&l");
            final String startColor = EventHolograms.config.getString("start-color", "&d&l");

            if (events.isEmpty()){
                eventsName.add(eventColor + EventHolograms.config.getString("no-events", ""));
            }else {
                final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd");
                for (final ScheduledEvent event : events){
                    if (event.getStatus().equals(ScheduledEvent.Status.COMPLETED)){
                        continue;
                    }

                    final String dayOfWeek = event.getStartTime().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.JAPANESE);
                    final String color = event.getStatus().equals(ScheduledEvent.Status.ACTIVE) ? startColor : eventColor;

                    if (filter.isEmpty()) {
                        eventsName.add(color + event.getStartTime().format(dateTimeFormatter) + " (" + dayOfWeek + ") " + event.getName());
                    }else {
                        for (final String key : filter){
                            if (event.getName().startsWith(key)){
                                eventsName.add(color + event.getStartTime().format(dateTimeFormatter) + " (" + dayOfWeek + ") " + event.getName());
                            }
                        }
                    }
                }
            }

            if (!EventHolograms.config.getString("footer", "").isEmpty()){
                eventsName.add(EventHolograms.config.getString("footer", ""));
            }
            return String.join("\n", eventsName);
        }
        return "&c不明なパラメーターです．";
    }
}
