/*
 * Copyright (c) 2019.
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

package com.sable.plugin;

import com.sable.contracts.plugin.PluginAsset;
import org.json.JSONObject;

public class GithubPluginAsset implements PluginAsset {

    private final String name;
    private final String downloadableUrl;

    public GithubPluginAsset(JSONObject object) {
        name = object.getString("name");
        downloadableUrl = object.getString("browser_download_url");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDownloadableUrl() {
        return downloadableUrl;
    }
}
