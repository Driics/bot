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
import com.sable.contracts.shard.EntityGenerator;
import net.dv8tion.jda.api.JDA;

public class ShardEntity {

    private final EntityGenerator generator;

    private long value = 0;
    private long lastUpdate = -1;

    ShardEntity(EntityGenerator generator) {
        this.generator = generator;
    }

    public long getValue(Sable avaire) {
        if (isOutdated()) {
            long count = 0;
            for (JDA shard : avaire.getShardManager().getShards()) {
                count += generator.generateEntity(shard);
            }
            value = count;

            lastUpdate = System.currentTimeMillis() + 15000;
        }

        return value;
    }

    private boolean isOutdated() {
        return lastUpdate < System.currentTimeMillis();
    }
}
