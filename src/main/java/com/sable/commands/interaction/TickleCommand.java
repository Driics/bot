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

package com.sable.commands.interaction;

import com.sable.Sable;
import com.sable.contracts.commands.InteractionCommand;

import java.util.Arrays;
import java.util.List;

public class TickleCommand extends InteractionCommand {

    public TickleCommand(Sable avaire) {
        super(avaire);
    }

    @Override
    public List<String> getInteractionImages() {
        return Arrays.asList(
            "https://i.imgur.com/qrjkj6d.gif",
            "https://i.imgur.com/fA3o4p4.gif",
            "https://i.imgur.com/AfobFAt.gif",
            "https://i.imgur.com/ExUivDr.gif",
            "https://i.imgur.com/osnnduz.gif",
            "https://i.imgur.com/hCbjjQ1.gif"
        );
    }

    @Override
    public String getName() {
        return "Tickle Command";
    }

    @Override
    public List<String> getTriggers() {
        return Arrays.asList("tickle", "tickles");
    }
}
