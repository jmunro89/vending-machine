package com.tenx.banking.core.api.resources;

import java.util.Map;

import com.tenx.banking.core.model.Coin;

public class BankRequest {
    private String vendingMachineId;
    private Map<Coin, Integer> coinsRequested;

    public BankRequest(String vendingMachineId, Map<Coin, Integer> coinsRequested) {
        this.vendingMachineId = vendingMachineId;
        this.coinsRequested = coinsRequested;
    }

    public String getVendingMachineId() {
        return vendingMachineId;
    }

    public Map<Coin, Integer> getCoinsRequested() {
        return coinsRequested;
    }
}
