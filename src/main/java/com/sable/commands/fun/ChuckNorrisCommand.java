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
import com.sable.contracts.commands.Command;
import com.sable.factories.RequestFactory;
import com.sable.requests.Response;
import com.sable.requests.service.ChuckNorrisService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;

public class ChuckNorrisCommand extends Command {

    public ChuckNorrisCommand(Sable avaire) {
        super(avaire);
    }

    @Override
    public String getName() {
        return "Chuck Norris Command";
    }

    @Override
    public String getDescription() {
        return "I will get a random 100% true, real facts about Chuck Norris for you using the \"Internet Chuck Norris Database\".";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Collections.singletonList("`:command [name]` - Gets a random fact for you, if a name is given \"Chuck Norris\" will be replaced with the given name.");
    }

    @Override
    public List<String> getExampleUsage() {
        return Collections.singletonList("`:command @Senither`");
    }

    @Override
    public List<String> getTriggers() {
        return Arrays.asList("chucknorris", "chuck", "norris");
    }

    @Override
    public List<String> getMiddleware() {
        return Collections.singletonList("throttle:user,2,5");
    }

    @Override
    public boolean onCommand(CommandMessage context, String[] args) {
        RequestFactory.makeGET("http://api.icndb.com/jokes/random")
            .addParameter("escape", "javascript")
            .send((Consumer<Response>) response -> {
                ChuckNorrisService service = (ChuckNorrisService) response.toService(ChuckNorrisService.class);

                context.makeSuccess(prepareJoke(context, args, service.getValue().getJoke())).queue();
            });
        return true;
    }

    private String prepareJoke(CommandMessage message, String[] args, String joke) {
        if (!message.getMentionedUsers().isEmpty()) {
            return joke.replaceAll("Chuck Norris", Matcher.quoteReplacement(
                message.getGuild().getMember(
                    message.getMentionedUsers().get(0)
                ).getEffectiveName()
            ));
        }

        if (args.length > 0) {
            return joke.replaceAll("Chuck Norris", Matcher.quoteReplacement(String.join(" ", args)));
        }

        return joke;
    }
}
