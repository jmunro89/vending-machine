package com.tenx.banking.core.business;

import static java.lang.String.format;

import static com.tenx.banking.core.model.Coin.FIFTY_PENCE;
import static com.tenx.banking.core.model.Coin.FIVE_PENCE;
import static com.tenx.banking.core.model.Coin.ONE_PENNY;
import static com.tenx.banking.core.model.Coin.ONE_POUND;
import static com.tenx.banking.core.model.Coin.TEN_PENCE;
import static com.tenx.banking.core.model.Coin.TWENTY_PENCE;
import static com.tenx.banking.core.model.Coin.TWO_PENCE;
import static com.tenx.banking.core.model.Coin.coins;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.tenx.banking.core.model.Coin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class GreedyChangeCalculatorTest {
    private GreedyChangeCalculator calculator = new GreedyChangeCalculator();

    @Nested
    @DisplayName("Calculate Optimal Change Tests")
    class OptimalChange {
        @Test
        public void shouldReturnNoChangeForZeroPence() {
            assertThat(calculator.calculate(0)).isEmpty();
        }

        @Test
        public void shouldReturnNoChangeForNegativeDenomination() {
            assertThat(calculator.calculate(-1)).isEmpty();
        }

        @Test
        public void shouldReturnOptimalChangeFor1Penny() {
            assertThat(calculator.calculate(1)).containsExactly(ONE_PENNY);
        }

        @Test
        public void shouldReturnOptimalChangeFor11Pence() {
            assertThat(calculator.calculate(11)).containsExactly(TEN_PENCE, ONE_PENNY);
        }

        @Test
        public void shouldReturnOptimalChangeFor188Pence() {
            assertThat(calculator.calculate(188)).containsExactly(ONE_POUND, FIFTY_PENCE, TWENTY_PENCE, TEN_PENCE, FIVE_PENCE, TWO_PENCE, ONE_PENNY);
        }
    }

    @Nested
    @DisplayName("Calculate Change From Inventory Tests")
    class ChangeFromInventory {

        private static final int ONE_COIN = 1;
        private Map<Coin, Integer> coinInventory;

        @BeforeEach
        private void setUp() {
            this.coinInventory = new HashMap<>();
            coins().forEach(coin -> this.coinInventory.put(coin, ONE_COIN));
        }

        @Test
        public void shouldReturnNoChangeForZeroPence() {
            assertThat(calculator.calculate(0)).isEmpty();
        }

        @Test
        public void shouldReturnNoChangeForNegativeDenomination() {
            assertThat(calculator.calculate(-1, coinInventory)).isEmpty();
        }

        @Test
        public void shouldReturnOptimalChangeFor1Penny() {
            assertThat(calculator.calculate(1, coinInventory)).containsExactly(ONE_PENNY);
        }

        @Test
        public void shouldReturnOptimalChangeFor11Pence() {
            assertThat(calculator.calculate(11, coinInventory)).containsExactly(TEN_PENCE, ONE_PENNY);
        }

        @Test
        public void shouldReturnOptimalChangeFor188Pence() {
            assertThat(calculator.calculate(188, coinInventory)).containsExactly(ONE_POUND, FIFTY_PENCE, TWENTY_PENCE, TEN_PENCE, FIVE_PENCE, TWO_PENCE, ONE_PENNY);
        }

        @Test
        public void shouldReturnSubOptimalChangeForInventoryWithOnlyPennies() {
            this.coinInventory = new HashMap<>();
            this.coinInventory.put(ONE_PENNY, 5);
            assertThat(calculator.calculate(5, coinInventory)).containsOnly(ONE_PENNY).hasSize(5);
        }

        @Test
        public void shouldThrowExceptionWhenUnableToProvideChangeWithGivenInventory() {
            Collection<Coin> expectedAvailableCoins = coinInventory.keySet();
            expectedAvailableCoins.remove(ONE_PENNY);
            expectedAvailableCoins.remove(TWO_PENCE);

            assertThatThrownBy(() -> calculator.calculate(4, coinInventory))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(format("Insufficient coins to return change for %d pence from inventory %s", 4, expectedAvailableCoins.toString()));
        }

    }

}