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
package io.motown.mobieurope.shared.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinimalSessionStateMachine extends SessionStateMachine {

    private static final Logger LOG = LoggerFactory.getLogger(MinimalSessionStateMachine.class);

    @Override
    protected void handleStartTxRequestedStateEntrance() {
        LOG.info("handleStartTxRequestedStateEntrance called");
    }

    @Override
    protected void handleTxRunningStateEntrance() {
        LOG.info("handleTxRunningStateEntrance called");
    }

    @Override
    protected void handleStopTxRequestedStateEntrance() {
        LOG.info("handleStopTxRequestedStateEntrance called");
    }

    @Override
    protected void handleTxFinishedStateEntrance() {
        LOG.info("handleTxFinishedStateEntrance called");
    }

    @Override
    protected void handleStartTxError() {
        LOG.warn("handleStartTxError called");
    }

    @Override
    protected void handleStopTxError() {
        LOG.warn("handleStopTxError called");
    }

    @Override
    protected void handleUnExpectedEvent(String state, String event) {
        LOG.warn("handleUnExpectedEvent called in state: " + state + ". Event: " + event);
    }
}
