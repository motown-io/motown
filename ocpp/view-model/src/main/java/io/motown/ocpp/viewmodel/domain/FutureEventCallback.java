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
package io.motown.ocpp.viewmodel.domain;

import io.motown.domain.utils.axon.EventCallback;

import java.util.concurrent.*;

/**
 * Defines an interface that can be used when using
 * {@link io.motown.domain.utils.axon.EventWaitingGateway#sendAndWaitForEvent}.
 *
 * @param <T> The result type returned by future's get method
 */
public abstract class FutureEventCallback<T> implements Future<T>, EventCallback {

    private volatile T result = null;
    private volatile boolean cancelled = false;
    private final CountDownLatch countDownLatch;

    public FutureEventCallback() {
        countDownLatch = new CountDownLatch(1);
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        countDownLatch.await();
        return result;
    }

    @Override
    public T get(final long timeout, final TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        countDownLatch.await(timeout, unit);
        return result;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public boolean isDone() {
        return countDownLatch.getCount() == 0;
    }

    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        if (isDone()) {
            return false;
        } else {
            countDownLatch.countDown();
            cancelled = true;
            return !isDone();
        }
    }

    public void setResult(T result) {
        this.result = result;
    }

    public void countDownLatch() {
        countDownLatch.countDown();
    }
}
