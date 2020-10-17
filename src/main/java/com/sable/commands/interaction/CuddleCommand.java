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

public class CuddleCommand extends InteractionCommand {

    public CuddleCommand(Sable avaire) {
        super(avaire);
    }

    @Override
    public List<String> getInteractionImages() {
        return Arrays.asList(
            "https://i.imgur.com/HqFckvG.gif",
            "https://i.imgur.com/pH8TTOd.gif",
            "https://i.imgur.com/3SqJTbV.gif",
            "https://i.imgur.com/SVAA1NH.gif",
            "https://i.imgur.com/6RyqnBL.gif",
            "https://i.imgur.com/YrMqjtQ.gif",
            "https://i.imgur.com/EAk4KtB.gif",
            "https://i.imgur.com/FAGX2zs.gif"
        );
    }

    @Override
    public String getName() {
        return "Cuddle Command";
    }

    @Override
    public List<String> getTriggers() {
        return Arrays.asList("cuddle", "cuddles");
    }
}
