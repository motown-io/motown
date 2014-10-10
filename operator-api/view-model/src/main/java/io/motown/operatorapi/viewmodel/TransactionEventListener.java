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
package io.motown.operatorapi.viewmodel;

import io.motown.domain.api.chargingstation.*;
import io.motown.operatorapi.viewmodel.persistence.entities.Transaction;
import io.motown.operatorapi.viewmodel.persistence.repositories.TransactionRepository;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class TransactionEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionEventListener.class);

    private TransactionRepository repository;

    public void setRepository(TransactionRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void handle(TransactionStartedEvent event) {
        LOG.debug("TransactionStartedEvent for [{}] received!", event.getChargingStationId());

        Transaction transaction = new Transaction(event.getChargingStationId().getId(), event.getTransactionId().getId(),
                event.getStartTransactionInfo().getEvseId(), event.getStartTransactionInfo().getIdentifyingToken().getToken(),
                event.getStartTransactionInfo().getMeterStart(), event.getStartTransactionInfo().getTimestamp());
        repository.createOrUpdate(transaction);
    }

    @EventHandler
    public void handle(TransactionStoppedEvent event) {
        LOG.debug("TransactionStoppedEvent for [{}] received!", event.getChargingStationId());

        Transaction transaction = repository.findByTransactionId(event.getTransactionId().getId());

        if (transaction != null) {
            transaction.setMeterStop(event.getMeterStop());
            transaction.setStoppedTimestamp(event.getTimestamp());
            repository.createOrUpdate(transaction);
        }
    }

    @EventHandler
    public void handle(ChargingStationSentMeterValuesEvent event) {
        LOG.debug("ChargingStationSentMeterValuesEvent for [{}] received!", event.getChargingStationId());

        checkNotNull(event);

        if (event.getTransactionId() != null) {
            Transaction transaction = repository.findByTransactionId(event.getTransactionId().getId());

            if (transaction != null) {
                addMeterValuesToTransaction(transaction, event.getMeterValueList());
                repository.createOrUpdate(transaction);
            }
        }
    }

    /**
     * Adds the {@code List} of {@code MeterValue}s to the {@code Transaction}.
     *
     * @param transaction the {@code Transaction} to which to add the {@code MeterValue}s.
     * @param meterValues the {@code List} of {@code MeterValue}s to add.
     */
    private void addMeterValuesToTransaction(final Transaction transaction, final List<MeterValue> meterValues) {
        for (MeterValue meterValue : meterValues) {
            addMeterValueToTransaction(transaction, meterValue);
        }
    }

    /**
     * Adds a single {@code MeterValue} to the {@code Transaction}.
     * <p/>
     * If a {@code MeterValue} cannot be added this method will skip adding it, won't throw an exception, and log that
     * this occurred and why.
     *
     * @param transaction the {@code Transaction} to which to add the {@code MeterValue}.
     * @param meterValue  the {@code MeterValue} to add.
     */
    private void addMeterValueToTransaction(final Transaction transaction, final MeterValue meterValue) {
        if (meterValue.getUnit() == UnitOfMeasure.WATT_HOUR || meterValue.getUnit() == UnitOfMeasure.KILOWATT_HOUR) {
            try {
                transaction.getMeterValues().add(toOperatorApiMeterValue(meterValue));
            } catch (Throwable t) {
                // Catching a Throwable here because we want to ensure other MeterValues are processed even if this one
                // fails (for whatever reason!).
                LOG.info(String.format("Skipping adding MeterValue [%s] to Transaction [%s] because an Exception was thrown", meterValue, transaction.getTransactionId()), t);
            }
        } else {
            LOG.info("Skipping adding MeterValue [{}] to Transaction [{}] because UnitOfMeasure is not WATT_HOUR or KILOWATT_HOUR", meterValue, transaction.getTransactionId());
        }
    }

    /**
     * Converts the Core API's {@code MeterValue} to an Operator API's {@code MeterValue}.
     *
     * @param meterValue the Core API {@code MeterValue}.
     * @return the Operator API {@code MeterValue}.
     * @throws AssertionError if an unexpected {@code UnitOfMeasure} is encountered.
     */
    private io.motown.operatorapi.viewmodel.persistence.entities.MeterValue toOperatorApiMeterValue(MeterValue meterValue) {
        String value;

        switch (meterValue.getUnit()) {
            case WATT_HOUR:
                value = meterValue.getValue();
                break;
            case KILOWATT_HOUR:
                value = String.valueOf(Float.parseFloat(meterValue.getValue()) * 1000);
                break;
            default:
                throw new AssertionError(String.format("Unexpected value for MeterValue's UnitOfMeasure [%s]", meterValue.getUnit()));
        }

        return new io.motown.operatorapi.viewmodel.persistence.entities.MeterValue(meterValue.getTimestamp(), value);
    }
}
