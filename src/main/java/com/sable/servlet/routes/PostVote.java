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

package com.sable.servlet.routes;

import com.sable.Sable;
import com.sable.contracts.metrics.SparkRoute;
import com.sable.metrics.Metrics;
import com.sable.time.Carbon;
import com.sable.vote.VoteCacheEntity;
import com.sable.vote.VoteMetricType;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

public class PostVote extends SparkRoute {

    private static final Logger log = LoggerFactory.getLogger(PostVote.class);

    @Override
    public Object handle(Request request, Response response) throws Exception {
        log.info("Vote route has been hit by {} with the body: {}",
            request.ip(), request.body()
        );

        if (!hasValidAuthorizationHeader(request)) {
            log.warn("Unauthorized request, missing or invalid \"Authorization\" header give.");
            return buildResponse(response, 401, "Unauthorized request, missing or invalid \"Authorization\" header give.");
        }

        VoteRequest voteRequest = Sable.gson.fromJson(request.body(), VoteRequest.class);

        if (!isValidVoteRequest(voteRequest)) {
            log.warn("Bad request, invalid JSON data given to justify a upvote request.");
            return buildResponse(response, 400, "Bad request, invalid JSON data given to justify a upvote request.");
        }

        Sable.getInstance().getVoteManager().registerVoteFor(
            Long.valueOf(voteRequest.user),
            voteRequest.isWeekend ? 2 : 1
        );

        VoteCacheEntity voteEntity = Sable.getInstance().getVoteManager()
            .getVoteEntityWithFallback(Long.valueOf(voteRequest.user));
        voteEntity.setCarbon(Carbon.now().addHours(12));

        Metrics.dblVotes.labels(VoteMetricType.WEBHOOK.getName()).inc();

        User userById = Sable.getInstance().getShardManager().getUserById(voteRequest.user);
        if (userById == null || userById.isBot()) {
            log.info("Vote has been registered by {} [No servers is shared with this user]",
                voteRequest.user
            );

            return buildResponse(response, 200, "Vote registered, thanks for voting!");
        }

        log.info("Vote has been registered by {} ({})",
            userById.getAsTag(), userById.getId()
        );

        if (!voteEntity.isOptIn()) {
            return buildResponse(response, 200, "Vote registered, thanks for voting!");
        }

        Sable.getInstance().getVoteManager().getMessenger().SendThanksForVotingMessageInDM(userById, voteEntity.getVotePoints());

        return buildResponse(response, 200, "Vote registered, thanks for voting!");
    }

    private boolean isValidVoteRequest(VoteRequest request) {
        if (request == null) {
            return false;
        }

        if (request.bot == null || request.user == null || request.type == null) {
            return false;
        }

        if (!request.bot.equals(Sable.getInstance().getSelfUser().getId())) {
            return false;
        }

        return request.type.equalsIgnoreCase("upvote");
    }

    private class VoteRequest {

        private String bot;
        private String user;
        private String type;
        private boolean isWeekend;
    }
}
