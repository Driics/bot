/*
 * Copyright (c) 2018.
 *
 * This file is part of Sable.
 *
 * Sable is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sable is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sable.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.sable.handlers.adapter;

import com.sable.Sable;
import com.sable.Constants;
import com.sable.contracts.handlers.EventAdapter;
import com.sable.database.collection.Collection;
import com.sable.database.controllers.GuildController;
import com.sable.database.controllers.ReactionController;
import com.sable.database.transformers.GuildTransformer;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class ChannelEventAdapter extends EventAdapter {

    private static final Logger log = LoggerFactory.getLogger(ChannelEventAdapter.class);

    /**
     * Instantiates the event adapter and sets the sable class instance.
     *
     * @param avaire The Sable application class instance.
     */
    public ChannelEventAdapter(Sable avaire) {
        super(avaire);
    }

    public void onTextChannelDelete(TextChannelDeleteEvent event) {
        handleTextChannelDeleteReactionsRoles(event);
        handleTextChannelDeleteGuildSettings(event);
    }

    private void handleTextChannelDeleteGuildSettings(TextChannelDeleteEvent event) {
        GuildTransformer transformer = GuildController.fetchGuild(sable, event.getGuild());
        if (transformer == null) {
            return;
        }

        if (transformer.getModlog() != null && transformer.getModlog().equalsIgnoreCase(event.getChannel().getId())) {
            setDatabaseColumnToNull(event.getGuild().getId(), "modlog");
        }

        if (transformer.getLevelChannel() != null && transformer.getLevelChannel().equals(event.getChannel().getId())) {
            setDatabaseColumnToNull(event.getGuild().getId(), "level_channel");
        }

        if (transformer.getMusicChannelText() != null && transformer.getMusicChannelText().equals(event.getChannel().getId())) {
            setDatabaseColumnToNull(event.getGuild().getId(), "music_channel_text");
        }
    }

    private void handleTextChannelDeleteReactionsRoles(TextChannelDeleteEvent event) {
        Collection collection = ReactionController.fetchReactions(sable, event.getGuild());
        if (collection == null || collection.isEmpty()) {
            return;
        }

        if (collection.where("channel_id", event.getChannel().getId()).isEmpty()) {
            return;
        }

        try {
            sable.getDatabase().newQueryBuilder(Constants.REACTION_ROLES_TABLE_NAME)
                .where("channel_id", event.getChannel().getId())
                .delete();

            ReactionController.forgetCache(event.getGuild().getIdLong());
        } catch (SQLException e) {
            log.error("Failed to delete reaction roles from {} for channel ID {}, error: {}",
                event.getGuild().getId(), event.getChannel().getId(), e.getMessage(), e
            );
        }
    }

    public void onVoiceChannelDelete(VoiceChannelDeleteEvent event) {
        GuildTransformer transformer = GuildController.fetchGuild(sable, event.getGuild());
        if (transformer == null) {
            return;
        }

        if (transformer.getMusicChannelVoice() != null && transformer.getMusicChannelVoice().equals(event.getChannel().getId())) {
            setDatabaseColumnToNull(event.getGuild().getId(), "music_channel_voice");
        }
    }

    public void updateChannelData(Guild guild) {
        try {
            sable.getDatabase().newQueryBuilder(Constants.GUILD_TABLE_NAME)
                .useAsync(true)
                .where("id", guild.getId())
                .update(statement -> {
                    statement.set("channels_data", GuildController.buildChannelData(guild.getTextChannels()), true);
                });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setDatabaseColumnToNull(String guildId, String column) {
        try {
            sable.getDatabase().newQueryBuilder(Constants.GUILD_TABLE_NAME)
                .useAsync(true)
                .where("id", guildId)
                .update(statement -> statement.set(column, null));
        } catch (SQLException ignored) {
            //
        }
    }
}
