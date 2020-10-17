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
import com.sable.commands.CommandHandler;
import com.sable.commands.CommandMessage;
import com.sable.commands.fun.RandomDogCommand;
import com.sable.contracts.ai.Intent;

@SuppressWarnings("unused")
public class RequestDog extends Intent {

    public RequestDog(Sable avaire) {
        super(avaire);
    }

    @Override
    public String getAction() {
        return "request.dog";
    }

    @Override
    @SuppressWarnings({"SingleStatementInBlock", "ConstantConditions"})
    public void onIntent(CommandMessage context, AIResponse response) {
        CommandHandler.getCommand(RandomDogCommand.class)
            .getCommand().onCommand(new CommandMessage(context), new String[0]);
    }
}
