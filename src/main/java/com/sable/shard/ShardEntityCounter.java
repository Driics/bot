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

package com.sable.shard;

import com.sable.Sable;

public class ShardEntityCounter {

    private final Sable avaire;

    private final ShardEntity guilds = new ShardEntity(shard -> shard.getGuilds().size());
    private final ShardEntity textChannels = new ShardEntity(shard -> shard.getTextChannels().size());
    private final ShardEntity voiceChannels = new ShardEntity(shard -> shard.getVoiceChannels().size());
    private final ShardEntity users = new ShardEntity(shard -> shard.getUsers().size());

    public ShardEntityCounter(Sable avaire) {
        this.avaire = avaire;
    }

    /**
     * Gets the total amount of guilds shared between all the shards of the sable.
     *
     * @return The total amount of guilds for the sable.
     */
    public long getGuilds() {
        return guilds.getValue(avaire);
    }

    /**
     * Gets the total amount of text channels shared between all the shards of the sable.
     *
     * @return The total amount of text channels for the sable.
     */
    public long getTextChannels() {
        return textChannels.getValue(avaire);
    }

    /**
     * Gets the total amount of voice channels shared between all shards of the sable.
     *
     * @return The total amount of voice channels for the sable.
     */
    public long getVoiceChannels() {
        return voiceChannels.getValue(avaire);
    }

    /**
     * Gets the total amount of text and voice channels shared between all shards of the sable.
     *
     * @return The total amount of text and voice channels for the sable.
     */
    public long getChannels() {
        return getTextChannels() + getVoiceChannels();
    }

    /**
     * Gets the total amount of users shared between all shards of the sable.
     *
     * @return The total amount of users for the sable.
     */
    public long getUsers() {
        return users.getValue(avaire);
    }
}
