package com.tenx.banking.core.model;

import static com.tenx.banking.core.model.Coin.coins;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CoinTest {

    @Test
    public void shouldHaveCorrectDenominations() {
        assertThat(coins().stream()
                .map(coin -> coin.denomination))
                .containsExactly(100, 50, 20, 10, 5, 2, 1);
    }

}