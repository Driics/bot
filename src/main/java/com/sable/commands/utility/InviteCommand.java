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
import com.sable.chat.PlaceholderMessage;
import com.sable.commands.CommandMessage;
import com.sable.contracts.commands.Command;
import com.sable.contracts.commands.CommandGroup;
import com.sable.contracts.commands.CommandGroups;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InviteCommand extends Command {

    public InviteCommand(Sable avaire) {
        super(avaire);
    }

    @Override
    public String getName() {
        return "Invite Command";
    }

    @Override
    public String getDescription() {
        return "Returns a link that can be used to invite the sable to other servers.";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Collections.singletonList("`:command` - Gives you an invite link that can be used to invite Sable to servers.");
    }

    @Override
    public List<String> getTriggers() {
        return Arrays.asList("invite", "join");
    }

    @Nonnull
    @Override
    public List<CommandGroup> getGroups() {
        return Collections.singletonList(CommandGroups.BOT_INFORMATION);
    }

    @Override
    public boolean onCommand(CommandMessage context, String[] args) {
        PlaceholderMessage note = new PlaceholderMessage(null, context.i18n("note"));

        context.makeInfo(context.i18n("message"))
            .set("oauth", avaire.getConfig().getString("discord.oauth"))
            .set("note", note.set("edgeInvite", "https://avairebot.com/invite-cutting-edge").toString())
            .queue();
        return true;
    }
}
