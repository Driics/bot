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

package com.sable.contracts.commands;

import com.sable.Sable;
import com.sable.commands.CommandMessage;
import com.avairebot.shared.ExitCodes;
import com.sable.time.Carbon;
import com.sable.time.Formats;

import java.text.ParseException;

public abstract class ApplicationShutdownCommand extends SystemCommand {

    /**
     * Creates the given command instance by calling {@link #ApplicationShutdownCommand(Sable, boolean)} with allowDM set to true.
     *
     * @param avaire The Sable class instance.
     */
    public ApplicationShutdownCommand(Sable avaire) {
        this(avaire, true);
    }

    /**
     * Creates the given command instance with the given
     * Sable instance and the allowDM settings.
     *
     * @param avaire  The Sable class instance.
     * @param allowDM Determines if the command can be used in DMs.
     */
    public ApplicationShutdownCommand(Sable avaire, boolean allowDM) {
        super(avaire, allowDM);
    }

    /**
     * The command executor, this method is invoked by the command handler
     * and the middleware stack when a user sends a message matching the
     * commands prefix and one of its command triggers.
     *
     * @param context The JDA message object from the message received event.
     * @param args    The arguments given to the command, if no arguments was given the array will just be empty.
     * @return true on success, false on failure.
     */
    @Override
    public boolean onCommand(CommandMessage context, String[] args) {
        if (context.isMentionableCommand()) {
            return sendErrorMessage(context, "This command can not be used via mentions!");
        }

        if (args.length == 0) {
            return sendErrorMessage(context, "You must include the time you want the sable to shutdown.");
        }

        if (args[0].equalsIgnoreCase("now")) {
            context.makeInfo(shutdownNow())
                .queue(
                    shutdownMessage -> avaire.shutdown(exitCode()),
                    throwable -> avaire.shutdown(exitCode())
                );

            return true;
        }

        if (args[0].equalsIgnoreCase("cancel")) {
            context.makeInfo(scheduleCancel())
                .queue(
                    shutdownMessage -> avaire.scheduleShutdown(null, exitCode()),
                    throwable -> avaire.scheduleShutdown(null, exitCode())
                );

            return true;
        }

        Carbon time = formatInput(String.join(" ", args));
        if (time == null) {
            return sendErrorMessage(context, "Invalid time format given, `%s` is not a valid supported time format.",
                String.join(" ", args)
            );
        }

        if (time.isPast()) {
            return sendErrorMessage(context, "The time given is in the past, that doesn't really work... Use a time set in the future, or use `now`.");
        }

        context.makeSuccess(scheduleShutdown())
            .set("fromNow", time.diffForHumans(true))
            .set("date", time.format("EEEEEEEE, dd MMMMMMM yyyy - HH:mm:ss z"))
            .queue(
                shutdownMessage -> avaire.scheduleShutdown(time, exitCode()),
                throwable -> avaire.scheduleShutdown(time, exitCode())
            );

        return true;
    }

    /**
     * Formats the input given by the user, trying to parse the time
     * to any of the valid formats supported by {@link Carbon}.
     *
     * @param time The string version of the time.
     * @return Possibly-null, A Carbon instance matching the given time, or {@code null}.
     */
    private Carbon formatInput(String time) {
        for (Formats format : Formats.values()) {
            try {
                return Carbon.createFromFormat(format.getFormat(), time);
            } catch (ParseException ignored) {
            }
        }
        return null;
    }

    /**
     * The {@link ExitCodes} that should be used when the sable shuts down.
     *
     * @return The exit code that should be used when the sable shuts down.
     */
    public abstract int exitCode();

    /**
     * The message that should be sent when the sable shuts down right now.
     *
     * @return The message that should be sent when the sable shuts down right now.
     */
    public abstract String shutdownNow();

    /**
     * The message that should be sent when the sable is scheduled to shutdown.
     *
     * @return The message that should be sent when the sable is scheduled to shutdown.
     */
    public abstract String scheduleShutdown();

    /**
     * The message that should be sent when a scheduled shutdown process is canceled.
     *
     * @return The message that should be sent when a scheduled shutdown process is canceled.
     */
    public abstract String scheduleCancel();
}
