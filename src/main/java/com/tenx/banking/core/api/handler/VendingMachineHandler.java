package com.tenx.banking.core.api.handler;

import static com.tenx.banking.core.model.Coin.coins;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenx.banking.core.api.resources.BankRequest;
import com.tenx.banking.core.api.publishing.PublishingService;
import com.tenx.banking.core.api.resources.VendingMachineRequest;
import com.tenx.banking.core.api.resources.VendingMachineResponse;
import com.tenx.banking.core.business.VendingMachine;
import com.tenx.banking.core.model.Coin;
import com.tenx.banking.core.state.CoinInventoryManager;
import com.tenx.banking.core.state.S3InventoryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VendingMachineHandler implements RequestStreamHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(VendingMachineHandler.class);
    private static final String SUCCESS_MESSAGE = "Here's your change. Have a nice day :)";
    private static final String NOT_ENOUGH_COINS_MESSAGE = "Sorry, I don't have enough coins to give you change. I am requesting more coins from the bank, please try again shortly";
    private CoinInventoryManager coinInventoryManager;
    private PublishingService publishingService;
    private ObjectMapper objectMapper;

    public VendingMachineHandler(CoinInventoryManager coinInventoryManager, PublishingService publishingService, ObjectMapper objectMapper) {
        this.coinInventoryManager = coinInventoryManager;
        this.publishingService = publishingService;
        this.objectMapper = objectMapper;
    }

    public VendingMachineHandler() {
        this.coinInventoryManager = new S3InventoryManager();
        this.publishingService = new PublishingService();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        VendingMachineRequest request = objectMapper.readValue(input, VendingMachineRequest.class);
        LOGGER.info("Handling request: {}", request.toString());
        String vendingMachineId = request.getVendingMachineId();
        try {
            VendingMachine vendingMachine = new VendingMachine(vendingMachineId, this.coinInventoryManager);
            VendingMachineResponse response = new VendingMachineResponse(SUCCESS_MESSAGE, vendingMachine.getChangeFor(request.getChange()));
            LOGGER.info("Returning response: {}", response.toString());
            objectMapper.writeValue(output, response);
        } catch (IllegalStateException e) {
            Map<Coin, Integer> coinInventory = coinInventoryRefill();
            BankRequest bankRequest = new BankRequest(vendingMachineId, coinInventory);
            this.publishingService.publish(objectMapper.writeValueAsString(bankRequest));
            VendingMachineResponse response = new VendingMachineResponse(NOT_ENOUGH_COINS_MESSAGE, null);
            LOGGER.info("Returning response: {}", response.toString());
            objectMapper.writeValue(output, response);
        }
    }

    private Map<Coin, Integer> coinInventoryRefill() {
        Map<Coin, Integer> coinInventory = new HashMap<>();
        coins().forEach(coin -> coinInventory.put(coin, 10));
        return coinInventory;
    }

}