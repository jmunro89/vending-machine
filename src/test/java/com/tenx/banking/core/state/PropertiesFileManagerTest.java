package com.tenx.banking.core.state;

import static java.lang.Integer.valueOf;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.tenx.banking.core.model.Coin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PropertiesFileManagerTest {

    private static final String ONE_COIN = "1";
    private static final String DEFAULT_VALUE = "0";
    private static final String ONE_PENNY = "1";
    private static final String TWO_PENCE = "2";
    private static final String FIVE_PENCE = "5";
    private static final String TEN_PENCE = "10";
    private static final String TWENTY_PENCE = "20";
    private static final String FIFTY_PENCE = "50";
    private static final String ONE_POUND = "100";
    private static final String A_KEY = "application.properties";

    @Mock
    Properties properties;

    private PropertiesFileManager manager;

    @BeforeEach
    private void setUp() {
        this.manager = new PropertiesFileManager(properties);
    }

    @Test
    public void shouldGetProperties() throws IOException {
        when(properties.getOrDefault(ONE_PENNY, DEFAULT_VALUE)).thenReturn(ONE_COIN);
        when(properties.getOrDefault(TWO_PENCE, DEFAULT_VALUE)).thenReturn(ONE_COIN);
        when(properties.getOrDefault(FIVE_PENCE, DEFAULT_VALUE)).thenReturn(ONE_COIN);
        when(properties.getOrDefault(TEN_PENCE, DEFAULT_VALUE)).thenReturn(ONE_COIN);
        when(properties.getOrDefault(TWENTY_PENCE, DEFAULT_VALUE)).thenReturn(ONE_COIN);
        when(properties.getOrDefault(FIFTY_PENCE, DEFAULT_VALUE)).thenReturn(ONE_COIN);
        when(properties.getOrDefault(ONE_POUND, DEFAULT_VALUE)).thenReturn(ONE_COIN);

        Map<Coin, Integer> coins = manager.getCoins(A_KEY);
        assertThat(coins).containsKeys(Coin.values());
        assertThat(coins).containsValue(valueOf(ONE_COIN));

        verify(properties).load(isA(FileInputStream.class));
        verify(properties).getOrDefault(ONE_PENNY, DEFAULT_VALUE);
        verify(properties).getOrDefault(TWO_PENCE, DEFAULT_VALUE);
        verify(properties).getOrDefault(FIVE_PENCE, DEFAULT_VALUE);
        verify(properties).getOrDefault(TEN_PENCE, DEFAULT_VALUE);
        verify(properties).getOrDefault(TWENTY_PENCE, DEFAULT_VALUE);
        verify(properties).getOrDefault(FIFTY_PENCE, DEFAULT_VALUE);
        verify(properties).getOrDefault(ONE_POUND, DEFAULT_VALUE);
    }

    @Test
    public void shouldSetCoins() throws IOException {
        Map<Coin, Integer> coinInventory = new HashMap<>();
        coinInventory.put(Coin.TEN_PENCE, 5);
        manager.setCoins(A_KEY, coinInventory);

        verify(properties).setProperty("10", "5");
        verify(properties).store(isA(FileOutputStream.class), isNull());
    }

}