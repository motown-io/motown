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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;

import static org.hamcrest.core.Is.is;

public class MinimalSessionStateMachineTest {

    Logger logger;
    Appender<ILoggingEvent> mockAppender;
    ArgumentCaptor<LoggingEvent> captorLoggingEvent;

    @Before
    public void setup() {
        logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        mockAppender = Mockito.mock(GenericAppender.class);
        logger.addAppender(mockAppender);
        captorLoggingEvent = ArgumentCaptor.forClass(LoggingEvent.class);
    }

    @After
    public void teardown() {
        logger.detachAppender(mockAppender);
    }

    @Test
    public void shouldStartInReadyState() {
        MinimalSessionStateMachine sessionStateMachine = new MinimalSessionStateMachine();
        Assert.assertThat(sessionStateMachine.hasStateReady(), is(true));
    }

    @Test
    public void readyStateShouldChangeToStartRequestedWhenUserRequestsStart() {
        MinimalSessionStateMachine sessionStateMachine = new MinimalSessionStateMachine();
        sessionStateMachine.eventStartRequest();
        Assert.assertThat(sessionStateMachine.hasStateStartTransactionRequested(), is(true));
    }

    @Test
    public void shouldLogStartTransactionWhenUserRequestsStart() {
        MinimalSessionStateMachine sessionStateMachine = new MinimalSessionStateMachine();
        sessionStateMachine.eventStartRequest();
        verifyLogging(Level.INFO, "handleStartTxRequestedStateEntrance", 1);
    }

    @Test
    public void startRequestedStateShouldChangeToTransactionRunningWhenDestinationConfirmsStart() {
        MinimalSessionStateMachine sessionStateMachine = getSessionWithStartTransactionRequestedState();
        sessionStateMachine.eventStartOk();
        Assert.assertThat(sessionStateMachine.hasStateTransactionRunning(), is(true));
    }

    @Test
    public void shouldLogTransactionStartedWhenDestinationConfirmsStart() {
        MinimalSessionStateMachine sessionStateMachine = getSessionWithStartTransactionRequestedState();
        sessionStateMachine.eventStartOk();
        verifyLogging(Level.INFO, "handleTxRunningStateEntrance", 1);
    }

    @Test
    public void transactionRunningStateShouldChangeToStopRequestedWhenUserRequestsStop() {
        MinimalSessionStateMachine sessionStateMachine = getSessionWithTransactionRunningState();
        sessionStateMachine.eventStopRequest();
        Assert.assertThat(sessionStateMachine.hasStateStopTransactionRequested(), is(true));
    }

    @Test
    public void shouldLogStopTransactionWhenUserRequestsStop() {
        MinimalSessionStateMachine sessionStateMachine = getSessionWithTransactionRunningState();
        sessionStateMachine.eventStopRequest();
        verifyLogging(Level.INFO, "handleStopTxRequestedStateEntrance", 1);
    }

    @Test
    public void stopRequestedStateShouldChangeToTransactionFinishedWhenDestinationConfirmsStop() {
        MinimalSessionStateMachine sessionStateMachine = getSessionWithStopTransactionRequestedState();
        sessionStateMachine.eventStopOk();
        Assert.assertThat(sessionStateMachine.hasStateTransactionFinished(), is(true));
    }

    @Test
    public void shouldLogTransactionFinishedWhenDestinationConfirmsStop() {
        MinimalSessionStateMachine sessionStateMachine = getSessionWithStopTransactionRequestedState();
        sessionStateMachine.eventStopOk();
        verifyLogging(Level.INFO, "handleTxFinishedStateEntrance", 1);
    }

    @Test
    public void startRequestedStateShouldChangeToReadyWhenDestinationSendsStartError() {
        MinimalSessionStateMachine sessionStateMachine = getSessionWithStartTransactionRequestedState();
        sessionStateMachine.eventStartError();
        Assert.assertThat(sessionStateMachine.hasStateReady(), is(true));
    }

    @Test
    public void shouldLogStartTransactionErrorWhenDestinationSendsStartError() {
        MinimalSessionStateMachine sessionStateMachine = getSessionWithStartTransactionRequestedState();
        sessionStateMachine.eventStartError();
        verifyLogging(Level.WARN, "handleStartTxError", 1);
    }

    @Test
    public void stopRequestedStateShouldChangeToTransactionRunningWhenDestinationSendsStopError() {
        MinimalSessionStateMachine sessionStateMachine = getSessionWithStopTransactionRequestedState();
        sessionStateMachine.eventStopError();
        Assert.assertThat(sessionStateMachine.hasStateTransactionRunning(), is(true));
    }

    @Test
    public void shouldLogStopTransactionErrorWhenDestinationSendsStopError() {
        MinimalSessionStateMachine sessionStateMachine = getSessionWithStopTransactionRequestedState();
        sessionStateMachine.eventStopError();
        verifyLogging(Level.WARN, "handleStopTxError", 1);
    }

    @Test
    public void shouldLogUnexpectedEventsInReadyState() {
        MinimalSessionStateMachine sessionStateMachine = new MinimalSessionStateMachine();
        sessionStateMachine.eventStopRequest();
        sessionStateMachine.eventStartOk();
        sessionStateMachine.eventStopOk();
        sessionStateMachine.eventStartError();
        sessionStateMachine.eventStopError();
        verifyLogging(Level.WARN, "handleUnExpectedEvent", 5);
    }

    @Test
    public void shouldLogUnexpectedEventsInStartTransactionRequested() {
        MinimalSessionStateMachine sessionStateMachine = getSessionWithStartTransactionRequestedState();
        sessionStateMachine.eventStartRequest();
        sessionStateMachine.eventStopRequest();
        sessionStateMachine.eventStopOk();
        sessionStateMachine.eventStopError();
        verifyLogging(Level.WARN, "handleUnExpectedEvent", 4);
    }

    @Test
    public void shouldLogUnexpectedEventsInTransactionRunning() {
        MinimalSessionStateMachine sessionStateMachine = getSessionWithTransactionRunningState();
        sessionStateMachine.eventStartRequest();
        sessionStateMachine.eventStartOk();
        sessionStateMachine.eventStopOk();
        sessionStateMachine.eventStartError();
        sessionStateMachine.eventStopError();
        verifyLogging(Level.WARN, "handleUnExpectedEvent", 5);
    }

    @Test
    public void shouldLogUnexpectedEventsInStopTransactionRequested() {
        MinimalSessionStateMachine sessionStateMachine = getSessionWithStopTransactionRequestedState();
        sessionStateMachine.eventStartRequest();
        sessionStateMachine.eventStopRequest();
        sessionStateMachine.eventStartOk();
        sessionStateMachine.eventStartError();
        verifyLogging(Level.WARN, "handleUnExpectedEvent", 4);
    }

    @Test
    public void shouldLogUnexpectedEventsInTransactionFinished() {
        MinimalSessionStateMachine sessionStateMachine = getSessionWithTransactionFinishedState();
        sessionStateMachine.eventStartRequest();
        sessionStateMachine.eventStopRequest();
        sessionStateMachine.eventStartOk();
        sessionStateMachine.eventStopOk();
        sessionStateMachine.eventStartError();
        sessionStateMachine.eventStopError();
        verifyLogging(Level.WARN, "handleUnExpectedEvent", 6);
    }

    private void verifyLogging(Level expectedLogLevel, String expectedMessage, int minimalNrOfCalls) {
        Mockito.verify(mockAppender, Mockito.atLeast(minimalNrOfCalls)).doAppend(captorLoggingEvent.capture());
        LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        Assert.assertThat(loggingEvent.getLevel(), is(expectedLogLevel));
        Assert.assertTrue(loggingEvent.getMessage().contains(expectedMessage));
    }

    private MinimalSessionStateMachine getSessionWithReadyState() {
        return new MinimalSessionStateMachine();
    }

    private MinimalSessionStateMachine getSessionWithStartTransactionRequestedState() {
        MinimalSessionStateMachine result = getSessionWithReadyState();
        result.eventStartRequest();
        return result;
    }

    private MinimalSessionStateMachine getSessionWithTransactionRunningState() {
        MinimalSessionStateMachine result = getSessionWithStartTransactionRequestedState();
        result.eventStartOk();
        return result;
    }

    private MinimalSessionStateMachine getSessionWithStopTransactionRequestedState() {
        MinimalSessionStateMachine result = getSessionWithTransactionRunningState();
        result.eventStopRequest();
        return result;
    }

    private MinimalSessionStateMachine getSessionWithTransactionFinishedState() {
        MinimalSessionStateMachine result = getSessionWithStopTransactionRequestedState();
        result.eventStopOk();
        return result;
    }

    // Interface to get rid of the unchecked assignment warnings when mocking a generic Type in a test
    interface GenericAppender extends Appender<ILoggingEvent> {
    }
}
