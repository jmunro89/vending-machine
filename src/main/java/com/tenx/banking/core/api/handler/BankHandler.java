package com.tenx.banking.core.api.handler;

import java.io.IOException;
import java.util.List;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenx.banking.core.api.resources.BankRequest;
import com.tenx.banking.core.state.CoinInventoryManager;
import com.tenx.banking.core.state.S3InventoryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BankHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(BankHandler.class);

    private final ObjectMapper objectMapper;
    private final CoinInventoryManager coinInventoryManager;

    public BankHandler(ObjectMapper objectMapper, CoinInventoryManager coinInventoryManager) {
        this.objectMapper = objectMapper;
        this.coinInventoryManager = coinInventoryManager;
    }

    public BankHandler() {
        this(new ObjectMapper(), new S3InventoryManager());
    }

    public void handleEvent(SNSEvent event) {
        List<SNSEvent.SNSRecord> records = event.getRecords();
        LOGGER.info(String.format("Processing %d records", records.size()));
        records.forEach(this::handleRecord);
    }

    private void handleRecord(SNSEvent.SNSRecord record) {
        String incomingMessage = record.getSNS().getMessage();
        LOGGER.info(String.format("processing message : %s", incomingMessage));
        try {
            BankRequest bankRequest = this.objectMapper.readValue(incomingMessage, BankRequest.class);
            this.coinInventoryManager.setCoins(bankRequest.getVendingMachineId(), bankRequest.getCoinsRequested());
        } catch (IOException e) {
            LOGGER.error("Error in lambda", e);
            throw new Error("Error in lambda, see log for details");
        }
    }

}
