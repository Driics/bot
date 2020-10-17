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
import com.sable.commands.CommandMessage;
import com.sable.commands.music.PlaylistCommand;
import com.sable.contracts.commands.playlist.PlaylistSubCommand;
import com.sable.database.controllers.PlaylistController;
import com.sable.database.transformers.GuildTransformer;
import com.sable.database.transformers.PlaylistTransformer;

import java.sql.SQLException;

public class DeletePlaylist extends PlaylistSubCommand {

    public DeletePlaylist(Sable avaire, PlaylistCommand command) {
        super(avaire, command);
    }

    @Override
    public boolean onCommand(CommandMessage context, String[] args, GuildTransformer guild, PlaylistTransformer playlist) {
        try {
            avaire.getDatabase().newQueryBuilder(Constants.MUSIC_PLAYLIST_TABLE_NAME)
                .where("guild_id", context.getGuild().getId())
                .andWhere("id", playlist.getId())
                .delete();

            PlaylistController.forgetCache(context.getGuild().getIdLong());

            context.makeSuccess(context.i18n("playlistDeleted"))
                .set("name", playlist.getName())
                .queue();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            context.makeError("Error: " + e.getMessage()).queue();
        }

        return false;
    }
}
