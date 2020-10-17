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

package com.sable.commands.utility;

import com.sable.Sable;
import com.sable.commands.CommandMessage;
import com.sable.contracts.commands.Command;
import com.sable.contracts.commands.CommandGroup;
import com.sable.contracts.commands.CommandGroups;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ServerIdCommand extends Command {

    public ServerIdCommand(Sable avaire) {
        super(avaire, false);
    }

    @Override
    public String getName() {
        return "Server ID Command";
    }

    @Override
    public String getDescription() {
        return "Shows the ID of the server the command was ran in.";
    }

    @Override
    public List<Class<? extends Command>> getRelations() {
        return Collections.singletonList(ServerInfoCommand.class);
    }

    @Override
    public List<String> getTriggers() {
        return Arrays.asList("serverid", "sid");
    }

    @Nonnull
    @Override
    public List<CommandGroup> getGroups() {
        return Collections.singletonList(CommandGroups.INFORMATION);
    }

    @Override
    public boolean onCommand(CommandMessage context, String[] args) {
        context.makeSuccess(context.i18n("message"))
            .queue();

        return true;
    }
}
