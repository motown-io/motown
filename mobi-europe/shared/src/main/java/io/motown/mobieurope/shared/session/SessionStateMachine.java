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

import java.io.Serializable;

public abstract class SessionStateMachine implements Serializable {

    private State state = State.Ready;
    private long lastTimeStateChanged = System.currentTimeMillis();

    private void setState(State s) {
        state = s;
        updateLastTimeStateChanged();
    }

    private void updateLastTimeStateChanged() {
        this.lastTimeStateChanged = System.currentTimeMillis();
    }

    public long getLastTimeStateChanged() {
        return lastTimeStateChanged;
    }

    public void eventStartRequest() {
        handleEvent(Event.StartRequest);
    }

    public void eventStartOk() {
        handleEvent(Event.StartTxOk);
    }

    public void eventStopRequest() {
        handleEvent(Event.StopRequest);
    }

    public void eventStopOk() {
        handleEvent(Event.StopTxOk);
    }

    public void eventStartError() {
        handleEvent(Event.StartTxError);
    }

    public void eventStopError() {
        handleEvent(Event.StopTxError);
    }

    private void handleEvent(Event event) {
        switch (state) {
            case Ready:
                switch (event) {
                    case StartRequest:
                        setState(State.StartTxRequested);
                        handleStartTxRequestedStateEntrance();
                        break;
                    default:
                        handleUnExpectedEvent(state.name(), event.name());
                        break;
                }
                break;
            case StartTxRequested:
                switch (event) {
                    case StartTxOk:
                        setState(State.TxRunning);
                        handleTxRunningStateEntrance();
                        break;
                    case StartTxError:
                        setState(State.Ready);
                        handleStartTxError();
                        break;
                    default:
                        handleUnExpectedEvent(state.name(), event.name());
                        break;
                }
                break;
            case StopTxRequested:
                switch (event) {
                    case StopTxOk:
                        setState(State.TxFinished);
                        handleTxFinishedStateEntrance();
                        break;
                    case StopTxError:
                        setState(State.TxRunning);
                        handleStopTxError();
                        break;
                    default:
                        handleUnExpectedEvent(state.name(), event.name());
                        break;
                }
                break;
            case TxFinished:
                switch (event) {
                    default:
                        handleUnExpectedEvent(state.name(), event.name());
                        break;
                }
                break;
            case TxRunning:
                switch (event) {
                    case StopRequest:
                        setState(State.StopTxRequested);
                        handleStopTxRequestedStateEntrance();
                        break;
                    default:
                        handleUnExpectedEvent(state.name(), event.name());
                        break;
                }
                break;
        }
    }

    protected abstract void handleStartTxRequestedStateEntrance();

    protected abstract void handleTxRunningStateEntrance();

    protected abstract void handleStopTxRequestedStateEntrance();

    protected abstract void handleTxFinishedStateEntrance();

    protected abstract void handleStartTxError();

    protected abstract void handleStopTxError();

    protected abstract void handleUnExpectedEvent(String state, String event);

    public boolean hasStateReady() {
        return state.equals(State.Ready);
    }

    public boolean hasStateStartTransactionRequested() {
        return state.equals(State.StartTxRequested);
    }

    public boolean hasStateTransactionRunning() {
        return state.equals(State.TxRunning);
    }

    public boolean hasStateStopTransactionRequested() {
        return state.equals(State.StopTxRequested);
    }

    public boolean hasStateTransactionFinished() {
        return state.equals(State.TxFinished);
    }

    private enum State {Ready, StartTxRequested, TxRunning, StopTxRequested, TxFinished}

    private enum Event {StartRequest, StartTxOk, StartTxError, StopRequest, StopTxOk, StopTxError}
}
