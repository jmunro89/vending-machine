package com.tenx.banking.core.business;

import static java.lang.String.format;
import static java.util.Collections.nCopies;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;

import static com.tenx.banking.core.model.Coin.coins;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.tenx.banking.core.model.Coin;

public class GreedyChangeCalculator implements ChangeCalculator {

    @Override
    public Collection<Coin> calculate(int pence) {
        return this.calculate(pence, coins(), true);
    }

    @Override
    public Collection<Coin> calculate(int pence, Map<Coin, Integer> coinInventory) {
        Collection<Coin> availableCoins = getAllCoinsFrom(coinInventory);
        return this.calculate(pence, availableCoins, false);
    }

    private List<Coin> getAllCoinsFrom(Map<Coin, Integer> coinInventory) {
        return coinInventory.keySet()
                .stream()
                .map(it -> nCopies(coinInventory.get(it), it))
                .flatMap(List::stream)
                .collect(toList());
    }

    private Collection<Coin> calculate(int pence, Collection<Coin> availableCoins, boolean samplingWithReplacement) {

        Collection<Coin> changeGiven = new ArrayList<>();

        int remainingChange = pence;

        while (remainingChange > 0) {
            int maximumAmountToReturn = remainingChange;
            Coin coin = getCoinWithHighestPossibleDenomination(availableCoins, maximumAmountToReturn);
            changeGiven.add(coin);
            if (!samplingWithReplacement) {
                availableCoins.remove(coin);
            }
            remainingChange = remainingChange - coin.denomination;
        }

        return changeGiven;
    }

    private Coin getCoinWithHighestPossibleDenomination(Collection<Coin> availableCoins, int maximumAmountToReturn) {
        return availableCoins.stream()
                .filter(it -> it.denomination <= maximumAmountToReturn)
                .max(comparingInt(coin -> coin.denomination))
                .orElseThrow(() -> new IllegalStateException(format("Insufficient coins to return change for %d pence from inventory %s", 4, availableCoins.toString())));
    }

}
