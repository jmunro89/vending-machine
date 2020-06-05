package com.tenx.banking.core.business;

import java.util.Collection;
import java.util.Map;

import com.tenx.banking.core.model.Coin;

public interface ChangeCalculator {
    Collection<Coin> calculate(int pence);

    Collection<Coin> calculate(int pence, Map<Coin, Integer> coinInventory);
}
