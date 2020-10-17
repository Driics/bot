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

package com.sable.database.migrate.migrations;

import com.sable.Constants;
import com.sable.contracts.database.migrations.Migration;
import com.sable.database.schema.DatabaseEngine;
import com.sable.database.schema.Schema;

import java.sql.SQLException;

public class CreateVotesTableMigration implements Migration {

    @Override
    public String created_at() {
        return "Thu, Mar 22, 2018 2:08 PM";
    }

    @Override
    public boolean up(Schema schema) throws SQLException {
        return schema.createIfNotExists(Constants.VOTES_TABLE_NAME, table -> {
            table.Long("user_id").unsigned();
            table.String("expires_in", 128);

            table.setEngine(DatabaseEngine.InnoDB);
        });
    }

    @Override
    public boolean down(Schema schema) throws SQLException {
        return schema.dropIfExists(Constants.VOTES_TABLE_NAME);
    }
}
