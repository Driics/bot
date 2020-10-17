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

package com.sable.middleware;

import com.sable.Sable;
import com.sable.commands.CommandMessage;
import com.sable.contracts.middleware.Middleware;
import net.dv8tion.jda.api.entities.Message;

import javax.annotation.Nonnull;

public class IsDMMessageMiddleware extends Middleware {

    public IsDMMessageMiddleware(Sable avaire) {
        super(avaire);
    }

    @Override
    public String buildHelpDescription(@Nonnull CommandMessage context, @Nonnull String[] arguments) {
        return "**This command can only be used in DMs.**";
    }

    @Override
    public boolean handle(@Nonnull Message message, @Nonnull MiddlewareStack stack, String... args) {
        return !message.getChannelType().isGuild() && stack.next();
    }
}
