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
import java.util.Collections;
import java.util.List;

public class TriggeredCommand extends InteractionCommand {

    public TriggeredCommand(Sable avaire) {
        super(avaire);
    }

    @Override
    public List<String> getInteractionImages() {
        return Arrays.asList(
            "https://i.imgur.com/HZ3nuUp.gif",
            "https://i.imgur.com/PL71evt.gif",
            "https://i.imgur.com/ISGZQzD.gif",
            "https://i.imgur.com/mE7JmCq.gif",
            "https://i.imgur.com/yxl7WzG.gif",
            "https://i.imgur.com/ssJNnyS.gif"
        );
    }

    @Override
    public String getName() {
        return "Triggered Command";
    }

    @Override
    public List<String> getTriggers() {
        return Collections.singletonList("triggered");
    }
}
