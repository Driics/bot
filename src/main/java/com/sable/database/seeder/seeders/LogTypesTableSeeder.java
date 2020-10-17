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

package com.sable.database.seeder.seeders;

import com.sable.Sable;
import com.sable.Constants;
import com.sable.contracts.database.seeder.Seeder;
import com.sable.modlog.ModlogType;

import java.sql.SQLException;

public class LogTypesTableSeeder extends Seeder {

    public LogTypesTableSeeder(Sable avaire) {
        super(avaire);
    }

    @Override
    public String table() {
        return Constants.LOG_TYPES_TABLE_NAME;
    }

    @Override
    public void run() throws SQLException {
        for (ModlogType type : ModlogType.values()) {
            if (!tableHasValue("id", type.getId())) {
                createRecord(type.getId(), type.getName(null));
            }
        }
    }

    private void createRecord(int id, String name) throws SQLException {
        createQuery().insert(statement -> {
            statement.set("id", id);
            statement.set("name", name);
        });
    }
}
