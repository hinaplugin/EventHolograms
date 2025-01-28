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

            final List<String> eventsName = Lists.newArrayList();
            final List<String> filter = EventHolograms.config.getStringList("filter").isEmpty() ? null : EventHolograms.config.getStringList("filter");
            if (!EventHolograms.config.getString("title", "").isEmpty()){
                eventsName.add(EventHolograms.config.getString("title", ""));
            }

            final String eventColor = EventHolograms.config.getString("event-color", "&l&a");
            final String startColor = EventHolograms.config.getString("start-color", "&l&d");

            if (events.isEmpty()){
                eventsName.add(eventColor + "現在登録されているイベントはありません．");
            }else {
                for (final ScheduledEvent event : events){
                    if (filter == null) {
                        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd");
                        String dayOfWeek = event.getStartTime().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.JAPANESE);
                        if (event.getStatus().equals(ScheduledEvent.Status.ACTIVE)){
                            eventsName.add(startColor + event.getStartTime().format(dateTimeFormatter) + " (" + dayOfWeek + ") " + event.getName());
                        }else {
                            eventsName.add(eventColor + event.getStartTime().format(dateTimeFormatter) + " (" + dayOfWeek + ") " + event.getName());
                        }
                    }else {
                        for (final String key : filter){
                            if (event.getName().startsWith(key)){
                                final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd");
                                String dayOfWeek = event.getStartTime().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.JAPANESE);
                                if (event.getStatus().equals(ScheduledEvent.Status.ACTIVE)){
                                    eventsName.add(startColor + event.getStartTime().format(dateTimeFormatter) + " (" + dayOfWeek + ") " + event.getName());
                                }else {
                                    eventsName.add(eventColor + event.getStartTime().format(dateTimeFormatter) + " (" + dayOfWeek + ") " + event.getName());
                                }
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
        return null;
    }
}
