package com.tenx.banking.core.business;

import static com.tenx.banking.core.model.Coin.FIFTY_PENCE;
import static com.tenx.banking.core.model.Coin.FIVE_PENCE;
import static com.tenx.banking.core.model.Coin.ONE_PENNY;
import static com.tenx.banking.core.model.Coin.ONE_POUND;
import static com.tenx.banking.core.model.Coin.TEN_PENCE;
import static com.tenx.banking.core.model.Coin.TWENTY_PENCE;
import static com.tenx.banking.core.model.Coin.TWO_PENCE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class GreedyChangeCalculatorTest {
    private GreedyChangeCalculator calculator = new GreedyChangeCalculator();

    @Test
    public void shouldReturnNoChangeForZeroPence() {
        assertThat(calculator.getOptimalChangeFor(0)).isEmpty();
    }

    @Test
    public void shouldReturnNoChangeForNegativeDenomination() {
        assertThat(calculator.getOptimalChangeFor(-1)).isEmpty();
    }

    @Test
    public void shouldReturnOptimalChangeFor1Penny() {
        assertThat(calculator.getOptimalChangeFor(1)).containsExactly(ONE_PENNY);
    }

    @Test
    public void shouldReturnOptimalChangeFor11Pence() {
        assertThat(calculator.getOptimalChangeFor(11)).containsExactly(TEN_PENCE, ONE_PENNY);
    }

    @Test
    public void shouldReturnOptimalChangeFor188Pence() {
        assertThat(calculator.getOptimalChangeFor(188)).containsExactly(ONE_POUND, FIFTY_PENCE, TWENTY_PENCE, TEN_PENCE, FIVE_PENCE, TWO_PENCE, ONE_PENNY);
    }

}