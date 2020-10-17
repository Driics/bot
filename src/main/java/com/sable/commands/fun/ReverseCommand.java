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
import com.sable.chat.PlaceholderMessage;
import com.sable.commands.CommandMessage;
import com.sable.contracts.commands.Command;

import java.util.Collections;
import java.util.List;

public class ReverseCommand extends Command {

    public ReverseCommand(Sable avaire) {
        super(avaire);
    }

    @Override
    public String getName() {
        return "Reverse Command";
    }

    @Override
    public String getDescription() {
        return "Reverses the message given.";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Collections.singletonList("`:command <message>` - Reverses the given message.");
    }

    @Override
    public List<String> getExampleUsage() {
        return Collections.singletonList("`:command This is some random message`");
    }

    @Override
    public List<String> getTriggers() {
        return Collections.singletonList("reverse");
    }

    @Override
    public boolean onCommand(CommandMessage context, String[] args) {
        if (args.length == 0) {
            return sendErrorMessage(context, "errors.missingArgument", "message");
        }

        String string = String.join(" ", args);
        String reverse = new StringBuilder(string).reverse().toString();

        PlaceholderMessage infoMessage = context.makeInfo(reverse);

        if (string.equalsIgnoreCase(reverse)) {
            infoMessage.setFooter(context.i18n("palindrome"));
        }

        infoMessage.queue();
        return true;
    }
}
