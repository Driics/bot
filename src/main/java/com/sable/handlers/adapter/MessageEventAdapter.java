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

package com.sable.handlers.adapter;

import com.sable.AppInfo;
import com.sable.Sable;
import com.sable.Constants;
import com.sable.commands.CommandContainer;
import com.sable.commands.CommandHandler;
import com.sable.commands.help.HelpCommand;
import com.sable.contracts.handlers.EventAdapter;
import com.sable.database.collection.Collection;
import com.sable.database.collection.DataRow;
import com.sable.database.controllers.GuildController;
import com.sable.database.controllers.PlayerController;
import com.sable.database.controllers.ReactionController;
import com.sable.database.query.QueryBuilder;
import com.sable.database.transformers.ChannelTransformer;
import com.sable.database.transformers.GuildTransformer;
import com.sable.factories.MessageFactory;
import com.sable.handlers.DatabaseEventHolder;
import com.sable.language.I18n;
import com.sable.middleware.MiddlewareStack;
import com.avairebot.shared.DiscordConstants;
import com.sable.utilities.ArrayUtil;
import com.sable.utilities.RestActionUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class MessageEventAdapter extends EventAdapter {

    public static final Set<Long> hasReceivedInfoMessageInTheLastMinute = new HashSet<>();

    private static final ExecutorService commandService = Executors.newCachedThreadPool(
        new ThreadFactoryBuilder()
            .setNameFormat("sable-command-thread-%d")
            .build()
    );

    private static final Logger log = LoggerFactory.getLogger(MessageEventAdapter.class);
    private static final Pattern userRegEX = Pattern.compile("<@(!|)+[0-9]{16,}+>", Pattern.CASE_INSENSITIVE);
    private static final String mentionMessage = String.join("\n", Arrays.asList(
        "Hi there! I'm **%s**, a multipurpose Discord sable built for fun by %s!",
        "You can see what commands I have by using the `%s` command.",
        "",
        "I am currently running **Sable v%s**",
        "",
        "You can find all of my source code on github:",
        "https://github.com/avaire/avaire",
        "",
        "If you like me please vote for Sable to help me grow:",
        "https://discordbots.org/bot/avaire/vote"
    ));

    /**
     * Instantiates the event adapter and sets the sable class instance.
     *
     * @param avaire The Sable application class instance.
     */
    public MessageEventAdapter(Sable avaire) {
        super(avaire);
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        if (!isValidMessage(event.getAuthor())) {
            return;
        }

        if (event.getChannelType().isGuild() && !event.getTextChannel().canTalk()) {
            return;
        }

        if (sable.getBlacklist().isBlacklisted(event.getMessage())) {
            return;
        }

        loadDatabasePropertiesIntoMemory(event).thenAccept(databaseEventHolder -> {
            if (databaseEventHolder.getGuild() != null && databaseEventHolder.getPlayer() != null) {
                sable.getLevelManager().rewardPlayer(event, databaseEventHolder.getGuild(), databaseEventHolder.getPlayer());
            }

            CommandContainer container = CommandHandler.getCommand(sable, event.getMessage(), event.getMessage().getContentRaw());
            if (container != null && canExecuteCommand(event, container)) {
                invokeMiddlewareStack(new MiddlewareStack(event.getMessage(), container, databaseEventHolder));
                return;
            }

            if (isMentionableAction(event)) {
                container = CommandHandler.getLazyCommand(ArrayUtil.toArguments(event.getMessage().getContentRaw())[1]);
                if (container != null && canExecuteCommand(event, container)) {
                    invokeMiddlewareStack(new MiddlewareStack(event.getMessage(), container, databaseEventHolder, true));
                    return;
                }

                if (sable.getIntelligenceManager().isEnabled()) {
                    if (isAIEnabledForChannel(event, databaseEventHolder.getGuild())) {
                        sable.getIntelligenceManager().handleRequest(
                            event.getMessage(), databaseEventHolder
                        );
                    }
                    return;
                }
            }

            if (isSingleBotMention(event.getMessage().getContentRaw().trim())) {
                sendTagInformationMessage(event);
                return;
            }

            if (!event.getChannelType().isGuild()) {
                sendInformationMessage(event);
            }
        });
    }

    private boolean isValidMessage(User author) {
        return !author.isBot() || author.getIdLong() == DiscordConstants.SENITHER_BOT_ID;
    }

    private void invokeMiddlewareStack(MiddlewareStack stack) {
        commandService.submit(stack::next);
    }

    private boolean canExecuteCommand(MessageReceivedEvent event, CommandContainer container) {
        if (!container.getCommand().isAllowedInDM() && !event.getChannelType().isGuild()) {
            MessageFactory.makeWarning(event.getMessage(), ":warning: You can not use this command in direct messages!").queue();
            return false;
        }
        return true;
    }

    private boolean isMentionableAction(MessageReceivedEvent event) {
        if (!event.getMessage().isMentioned(sable.getSelfUser())) {
            return false;
        }

        String[] args = event.getMessage().getContentRaw().split(" ");
        return args.length >= 2 &&
            userRegEX.matcher(args[0]).matches() &&
            event.getMessage().getMentionedUsers().get(0).getId().equals(sable.getSelfUser().getId());

    }

    private boolean isSingleBotMention(String rawContent) {
        return rawContent.equals("<@" + sable.getSelfUser().getId() + ">") ||
            rawContent.equals("<@!" + sable.getSelfUser().getId() + ">");
    }

    private boolean isAIEnabledForChannel(MessageReceivedEvent event, GuildTransformer transformer) {
        if (transformer == null) {
            return true;
        }

        ChannelTransformer channel = transformer.getChannel(event.getChannel().getId());
        return channel == null || channel.getAI().isEnabled();
    }

    private void sendTagInformationMessage(MessageReceivedEvent event) {
        String author = "**Senither#0001**";
        if (event.getMessage().getChannelType().isGuild() && event.getGuild().getMemberById(88739639380172800L) != null) {
            author = "<@88739639380172800>";
        }

        MessageFactory.makeEmbeddedMessage(event.getMessage().getChannel(), Color.decode("#E91E63"), String.format(mentionMessage,
            sable.getSelfUser().getName(),
            author,
            CommandHandler.getLazyCommand("help").getCommand().generateCommandTrigger(event.getMessage()),
            AppInfo.getAppInfo().version
        ))
            .setFooter("This message will be automatically deleted in one minute.")
            .queue(message -> message.delete().queueAfter(1, TimeUnit.MINUTES, null, RestActionUtil.ignore));
    }

    @SuppressWarnings("ConstantConditions")
    private void sendInformationMessage(MessageReceivedEvent event) {
        log.info("Private message received from user(ID: {}) that does not match any commands!",
            event.getAuthor().getId()
        );

        if (hasReceivedInfoMessageInTheLastMinute.contains(event.getAuthor().getIdLong())) {
            return;
        }

        hasReceivedInfoMessageInTheLastMinute.add(event.getAuthor().getIdLong());

        try {
            ArrayList<String> strings = new ArrayList<>();
            strings.addAll(Arrays.asList(
                "To invite me to your server, use this link:",
                "*:oauth*",
                "",
                "You can use `{0}help` to see a list of all the categories of commands.",
                "You can use `{0}help category` to see a list of commands for that category.",
                "For specific command help, use `{0}help command` (for example `{0}help {1}{2}`,\n`{0}help {2}` also works)"
            ));

            if (sable.getIntelligenceManager().isEnabled()) {
                strings.add("\nYou can tag me in a message with <@:botId> to send me a message that I should process using my AI.");
            }

            strings.add("\n**Full list of commands**\n*https://avairebot.com/commands*");
            strings.add("\nSable Support Server:\n*https://avairebot.com/support*");

            CommandContainer commandContainer = CommandHandler.getCommands().stream()
                .filter(container -> !container.getCategory().isGlobalOrSystem())
                .findAny()
                .get();

            MessageFactory.makeEmbeddedMessage(event.getMessage(), Color.decode("#E91E63"), I18n.format(
                String.join("\n", strings),
                CommandHandler.getCommand(HelpCommand.class).getCategory().getPrefix(event.getMessage()),
                commandContainer.getCategory().getPrefix(event.getMessage()),
                commandContainer.getTriggers().iterator().next()
            ))
                .set("oauth", sable.getConfig().getString("discord.oauth"))
                .set("botId", sable.getSelfUser().getId())
                .queue();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private CompletableFuture<DatabaseEventHolder> loadDatabasePropertiesIntoMemory(final MessageReceivedEvent event) {
        return CompletableFuture.supplyAsync(() -> {
            if (!event.getChannelType().isGuild()) {
                return new DatabaseEventHolder(null, null);
            }

            GuildTransformer guild = GuildController.fetchGuild(sable, event.getMessage());

            if (guild == null || !guild.isLevels() || event.getAuthor().isBot()) {
                return new DatabaseEventHolder(guild, null);
            }
            return new DatabaseEventHolder(guild, PlayerController.fetchPlayer(sable, event.getMessage()));
        });
    }

    public void onMessageDelete(TextChannel channel, List<String> messageIds) {
        Collection reactions = ReactionController.fetchReactions(sable, channel.getGuild());
        if (reactions == null || reactions.isEmpty()) {
            return;
        }

        List<String> removedReactionMessageIds = new ArrayList<>();
        for (DataRow row : reactions) {
            for (String messageId : messageIds) {
                if (Objects.equals(row.getString("message_id"), messageId)) {
                    removedReactionMessageIds.add(messageId);
                }
            }
        }

        if (removedReactionMessageIds.isEmpty()) {
            return;
        }

        QueryBuilder builder = sable.getDatabase().newQueryBuilder(Constants.REACTION_ROLES_TABLE_NAME);
        for (String messageId : removedReactionMessageIds) {
            builder.orWhere("message_id", messageId);
        }

        try {
            builder.delete();

            ReactionController.forgetCache(
                channel.getGuild().getIdLong()
            );
        } catch (SQLException e) {
            log.error("Failed to delete {} reaction messages for the guild with an ID of {}",
                removedReactionMessageIds.size(), channel.getGuild().getId(), e
            );
        }
    }

    public void onMessageUpdate(MessageUpdateEvent event) {
        Collection reactions = ReactionController.fetchReactions(sable, event.getGuild());
        if (reactions == null) {
            return;
        }

        if (reactions.where("message_id", event.getMessage().getId()).isEmpty()) {
            return;
        }

        try {
            String messageContent = event.getMessage().getContentStripped();
            if (messageContent.trim().length() == 0 && !event.getMessage().getEmbeds().isEmpty()) {
                messageContent = event.getMessage().getEmbeds().get(0).getDescription();
            }

            String finalMessageContent = messageContent;
            sable.getDatabase().newQueryBuilder(Constants.REACTION_ROLES_TABLE_NAME)
                .where("guild_id", event.getGuild().getId())
                .where("message_id", event.getMessage().getId())
                .update(statement -> {
                    statement.set("snippet", finalMessageContent.substring(
                        0, Math.min(finalMessageContent.length(), 64)
                    ), true);
                });

            ReactionController.forgetCache(event.getGuild().getIdLong());
        } catch (SQLException e) {
            log.error("Failed to update the reaction role message with a message ID of {}, error: {}",
                event.getMessage().getId(), e.getMessage(), e
            );
        }
    }
}
