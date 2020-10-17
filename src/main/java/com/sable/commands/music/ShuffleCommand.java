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

package com.sable.commands.music;

import com.sable.Sable;
import com.sable.audio.AudioHandler;
import com.sable.audio.AudioTrackContainer;
import com.sable.audio.GuildMusicManager;
import com.sable.commands.CommandMessage;
import com.sable.contracts.commands.Command;
import com.sable.contracts.commands.CommandGroup;
import com.sable.contracts.commands.CommandGroups;
import com.sable.utilities.NumberUtil;
import com.sable.utilities.RestActionUtil;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ShuffleCommand extends Command {

    public ShuffleCommand(Sable avaire) {
        super(avaire, false);
    }

    @Override
    public String getName() {
        return "Shuffle Command";
    }

    @Override
    public String getDescription() {
        return "Shuffles the music queue, mixing the songs up in random order.";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Collections.singletonList("`:command` - Shuffles all the songs currently in the queue.");
    }

    @Override
    public List<String> getTriggers() {
        return Collections.singletonList("shuffle");
    }

    @Override
    public List<String> getMiddleware() {
        return Arrays.asList(
            "hasDJLevel:normal",
            "throttle:guild,2,4",
            "musicChannel"
        );
    }

    @Nonnull
    @Override
    public List<CommandGroup> getGroups() {
        return Collections.singletonList(CommandGroups.MUSIC_QUEUE);
    }

    @Override
    public boolean onCommand(CommandMessage context, String[] args) {
        GuildMusicManager musicManager = AudioHandler.getDefaultAudioHandler().getGuildAudioPlayer(context.getGuild());

        if (!musicManager.isReady() || musicManager.getScheduler().getQueue().isEmpty()) {
            return sendErrorMessage(context, context.i18n("error", generateCommandPrefix(context.getMessage())));
        }

        List<AudioTrackContainer> queue = new ArrayList<>();
        musicManager.getScheduler().getQueue().drainTo(queue);

        Collections.shuffle(queue);
        musicManager.getScheduler().getQueue().addAll(queue);

        context.makeSuccess(context.i18n("success"))
            .set("amount", NumberUtil.formatNicely(queue.size()))
            .queue(message -> message.delete().queueAfter(5, TimeUnit.MINUTES, null, RestActionUtil.ignore));

        return true;
    }
}
