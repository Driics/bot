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
import com.sable.commands.CommandMessage;
import com.sable.contracts.commands.SystemCommand;
import com.sable.plugin.PluginLoader;

import java.util.Collections;
import java.util.List;

public class ReloadCommand extends SystemCommand {

    public ReloadCommand(Sable avaire) {
        super(avaire);
    }

    @Override
    public String getName() {
        return "Reload Configuration Command";
    }

    @Override
    public String getDescription() {
        return "Reloads the main configuration, and all the configs for loaded plugins.";
    }

    @Override
    public List<String> getTriggers() {
        return Collections.singletonList("reload");
    }

    @Override
    public boolean onCommand(CommandMessage context, String[] args) {
        avaire.getConfig().reloadConfig();
        avaire.getConstants().reloadConfig();

        for (PluginLoader loader : avaire.getPluginManager().getPlugins()) {
            loader.getClassLoader().getPlugin().reloadConfig();
        }

        context.makeSuccess("Configuration has been successfully reloaded!").queue();

        return true;
    }
}
