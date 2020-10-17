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
import net.dv8tion.jda.api.entities.TextChannel;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChannelIdCommand extends Command {

    public ChannelIdCommand(Sable avaire) {
        super(avaire, false);
    }

    @Override
    public String getName() {
        return "Channel ID Command";
    }

    @Override
    public String getDescription() {
        return "Shows the ID of the channel the command was ran in, or the channel tagged in the command.";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Collections.singletonList("`:command [channel]` - Gets the ID of the current channel, or the mentioned channel.");
    }

    @Override
    public List<String> getExampleUsage() {
        return Arrays.asList(
            "`:command #general`",
            "`:command`"
        );
    }

    @Override
    public List<Class<? extends Command>> getRelations() {
        return Collections.singletonList(ChannelInfoCommand.class);
    }

    @Override
    public List<String> getTriggers() {
        return Arrays.asList("channelid", "cid");
    }

    @Nonnull
    @Override
    public List<CommandGroup> getGroups() {
        return Collections.singletonList(CommandGroups.INFORMATION);
    }

    @Override
    public boolean onCommand(CommandMessage context, String[] args) {
        TextChannel channel = context.getChannel();
        if (!context.getMentionedChannels().isEmpty()) {
            channel = context.getMentionedChannels().get(0);
        }

        context.makeSuccess(context.i18n("message"))
            .set("targetChannel", channel.getId())
            .queue();

        return true;
    }
}
