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

package com.sable.commands.music.playlist;

import com.sable.Sable;
import com.sable.Constants;
import com.sable.audio.AudioHandler;
import com.sable.audio.TrackRequestContext;
import com.sable.audio.exceptions.SearchingException;
import com.sable.audio.searcher.SearchTrackResultHandler;
import com.sable.commands.CommandMessage;
import com.sable.commands.music.PlaylistCommand;
import com.sable.contracts.commands.playlist.PlaylistSubCommand;
import com.sable.database.controllers.PlaylistController;
import com.sable.database.transformers.GuildTransformer;
import com.sable.database.transformers.PlaylistTransformer;
import com.sable.utilities.NumberUtil;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.sql.SQLException;
import java.util.Arrays;

public class AddSongToPlaylist extends PlaylistSubCommand {

    public AddSongToPlaylist(Sable avaire, PlaylistCommand command) {
        super(avaire, command);
    }

    @Override
    public boolean onCommand(CommandMessage context, String[] args, GuildTransformer guild, PlaylistTransformer playlist) {
        String query = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

        if (query.trim().length() == 0) {
            context.makeWarning(context.i18n("invalidFormat"))
                .set("command", command.generateCommandTrigger(context.getMessage()) + " " + playlist.getName() + " add <song title / link>")
                .set("type", "song")
                .queue();

            return false;
        }

        if (playlist.getSongs().size() >= guild.getType().getLimits().getPlaylist().getSongs()) {
            context.makeWarning(context.i18n("noMoreSongSlots"))
                .set("playlist", playlist.getName())
                .queue();

            return false;
        }

        TrackRequestContext trackContext = AudioHandler.getDefaultAudioHandler().createTrackRequestContext(
            context.getAuthor(), query.split(" ")
        );

        context.getChannel().sendTyping().queue(v -> loadSong(context, trackContext, guild, playlist));
        return true;
    }

    private void loadSong(CommandMessage context, TrackRequestContext trackContext, GuildTransformer guild, PlaylistTransformer playlist) {
        try {
            AudioPlaylist result = new SearchTrackResultHandler(trackContext).searchSync();

            if (result.getTracks() == null || result.getTracks().isEmpty()) {
                context.makeWarning(context.i18n("noMatches")).queue();
            } else {
                handleTrackLoadedEvent(context, guild, playlist, result.getTracks().get(0));
            }
        } catch (SearchingException e) {
            context.makeWarning(context.i18n("failedToLoad", e.getMessage())).queue();
        }
    }

    private void handleTrackLoadedEvent(CommandMessage context, GuildTransformer guild, PlaylistTransformer playlist, AudioTrack track) {
        if (track.getInfo().isStream) {
            context.makeWarning(context.i18n("attemptingToAddLivestreamToPlaylist")).queue();
            return;
        }

        playlist.addSong(
            track.getInfo().title,
            NumberUtil.formatTime(track.getDuration()),
            track.getInfo().uri
        );

        try {
            avaire.getDatabase().newQueryBuilder(Constants.MUSIC_PLAYLIST_TABLE_NAME)
                .where("id", playlist.getId()).andWhere("guild_id", context.getGuild().getId())
                .update(statement -> {
                    statement.set("songs", Sable.gson.toJson(playlist.getSongs()), true);
                    statement.set("amount", playlist.getSongs().size());
                });

            PlaylistController.forgetCache(context.getGuild().getIdLong());

            context.makeSuccess(context.i18n("userHasAddedSong"))
                .set("name", track.getInfo().title)
                .set("url", track.getInfo().uri)
                .set("playlist", playlist.getName())
                .set("slots", guild.getType().getLimits().getPlaylist().getSongs() - playlist.getSongs().size())
                .queue();
        } catch (SQLException e) {
            e.printStackTrace();

            context.makeError(context.i18n("failedToSavePlaylist", e.getMessage())).queue();
        }
    }
}
