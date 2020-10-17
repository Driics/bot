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

package com.sable.contracts.handlers;

import com.sable.Sable;

public abstract class EventHandler extends EventListener {

    /**
     * The Sable class instance, this is used to access
     * and interact with the rest of the application.
     */
    protected final Sable avaire;

    /**
     * Instantiates the event handler and sets the sable class instance.
     *
     * @param avaire The Sable application class instance.
     */
    public EventHandler(Sable avaire) {
        this.avaire = avaire;
    }
}
