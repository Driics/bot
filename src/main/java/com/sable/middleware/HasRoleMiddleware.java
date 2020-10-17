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
import com.sable.factories.MessageFactory;
import com.sable.permissions.Permissions;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HasRoleMiddleware extends Middleware {

    public HasRoleMiddleware(Sable avaire) {
        super(avaire);
    }

    @Override
    public String buildHelpDescription(@Nonnull CommandMessage context, @Nonnull String[] arguments) {
        if (arguments.length == 1) {
            return String.format("**The `%s` role is required to use this command!**", arguments[0]);
        }

        return String.format("**The `%s` roles is required to use this command!**",
            String.join("`, `", arguments)
        );
    }

    @Override
    public boolean handle(@Nonnull Message message, @Nonnull MiddlewareStack stack, String... args) {
        if (!message.getChannelType().isGuild()) {
            return stack.next();
        }

        if (message.getMember().hasPermission(Permissions.ADMINISTRATOR.getPermission())) {
            return stack.next();
        }

        List<Role> roles = message.getMember().getRoles();
        for (String roleName : args) {
            if (!hasRole(roles, roleName)) {
                return runMessageCheck(message, () -> {
                    MessageFactory.makeError(message, "You don't have the required role to execute this command:\n`:role`")
                        .set("role", roleName)
                        .queue(newMessage -> newMessage.delete().queueAfter(45, TimeUnit.SECONDS));

                    return false;
                });
            }
        }

        return stack.next();
    }

    private boolean hasRole(List<Role> roles, String roleName) {
        for (Role role : roles) {
            if (role.getName().equalsIgnoreCase(roleName)) {
                return true;
            }
        }
        return false;
    }
}
