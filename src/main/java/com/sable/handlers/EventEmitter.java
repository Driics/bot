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
import com.sable.contracts.handlers.EventListener;
import com.sable.plugin.PluginLoader;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.utils.Checks;

public class EventEmitter {

    private final Sable avaire;

    /**
     * Creates a new event emitter instance using
     * the given Sable application instance.
     *
     * @param avaire The Sable instance that the event
     *               emitter should be created for.
     */
    public EventEmitter(Sable avaire) {
        this.avaire = avaire;
    }

    /**
     * Pushes the given event to all loaded plugins with at least
     * one event listener, forwarding the custom events through
     * each plugin one by one.
     *
     * @param event The event that should be pushed to all the loaded plugins.
     */
    public void push(Event event) {
        Checks.notNull(event, "event instance");
        for (PluginLoader plugin : avaire.getPluginManager().getPlugins()) {
            for (ListenerAdapter listener : plugin.getEventListeners()) {
                if (listener != null && listener instanceof EventListener) {
                    ((EventListener) listener).onCustomEvent(event);
                }
            }
        }
    }
}
