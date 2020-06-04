package com.tenx.banking.core.business;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import com.tenx.banking.core.model.Coin;
import com.tenx.banking.core.state.CoinInventoryManager;
import com.tenx.banking.core.state.PropertiesFileManager;

public class VendingMachine {
    private CoinInventoryManager coinInventoryManager;
    private ChangeCalculator changeCalculator;

    public VendingMachine(CoinInventoryManager coinInventoryManager, ChangeCalculator changeCalculator) {
        this.coinInventoryManager = coinInventoryManager;
        this.changeCalculator = changeCalculator;
    }

    public VendingMachine() {
        this(new PropertiesFileManager(new Properties()), new GreedyChangeCalculator());
    }

    public Collection<Coin> getChangeFor(int pence) {
        Map<Coin, Integer> coinInventory = coinInventoryManager.getCoins();
        Collection<Coin> change = this.changeCalculator.calculate(pence, coinInventoryManager.getCoins());
        Map<Coin, Integer> updatedInventory = subtractChangeFromInventory(coinInventory, change);
        coinInventoryManager.setCoins(updatedInventory);
        return change;
    }

    private Map<Coin, Integer> subtractChangeFromInventory(Map<Coin, Integer> coinInventory, Collection<Coin> change) {
        change.forEach(coin -> coinInventory.put(coin, coinInventory.get(coin) -1));
        return coinInventory;
    }
}
