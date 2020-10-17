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

package com.sable.scheduler.jobs.generic;

import com.sable.Sable;
import com.sable.contracts.scheduler.Job;
import com.sable.scheduler.tasks.*;

import java.util.concurrent.TimeUnit;

public class RunEveryMinuteJob extends Job {

    private final ChangeGameTask changeGameTask = new ChangeGameTask();
    private final DrainMuteQueueTask drainMuteQueueTask = new DrainMuteQueueTask();
    private final GarbageCollectorTask garbageCollectorTask = new GarbageCollectorTask();
    private final SyncBlacklistMetricsTask syncBlacklistMetricsTask = new SyncBlacklistMetricsTask();
    private final ResetRespectStatisticsTask resetRespectStatisticsTask = new ResetRespectStatisticsTask();
    private final DeleteExpiredBlacklistEntitiesTask deleteExpiredBlacklistEntitiesTask = new DeleteExpiredBlacklistEntitiesTask();
    private final UpdateWebsocketHeartbeatMetricsTask updateWebsocketHeartbeatMetricsTask = new UpdateWebsocketHeartbeatMetricsTask();
    private final SyncValidVoteRequestsWithMetricsTask syncValidVoteRequestsWithMetricsTask = new SyncValidVoteRequestsWithMetricsTask();
    private final SyncPlayerExperienceWithDatabaseTask syncPlayerExperienceWithDatabaseTask = new SyncPlayerExperienceWithDatabaseTask();
    private final SyncPlayerUpdateReferencesWithDatabaseTask syncPlayerUpdateReferencesWithDatabaseTask = new SyncPlayerUpdateReferencesWithDatabaseTask();

    public RunEveryMinuteJob(Sable avaire) {
        super(avaire, 0, 1, TimeUnit.MINUTES);
    }

    @Override
    public void run() {
        handleTask(
            changeGameTask,
            drainMuteQueueTask,
            garbageCollectorTask,
            syncBlacklistMetricsTask,
            resetRespectStatisticsTask,
            deleteExpiredBlacklistEntitiesTask,
            updateWebsocketHeartbeatMetricsTask,
            syncValidVoteRequestsWithMetricsTask,
            syncPlayerExperienceWithDatabaseTask,
            syncPlayerUpdateReferencesWithDatabaseTask
        );
    }
}
