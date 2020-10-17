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

public class PoutCommand extends InteractionCommand {

    public PoutCommand(Sable avaire) {
        super(avaire);
    }

    @Override
    public List<String> getInteractionImages() {
        return Arrays.asList(
            "https://i.imgur.com/LuJllaE.gif",
            "https://i.imgur.com/bUk20mW.gif",
            "https://i.imgur.com/gWGH3mW.gif",
            "https://i.imgur.com/DZEqNrV.gif",
            "https://i.imgur.com/IvRIGse.gif",
            "https://i.imgur.com/ZeWvVY5.gif",
            "https://i.imgur.com/GsMZGE5.gif",
            "https://i.imgur.com/3EidTZl.gif",
            "https://i.imgur.com/B6Dsfsi.gif",
            "https://i.imgur.com/793Xoz8.gif",
            "https://i.imgur.com/p2gQm0i.gif"
        );
    }

    @Override
    public String getName() {
        return "Pout Command";
    }

    @Override
    public List<String> getTriggers() {
        return Arrays.asList("pouts", "pout");
    }
}
