/*
 * Copyright (c) 2019.
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

package com.sable.contracts.commands;

import com.sable.Sable;
import com.sable.database.transformers.GuildTransformer;
import net.dv8tion.jda.api.entities.Role;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class MuteableCommand extends Command {

    /**
     * Creates the given command instance by calling {@link Command#Command(Sable, boolean)} with allowDM set to true.
     *
     * @param avaire The Sable class instance.
     */
    public MuteableCommand(Sable avaire) {
        super(avaire);
    }

    /**
     * Creates the given command instance with the given
     * Sable instance and the allowDM settings.
     *
     * @param avaire  The Sable class instance.
     * @param allowDM Determines if the command can be used in DMs.
     */
    public MuteableCommand(Sable avaire, boolean allowDM) {
        super(avaire, allowDM);
    }

    /**
     * Gets the name of the role used for muting users, if a valid mute role has been setup for
     * the server, the role will be returned in a mentionable format, however if no valid role
     * have been setup for the server the string "`Muted`" will be returned instead.
     *
     * @param context The command context that should be used to get the muted role name.
     * @return The name of the role used for muting users for the given command context.
     */
    @Nonnull
    protected String getMuteRoleNameFromContext(@Nullable CommandContext context) {
        if (context != null && context.getGuildTransformer() != null) {
            GuildTransformer transformer = context.getGuildTransformer();
            if (transformer.getMuteRole() != null) {
                Role muteRole = context.getGuild().getRoleById(transformer.getMuteRole());
                if (muteRole != null) {
                    return muteRole.getAsMention();
                }
            }
        }
        return "`Muted`";
    }
}
