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

package com.sable.commands.system;

import com.sable.Sable;
import com.sable.commands.CommandMessage;
import com.sable.commands.CommandPriority;
import com.sable.contracts.commands.Command;
import com.sable.contracts.commands.SystemCommand;
import com.sable.scheduler.tasks.ChangeGameTask;
import com.sable.utilities.ComparatorUtil;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SetStatusCommand extends SystemCommand {

    public SetStatusCommand(Sable avaire) {
        super(avaire);
    }

    @Override
    public String getName() {
        return "Set Status Command";
    }

    @Override
    public String getDescription() {
        return "Sets the status of the sable instance for all servers the sable is on, if no status is set the sable status will go back to cycling status from the config.";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Arrays.asList(
            "`:command <game>` - Sets the bots playing status to the given game.",
            "`:command <twitch url>` - The URL that the sable should be broadcasting."
        );
    }

    @Override
    public List<String> getExampleUsage() {
        return Collections.singletonList("`:command with some stuff`");
    }

    @Override
    public List<Class<? extends Command>> getRelations() {
        return Collections.singletonList(ResetStatusTypesCommand.class);
    }

    @Override
    public List<String> getTriggers() {
        return Arrays.asList("setstatus", "status");
    }

    @Override
    public CommandPriority getCommandPriority() {
        return CommandPriority.SYSTEM_ROLE;
    }

    @Override
    public boolean onCommand(CommandMessage context, String[] args) {
        if (args.length == 0) {
            context.makeInfo(
                "The sable status cycle has been re-enabled, the change game job can now change the sable status again."
            ).queue(newMessage -> ChangeGameTask.hasCustomStatus = false);

            return true;
        }

        if (ComparatorUtil.isFuzzyFalse(String.join(" ", args))) {
            ChangeGameTask.hasCustomStatus = true;
            avaire.getShardManager().setActivity(null);

            context.makeSuccess("The status message has been **disabled**")
                .queue();

            return true;
        }

        Activity game = parseGame(args);
        avaire.getShardManager().setActivity(game);

        context.makeSuccess("Changed status to **:type :status**")
            .set("type", getTypeAsString(game.getType()))
            .set("status", game.getName())
            .queue(newMessage -> ChangeGameTask.hasCustomStatus = true);

        return true;
    }

    private String getTypeAsString(Activity.ActivityType type) {
        switch (type) {
            case STREAMING:
                return "Streaming";

            case WATCHING:
                return "Watching";

            case LISTENING:
                return "Listening to";

            default:
                return "Playing";
        }
    }

    private Activity parseGame(String[] args) {
        if (Activity.isValidStreamingUrl(args[0])) {
            String url = args[0];
            String streamStatus = args.length > 1
                ? String.join(" ", Arrays.copyOfRange(args, 1, args.length))
                : "on Twitch.tv!";

            return Activity.streaming(streamStatus, url);
        }

        String status = String.join(" ", args);

        if (status.contains(":")) {
            String[] split = status.split(":");
            status = String.join(":", Arrays.copyOfRange(split, 1, split.length));
            switch (split[0].toLowerCase()) {
                case "listen":
                case "listening":
                    return Activity.listening(status);

                case "watch":
                case "watching":
                    return Activity.watching(status);

                case "play":
                case "playing":
                    return Activity.playing(status);

                case "stream":
                case "streaming":
                    String streamUrl = "https://www.twitch.tv/senither";
                    if (split.length > 1) {
                        String[] parts = split[1].split(" ");
                        streamUrl = "https://www.twitch.tv/" + parts[0];
                        status = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
                    }
                    return Activity.streaming(status, streamUrl);
            }
        }

        return Activity.playing(status);
    }
}
