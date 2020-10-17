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
import com.sable.chat.SimplePaginator;
import com.sable.commands.CommandMessage;
import com.sable.commands.music.PlaylistCommand;
import com.sable.contracts.commands.playlist.PlaylistSubCommand;
import com.sable.database.collection.Collection;
import com.sable.database.collection.DataRow;
import com.sable.database.transformers.GuildTransformer;
import com.sable.utilities.NumberUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SendPlaylists extends PlaylistSubCommand {

    public SendPlaylists(Sable avaire, PlaylistCommand command) {
        super(avaire, command);
    }

    @Override
    public boolean onCommand(CommandMessage context, String[] args, GuildTransformer guild, Collection playlists) {
        SimplePaginator<DataRow> paginator = new SimplePaginator<>(playlists.sort(
            Comparator.comparing(dataRow -> dataRow.getString("name"))
        ).getItems(), 5);

        if (args.length > 0) {
            paginator.setCurrentPage(NumberUtil.parseInt(args[0], 1));
        }

        List<String> messages = new ArrayList<>();
        paginator.forEach((index, key, row) -> {
            messages.add(context.i18n(
                "playlistLine",
                row.getString("name"),
                row.getInt("amount")
            ));
        });

        String counter = context.i18n("playlistSize", playlists.size(), guild.getType().getLimits().getPlaylist().getPlaylists());

        context.makeInfo("\u2022 " +
            String.join("\n\u2022 ", messages) + "\n\n" +
            paginator.generateFooter(context.getGuild(), command.generateCommandTrigger(context.getMessage()))
        ).setTitle(context.i18n("playlistTitle", counter)).queue();

        return true;
    }
}
