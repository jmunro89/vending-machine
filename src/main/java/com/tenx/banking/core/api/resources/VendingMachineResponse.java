package com.tenx.banking.core.api.resources;

import java.util.Collection;
import java.util.StringJoiner;

import com.tenx.banking.core.model.Coin;

public class VendingMachineResponse {
    private String message;
    private Collection<Coin> coins;

    public VendingMachineResponse(String message, Collection<Coin> coins) {
        this.message = message;
        this.coins = coins;
    }

    public String getMessage() {
        return message;
    }

    public Collection<Coin> getCoins() {
        return coins;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", VendingMachineResponse.class.getSimpleName() + "[", "]")
                .add("message='" + message + "'")
                .add("coins=" + coins)
                .toString();
    }
}
