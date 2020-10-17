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

package com.sable.ai.dialogflow.intents;

import ai.api.model.AIResponse;
import com.sable.Sable;
import com.sable.commands.CommandContainer;
import com.sable.commands.CommandHandler;
import com.sable.commands.CommandMessage;
import com.sable.commands.utility.RankCommand;
import com.sable.contracts.ai.Intent;
import com.sable.database.transformers.GuildTransformer;
import com.sable.factories.MessageFactory;

@SuppressWarnings("unused")
public class RequestLevel extends Intent {

    public RequestLevel(Sable avaire) {
        super(avaire);
    }

    @Override
    public String getAction() {
        return "request.level";
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onIntent(CommandMessage context, AIResponse response) {
        GuildTransformer guildTransformer = context.getGuildTransformer();
        if (guildTransformer == null || !guildTransformer.isLevels()) {
            MessageFactory.makeWarning(context.getMessage(),
                "This server doesn't have the `Levels & Experience` feature enabled so I can't tell you what level you are :("
            ).queue();
            return;
        }

        CommandContainer container = CommandHandler.getCommand(RankCommand.class);
        container.getCommand().onCommand(
            new CommandMessage(container, context.getDatabaseEventHolder(), context.getMessage()), new String[]{"---skip-mentions"}
        );
    }
}
