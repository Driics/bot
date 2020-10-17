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
import com.sable.middleware.permission.PermissionCheck;
import com.sable.middleware.permission.PermissionCommon;
import com.sable.middleware.permission.PermissionType;
import com.sable.permissions.Permissions;
import com.sable.utilities.RestActionUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RequirePermissionMiddleware extends Middleware {

    public RequirePermissionMiddleware(Sable avaire) {
        super(avaire);
    }

    @Override
    public String buildHelpDescription(@Nonnull CommandMessage context, @Nonnull String[] arguments) {
        PermissionType type = PermissionType.fromName(arguments[0]);
        arguments = Arrays.copyOfRange(arguments, 1, arguments.length);

        if (arguments.length == 1) {
            return PermissionCommon.formatWithOneArgument(type, arguments[0]);
        }

        return String.format(PermissionCommon.getPermissionTypeMessage(type, true),
            Arrays.stream(arguments)
                .map(Permissions::fromNode)
                .map(Permissions::getPermission)
                .map(Permission::getName)
                .collect(Collectors.joining("`, `"))
        );
    }

    @Override
    public boolean handle(@Nonnull Message message, @Nonnull MiddlewareStack stack, String... args) {
        if (!message.getChannelType().isGuild()) {
            return stack.next();
        }

        if (args.length < 2) {
            Sable.getLogger().warn(String.format(
                "\"%s\" is parsing invalid amount of arguments to the require middleware, 2 arguments are required.", stack.getCommand()
            ));
            return stack.next();
        }

        PermissionCheck permissionCheck = new PermissionCheck(message, args);
        if (!permissionCheck.check(stack)) {
            return false;
        }

        if (!permissionCheck.getMissingUserPermissions().isEmpty()) {
            runMessageCheck(message, () -> {
                MessageFactory.makeError(message, "You're missing the required permission node for this command:\n`:permission`")
                    .set("permission", permissionCheck.getMissingUserPermissions().stream()
                        .map(Permissions::getPermission)
                        .map(Permission::getName)
                        .collect(Collectors.joining("`, `"))
                    ).queue(newMessage -> newMessage.delete().queueAfter(45, TimeUnit.SECONDS, null, RestActionUtil.ignore));

                return false;
            });
        }

        if (!permissionCheck.getMissingBotPermissions().isEmpty()) {
            runMessageCheck(message, () -> {
                MessageFactory.makeError(message, "I'm missing the following permission to run this command successfully:\n`:permission`")
                    .set("permission", permissionCheck.getMissingBotPermissions().stream()
                        .map(Permissions::getPermission)
                        .map(Permission::getName)
                        .collect(Collectors.joining("`, `"))
                    ).queue(newMessage -> newMessage.delete().queueAfter(45, TimeUnit.SECONDS, null, RestActionUtil.ignore));

                return false;
            });
        }

        return permissionCheck.isEmpty() && stack.next();
    }
}
