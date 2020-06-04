package com.tenx.banking.core.model;

import static java.util.Arrays.stream;

import java.util.Collection;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public enum Coin {
    ONE_POUND(100),
    FIFTY_PENCE(50),
    TWENTY_PENCE(20),
    TEN_PENCE(10),
    FIVE_PENCE(5),
    TWO_PENCE(2),
    ONE_PENNY(1);

    public final int denomination;

    Coin(int denomination) {
        this.denomination = denomination;
    }

    public static Collection<Coin> coins() {
        return stream(values()).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Coin.class.getSimpleName() + "[", "]")
                .add("denomination=" + denomination)
                .toString();
    }
}
