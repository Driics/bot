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

package com.sable.servlet.routes;

import com.sable.Sable;
import com.sable.contracts.metrics.SparkRoute;
import net.dv8tion.jda.api.entities.Guild;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

public class GetGuildsExists extends SparkRoute {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String[] ids = request.params("ids").split(",");

        JSONObject root = new JSONObject();
        for (String id : ids) {
            try {
                Guild guildById = Sable.getInstance().getShardManager().getGuildById(Long.parseLong(id));
                if (guildById == null) {
                    root.put(id, false);
                    continue;
                }
                root.put(id, true);
            } catch (NumberFormatException e) {
                root.put(id, false);
            }
        }

        return root;
    }
}
