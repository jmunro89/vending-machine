package com.tenx.banking.core.business;

import static java.util.Comparator.comparingInt;

import static com.tenx.banking.core.model.Coin.coins;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.tenx.banking.core.model.Coin;

public class GreedyChangeCalculator implements ChangeCalculator {

    @Override
    public Collection<Coin> getOptimalChangeFor(int pence) {
        Collection<Coin> availableCoins = coins();

        Collection<Coin> changeGiven = new ArrayList<>();

        int remainingChange = pence;

        while (remainingChange > 0) {
            int maximumAmountToReturn = remainingChange;
            Coin coinWithHighestPossibleDenomination = availableCoins.stream()
                    .filter(it -> it.denomination <= maximumAmountToReturn)
                    .max(comparingInt(coin -> coin.denomination))
                    .orElseThrow(() -> new IllegalStateException("Insufficient coins to return change"));
            changeGiven.add(coinWithHighestPossibleDenomination);
            remainingChange = remainingChange - coinWithHighestPossibleDenomination.denomination;
        }

        return changeGiven;
    }

    @Override
    public Collection<Coin> calculate(int pence, Map<Coin, Integer> coinInventory) {
        return null;
    }
}
