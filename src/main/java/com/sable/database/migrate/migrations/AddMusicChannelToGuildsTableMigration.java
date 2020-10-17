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
import com.sable.database.connections.MySQL;
import com.sable.database.schema.Schema;

import java.sql.SQLException;

public class AddMusicChannelToGuildsTableMigration implements Migration {

    @Override
    public String created_at() {
        return "Thu, May 17, 2018 1:27 PM";
    }

    @Override
    public boolean up(Schema schema) throws SQLException {
        if (hasColumns(schema)) {
            return true;
        }

        if (schema.getDbm().getConnection() instanceof MySQL) {
            schema.getDbm().queryUpdate(String.format(
                "ALTER TABLE `%s` ADD `music_channel_text` VARCHAR(64) NULL DEFAULT NULL AFTER `level_channel`, "
                    + "ADD `music_channel_voice` VARCHAR(64) NULL DEFAULT NULL AFTER `music_channel_text`;",
                Constants.GUILD_TABLE_NAME
            ));
        } else {
            schema.getDbm().queryUpdate(String.format(
                "ALTER TABLE `%s` ADD `music_channel_text` VARCHAR(64) NULL DEFAULT NULL;",
                Constants.GUILD_TABLE_NAME
            ));

            schema.getDbm().queryUpdate(String.format(
                "ALTER TABLE `%s` ADD `music_channel_voice` VARCHAR(64) NULL DEFAULT NULL;",
                Constants.GUILD_TABLE_NAME
            ));
        }

        return true;
    }

    @Override
    public boolean down(Schema schema) throws SQLException {
        if (!hasColumns(schema)) {
            return true;
        }

        schema.getDbm().queryUpdate(String.format(
            "ALTER TABLE `%s` DROP `music_channel_text`, DROP `music_channel_voice`;",
            Constants.GUILD_TABLE_NAME
        ));

        return true;
    }

    private boolean hasColumns(Schema schema) throws SQLException {
        return schema.hasColumn(Constants.GUILD_TABLE_NAME, "music_channel_text")
            && schema.hasColumn(Constants.GUILD_TABLE_NAME, "music_channel_voice");
    }
}
