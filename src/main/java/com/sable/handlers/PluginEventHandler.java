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

package com.sable.handlers;

import com.sable.Sable;
import com.sable.contracts.handlers.EventHandler;
import com.sable.plugin.PluginLoader;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PluginEventHandler extends EventHandler {

    /**
     * Instantiates the event handler and sets the sable class instance.
     *
     * @param avaire The Sable application class instance.
     */
    public PluginEventHandler(Sable avaire) {
        super(avaire);
    }

    @Override
    public void onGenericEvent(GenericEvent event) {
        for (PluginLoader plugin : avaire.getPluginManager().getPlugins()) {
            for (ListenerAdapter listener : plugin.getEventListeners()) {
                listener.onEvent(event);
            }
        }
    }
}
