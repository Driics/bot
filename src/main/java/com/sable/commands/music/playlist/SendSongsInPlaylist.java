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
import com.sable.database.transformers.GuildTransformer;
import com.sable.database.transformers.PlaylistTransformer;
import com.sable.utilities.NumberUtil;

import java.util.ArrayList;
import java.util.List;

public class SendSongsInPlaylist extends PlaylistSubCommand {

    public SendSongsInPlaylist(Sable avaire, PlaylistCommand command) {
        super(avaire, command);
    }

    @Override
    public boolean onCommand(CommandMessage context, String[] args, GuildTransformer guild, PlaylistTransformer playlist) {
        if (playlist.getSongs().isEmpty()) {
            context.makeWarning(context.i18n("playlistIsEmpty"))
                .set("command", command.generateCommandTrigger(context.getMessage()) + " " + playlist.getName() + " add <song url>")
                .queue();

            return false;
        }

        SimplePaginator<PlaylistTransformer.PlaylistSong> paginator = new SimplePaginator<>(playlist.getSongs(), 10);
        if (args.length > 1) {
            paginator.setCurrentPage(NumberUtil.parseInt(args[1], 1));
        }

        List<String> messages = new ArrayList<>();
        paginator.forEach((index, key, song) -> {
            messages.add(context.i18n("playlistSongLine",
                index + 1,
                song.getTitle(),
                song.getLink(),
                song.getDuration()
            ));
        });

        context.makeInfo(
            String.join("\n", messages) + "\n\n" + paginator.generateFooter(
                context.getGuild(),
                command.generateCommandTrigger(context.getMessage()) + " " + playlist.getName()
            )
        ).setTitle(":musical_note: " + playlist.getName()).queue();

        return true;
    }
}
