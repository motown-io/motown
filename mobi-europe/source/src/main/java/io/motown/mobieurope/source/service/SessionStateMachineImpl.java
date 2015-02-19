/**
 * Copyright (C) 2013 Motown.IO (info@motown.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.motown.mobieurope.source.service;

import io.motown.mobieurope.shared.session.SessionStateMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionStateMachineImpl extends SessionStateMachine {

    private static final Logger LOG = LoggerFactory.getLogger(SessionStateMachineImpl.class);

    @Override
    public void handleUnExpectedEvent(String state, String event) {
        LOG.warn("Unexpected Event in state: " + state + ". Event: " + event);
    }

    @Override
    protected void handleStartTxRequestedStateEntrance() {
        LOG.info("Implement Request START Transaction.");
    }

    @Override
    protected void handleTxRunningStateEntrance() {
        LOG.info("Start Transaction OK Notification");
    }

    @Override
    protected void handleStopTxRequestedStateEntrance() {
        LOG.info("Implement Request STOP Transaction.");
    }

    @Override
    protected void handleTxFinishedStateEntrance() {
        LOG.info("Transaction Finished Notification");
    }

    @Override
    protected void handleStopTxError() {
        LOG.warn("Stop Transaction Error Notification");
    }

    @Override
    protected void handleStartTxError() {
        LOG.warn("Start Transaction Error Notification");
    }
}
