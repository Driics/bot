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

package com.sable.middleware.global;

import com.sable.Sable;
import com.sable.contracts.middleware.Middleware;
import com.sable.metrics.Metrics;
import com.sable.middleware.MiddlewareStack;
import net.dv8tion.jda.api.entities.Message;

import javax.annotation.Nonnull;

public class IncrementMetricsForCommand extends Middleware {

    public IncrementMetricsForCommand(Sable avaire) {
        super(avaire);
    }

    @Override
    public boolean handle(@Nonnull Message message, @Nonnull MiddlewareStack stack, String... args) {
        Metrics.commandsReceived.labels(stack.getCommand().getClass().getSimpleName()).inc();

        return stack.next();
    }
}
