package com.tenx.banking.core.business;

import java.util.Collection;

import com.tenx.banking.core.model.Coin;

public interface ChangeCalculator {
    Collection<Coin> getOptimalChangeFor(int pence);

    Collection<Coin> getChangeFor(int pence);
}
