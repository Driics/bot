package com.sable;

import java.io.File;

@SuppressWarnings("WeakerAccess")
public class Constants {

    public static final File STORAGE_PATH = new File("storage");

    // Database Tables
    public static final String GUILD_TABLE_NAME = "guilds";
    public static final String GUILD_TYPES_TABLE_NAME = "guild_types";
    public static final String STATISTICS_TABLE_NAME = "statistics";
    public static final String BLACKLIST_TABLE_NAME = "blacklists";
    public static final String PLAYER_EXPERIENCE_TABLE_NAME = "experiences";
    public static final String VOTES_TABLE_NAME = "votes";
    public static final String FEEDBACK_TABLE_NAME = "feedback";
    public static final String MUSIC_PLAYLIST_TABLE_NAME = "playlists";
    public static final String SHARDS_TABLE_NAME = "shards";
    public static final String LOG_TABLE_NAME = "logs";
    public static final String LOG_TYPES_TABLE_NAME = "log_types";
    public static final String REACTION_ROLES_TABLE_NAME = "reaction_roles";
    public static final String PURCHASES_TABLE_NAME = "purchases";
    public static final String MUTE_TABLE_NAME = "mutes";
    public static final String MUSIC_SEARCH_PROVIDERS_TABLE_NAME = "music_search_providers";
    public static final String MUSIC_SEARCH_CACHE_TABLE_NAME = "music_search_cache";
    public static final String INSTALLED_PLUGINS_TABLE_NAME = "installed_plugins";

    // Package Specific Information
    public static final String PACKAGE_MIGRATION_PATH = "com.sable.database.migrate";
    public static final String PACKAGE_SEEDER_PATH = "com.sable.database.seeder";
    public static final String PACKAGE_COMMAND_PATH = "com.sable.commands";
    public static final String PACKAGE_INTENTS_PATH = "com.sable.ai.dialogflow.intents";
    public static final String PACKAGE_JOB_PATH = "com.sable.scheduler";

    // Emojis
    public static final String EMOTE_ONLINE = "<:online:324986081378435072>";
    public static final String EMOTE_AWAY = "<:away:324986135346675712>";
    public static final String EMOTE_DND = "<:dnd:324986174806425610>";

    // Purchase Types
    public static final String RANK_BACKGROUND_PURCHASE_TYPE = "rank-background";

    // Audio Metadata
    public static final String AUDIO_HAS_SENT_NOW_PLAYING_METADATA = "has-sent-now-playing";

    // Command source link
    public static final String SOURCE_URI = "https://github.com/avaire/avaire/tree/master/src/main/java/com/avairebot/commands/%s/%s.java";
}
