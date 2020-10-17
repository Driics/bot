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

package com.sable.database.transformers;

import com.sable.Sable;
import com.sable.contracts.database.transformers.Transformer;
import com.sable.contracts.debug.Evalable;
import com.sable.database.collection.DataRow;
import com.google.gson.reflect.TypeToken;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class PlaylistTransformer extends Transformer {

    private final List<PlaylistSong> songs = new ArrayList<>();

    private int id;
    private int size;
    private long guildId;
    private String name;

    public PlaylistTransformer(DataRow data) {
        super(data);

        if (hasData()) {
            id = data.getInt("id");
            size = data.getInt("amount");
            guildId = data.getLong("guild_id");
            name = data.getString("name");

            if (data.has("songs") && data.getString("songs", "").length() > 0) {
                List<PlaylistSong> songs = Sable.gson.fromJson(data.getString("songs"), (new TypeToken<List<PlaylistSong>>() {
                }.getType()));

                if (!songs.isEmpty()) {
                    this.songs.addAll(songs);
                }
            }
        }
    }

    public int getId() {
        return id;
    }

    public long getGuildId() {
        return guildId;
    }

    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PlaylistSong> getSongs() {
        return songs;
    }

    public void addSong(@Nonnull String title, @Nonnull String duration, @Nonnull String link) {
        PlaylistSong song = new PlaylistSong();

        song.title = title;
        song.duration = duration;
        song.link = link;

        songs.add(song);
    }

    public class PlaylistSong extends Evalable {
        private String title;
        private String duration;
        private String link;

        public String getTitle() {
            return title;
        }

        public String getDuration() {
            return duration;
        }

        public String getLink() {
            return link;
        }
    }
}
