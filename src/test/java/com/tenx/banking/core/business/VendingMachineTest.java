package com.tenx.banking.core.business;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import static com.tenx.banking.core.model.Coin.ONE_PENNY;
import static com.tenx.banking.core.model.Coin.ONE_POUND;
import static com.tenx.banking.core.model.Coin.TWO_PENCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tenx.banking.core.model.Coin;
import com.tenx.banking.core.state.CoinInventoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VendingMachineTest {
    private static final int A_PENNY = 1;

    @Mock
    private ChangeCalculator changeCalculator;

    @Mock
    private CoinInventoryManager inventoryManager;

    private VendingMachine vendingMachine;

    private Map<Coin, Integer> coinInventory;

    @BeforeEach
    public void setup() {
        this.vendingMachine = new VendingMachine(inventoryManager, changeCalculator);
        this.coinInventory = new HashMap<>();
    }

    @Test
    public void shouldCalculateUsingCoinsFromInventory() {
        when(this.inventoryManager.getCoins()).thenReturn(coinInventory);

        vendingMachine.getChangeFor(A_PENNY);
        verify(this.changeCalculator).calculate(A_PENNY, coinInventory);
        verify(this.inventoryManager, times(2)).getCoins();
    }

    @Test
    public void shouldReturnCalculatedChange() {
        List<Coin> expectedCoins = singletonList(ONE_PENNY);

        when(this.inventoryManager.getCoins()).thenReturn(coinInventory);
        when(this.changeCalculator.calculate(A_PENNY, coinInventory)).thenReturn(expectedCoins);

        assertThat(vendingMachine.getChangeFor(A_PENNY)).isEqualTo(expectedCoins);
    }

    @Test
    public void shouldUpdateInventoryWithChangeSubtracted() {
        coinInventory.put(ONE_PENNY, 5);
        coinInventory.put(TWO_PENCE, 3);
        coinInventory.put(ONE_POUND, 1);

        List<Coin> change = asList(ONE_PENNY, ONE_PENNY, TWO_PENCE);

        Map<Coin, Integer> expectedUpdatedInventory = new HashMap<>();
        expectedUpdatedInventory.put(ONE_PENNY, 3);
        expectedUpdatedInventory.put(TWO_PENCE, 2);
        expectedUpdatedInventory.put(ONE_POUND, 1);

        when(this.inventoryManager.getCoins()).thenReturn(coinInventory);
        when(this.changeCalculator.calculate(A_PENNY, coinInventory)).thenReturn(change);

        vendingMachine.getChangeFor(A_PENNY);

        verify(inventoryManager).setCoins(expectedUpdatedInventory);
    }

}