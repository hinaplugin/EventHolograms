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
                return null;
            }

            final Guild guild = EventHolograms.plugin.getJda().getGuildById(guildId);
            if (guild == null){
                return null;
            }

            final List<ScheduledEvent> events = guild.getScheduledEvents();
            if (events.isEmpty()){
                return null;
            }

            final List<String> eventsName = Lists.newArrayList();
            final String filter = EventHolograms.config.getString("filter", "").isEmpty() ? null : EventHolograms.config.getString("filter", "");
            boolean color = true;
            if (!EventHolograms.config.getString("title", "").isEmpty()){
                eventsName.add(EventHolograms.config.getString("title", ""));
            }
            for (final ScheduledEvent event : events){
                if (filter == null) {
                    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd");
                    String dayOfWeek = event.getStartTime().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.JAPANESE);
                    if (color){
                        eventsName.add("&l&a" + event.getStartTime().format(dateTimeFormatter) + " (" + dayOfWeek + ") " + event.getName().split("マイクラ ")[1]);
                        color = false;
                    }else {
                        eventsName.add(event.getStartTime().format(dateTimeFormatter) + " (" + dayOfWeek + ") " + event.getName().split("マイクラ ")[1]);
                    }
                }else {
                    if (event.getName().contains(filter)){
                        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd");
                        String dayOfWeek = event.getStartTime().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.JAPANESE);
                        if (color){
                            eventsName.add("&l&a" + event.getStartTime().format(dateTimeFormatter) + " (" + dayOfWeek + ") " + event.getName().split("マイクラ ")[1]);
                            color = false;
                        }else {
                            eventsName.add(event.getStartTime().format(dateTimeFormatter) + " (" + dayOfWeek + ") " + event.getName().split("マイクラ ")[1]);
                        }
                    }
                }
            }
            if (!EventHolograms.config.getString("footer", "").isEmpty()){
                eventsName.add(EventHolograms.config.getString("footer", ""));
            }
            return String.join("\n", eventsName);
        }
        return null;
    }
}
