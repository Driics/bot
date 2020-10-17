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

package com.sable.commands.fun;

import com.sable.Sable;
import com.sable.commands.CommandMessage;
import com.sable.commands.CommandPriority;
import com.sable.contracts.commands.Command;
import com.sable.factories.RequestFactory;
import com.sable.requests.Response;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import okhttp3.ResponseBody;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class MonikaCommand extends Command {

    public MonikaCommand(Sable avaire) {
        super(avaire);
    }

    @Override
    public String getName() {
        return "Just Monika";
    }

    @Override
    public String getDescription() {
        return "Just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika...";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Collections.singletonList("Just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika...");
    }

    @Override
    public List<String> getExampleUsage() {
        return Collections.singletonList("Just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika, just Monika...");
    }

    @Override
    public List<String> getTriggers() {
        return Arrays.asList("justmonika", "monika");
    }

    @Override
    public CommandPriority getCommandPriority() {
        return CommandPriority.HIDDEN;
    }

    @Override
    public boolean onCommand(CommandMessage context, String[] args) {
        RequestFactory.makeGET("https://i.imgur.com/ZupgGkI.jpg")
            .send((Consumer<Response>) response -> {
                ResponseBody body = response.getResponse().body();

                if (body == null) {
                    return;
                }

                context.getChannel().sendMessage(
                    new MessageBuilder().setEmbed(
                        new EmbedBuilder()
                            .setImage("attachment://just-monika.jpg")
                            .setDescription("Just Monika")
                            .setFooter("Just Monika", null)
                            .build()
                    ).build()).addFile(body.byteStream(),
                    "just-monika.jpg"
                ).queue();
            });

        return true;
    }
}
