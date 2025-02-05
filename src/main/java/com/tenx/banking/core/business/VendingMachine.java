package com.tenx.banking.core.business;

import java.util.Collection;
import java.util.Map;

import com.tenx.banking.core.model.Coin;
import com.tenx.banking.core.state.CoinInventoryManager;

public class VendingMachine {
    private CoinInventoryManager coinInventoryManager;
    private ChangeCalculator changeCalculator;
    private String id;

    public VendingMachine(String id, CoinInventoryManager coinInventoryManager, ChangeCalculator changeCalculator) {
        this.coinInventoryManager = coinInventoryManager;
        this.changeCalculator = changeCalculator;
        this.id = id;
    }

    public VendingMachine(String id, CoinInventoryManager inventoryManager) {
        this(id, inventoryManager, new GreedyChangeCalculator());
    }

    public Collection<Coin> getOptimalChangeFor(int pence) {
        return this.changeCalculator.calculate(pence);
    }

    public Collection<Coin> getChangeFor(int pence) {
        Map<Coin, Integer> coinInventory = coinInventoryManager.getCoins(this.id);
        Collection<Coin> change = this.changeCalculator.calculate(pence, coinInventory);
        Map<Coin, Integer> updatedInventory = takeChangeFromInventory(coinInventory, change);
        coinInventoryManager.setCoins(this.id, updatedInventory);
        return change;
    }

    private Map<Coin, Integer> takeChangeFromInventory(Map<Coin, Integer> coinInventory, Collection<Coin> change) {
        change.forEach(coin -> coinInventory.put(coin, coinInventory.getOrDefault(coin, 1) - 1));
        return coinInventory;
    }
}
