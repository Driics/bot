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

package com.sable.commands.system;

import com.sable.Sable;
import com.sable.contracts.commands.ApplicationShutdownCommand;
import com.avairebot.shared.ExitCodes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ShutdownCommand extends ApplicationShutdownCommand {

    public ShutdownCommand(Sable avaire) {
        super(avaire);
    }

    @Override
    public String getName() {
        return "Shutdown Command";
    }

    @Override
    public String getDescription() {
        return "Schedules a time the sable should be shutdown gracefully.";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Arrays.asList(
            "`:command now` - Shuts down the sable now.",
            "`:command cancel` - Cancels the shutdown process.",
            "`:command <time>` - Schedules a time the sable should be shutdown."
        );
    }

    @Override
    public List<String> getTriggers() {
        return Collections.singletonList("shutdown");
    }

    @Override
    public int exitCode() {
        return ExitCodes.EXIT_CODE_NORMAL;
    }

    @Override
    public String shutdownNow() {
        return "Shutting down processes... See you soon :wave:";
    }

    @Override
    public String scheduleShutdown() {
        return "The sable has been scheduled to shutdown in :fromNow.\n**Date:** :date";
    }

    @Override
    public String scheduleCancel() {
        return "The shutdown process has been canceled.";
    }
}
