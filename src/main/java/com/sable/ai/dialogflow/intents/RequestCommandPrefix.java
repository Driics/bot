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
import com.sable.commands.Category;
import com.sable.commands.CategoryHandler;
import com.sable.commands.CommandMessage;
import com.sable.contracts.ai.Intent;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class RequestCommandPrefix extends Intent {

    public RequestCommandPrefix(Sable avaire) {
        super(avaire);
    }

    @Override
    public String getAction() {
        return "command.prefix";
    }

    @Override
    public void onIntent(CommandMessage context, AIResponse response) {
        List<String> prefixes = new ArrayList<>();
        for (Category category : CategoryHandler.getValues()) {
            if (category.isGlobal()) continue;

            prefixes.add(
                String.format("`%s` %s", category.getPrefix(context.getMessage()), category.getName())
            );
        }

        context.makeSuccess(
            "Here is all my prefixes for this server.\n\n" +
                String.join("\n", prefixes)
        ).queue();
    }
}
