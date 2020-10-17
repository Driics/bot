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

public class MarryCommand extends InteractionCommand {

    public MarryCommand(Sable avaire) {
        super(avaire);
    }

    @Override
    public String getName() {
        return "Marry Me Command";
    }

    @Override
    public List<String> getTriggers() {
        return Arrays.asList("marryme", "marry");
    }

    @Override
    public List<String> getInteractionImages() {
        return Arrays.asList(
            "https://i.imgur.com/JBdzTFS.gif",
            "https://i.imgur.com/kXKaIlV.gif",
            "https://i.imgur.com/bIZDJoG.gif",
            "https://i.imgur.com/Uq4gCOM.gif",
            "https://i.imgur.com/hULfQUb.gif",
            "https://i.imgur.com/EP3Wc5N.gif",
            "https://i.imgur.com/P8d7tM6.gif"
        );
    }
}
