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

package com.sable.contracts.commands.playlist;

import com.sable.Sable;
import com.sable.commands.CommandMessage;
import com.sable.commands.music.PlaylistCommand;
import com.sable.database.collection.Collection;
import com.sable.database.transformers.GuildTransformer;
import com.sable.database.transformers.PlaylistTransformer;

public abstract class PlaylistSubCommand {

    /**
     * The main {@link Sable sable} application instance.
     */
    protected final Sable avaire;

    /**
     * The parent playlist command, used for accessing command specific
     * methods and generating error response messages.
     */
    protected final PlaylistCommand command;

    /**
     * Creates a new playlist sub command instance.
     *
     * @param avaire  The main sable application instance.
     * @param command The parent playlist command instance.
     */
    public PlaylistSubCommand(Sable avaire, PlaylistCommand command) {
        this.avaire = avaire;
        this.command = command;
    }

    /**
     * The main command handler for the sub playlist command, the method will invoke
     * another sub command depending on what the object type given is.
     *
     * @param context The command message context generated using the
     *                JDA message event that invoked the command.
     * @param args    The arguments parsed to the command.
     * @param guild   The guild transformer instance for the current guild.
     * @param object  The playlist transformer instance, or a collection
     *                of playlists loaded from the database.
     * @return {@code True} on success, {@code False} on failure.
     * @see #onCommand(CommandMessage, String[], GuildTransformer, Collection)
     * @see #onCommand(CommandMessage, String[], GuildTransformer, PlaylistTransformer)
     */
    public final boolean onCommand(CommandMessage context, String[] args, GuildTransformer guild, Object object) {
        if (object instanceof PlaylistTransformer) {
            return onCommand(context, args, guild, (PlaylistTransformer) object);
        }

        return object instanceof Collection && onCommand(context, args, guild, (Collection) object);
    }

    /**
     * Handles the sub playlist command using the given command context,
     * command arguments, guild transformer for the current server,
     * and the playlist transformer for the selected playlist.
     *
     * @param context  The command message context generated using the
     *                 JDA message event that invoked the command.
     * @param args     The arguments parsed to the command.
     * @param guild    The guild transformer instance for the current guild.
     * @param playlist The selected playlist that should be used for the command.
     * @return {@code True} on success, {@code False} on failure.
     */
    public boolean onCommand(CommandMessage context, String[] args, GuildTransformer guild, PlaylistTransformer playlist) {
        return false;
    }

    /**
     * Handles the sub playlist command using the given command context,
     * command arguments, guild transformer for the current server,
     * and the collection of playlists.
     *
     * @param context   The command message context generated using the
     *                  JDA message event that invoked the command.
     * @param args      The arguments parsed to the command.
     * @param guild     The guild transformer instance for the current guild.
     * @param playlists The playlists that should be used for the command.
     * @return {@code True} on success, {@code False} on failure.
     */
    public boolean onCommand(CommandMessage context, String[] args, GuildTransformer guild, Collection playlists) {
        return false;
    }
}
