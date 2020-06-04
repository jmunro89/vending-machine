package com.tenx.banking.core.state;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import static java.util.stream.Collectors.toMap;

import static com.tenx.banking.core.model.Coin.coins;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import com.tenx.banking.core.model.Coin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesFileManager implements CoinInventoryManager {

    private static final String COIN_INVENTORY_PROPERTIES = "coin-inventory.properties";
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesFileManager.class.getName());
    private static final String NO_COINS = "0";

    private Properties properties;

    public PropertiesFileManager(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Map<Coin, Integer> getCoins() {
        try {
            properties.load(new FileInputStream(COIN_INVENTORY_PROPERTIES));
        } catch (IOException e) {
            LOGGER.error("Failed to read properties file", e);
        }
        return coins().stream()
                .collect(toMap(coin -> coin, this::getCountFor));
    }

    @Override
    public void setCoins(Map<Coin, Integer> coins) {
        coins.keySet().forEach(it -> properties.setProperty(valueOf(it.denomination), valueOf(coins.get(it))));
        try {
            properties.store(new FileOutputStream(COIN_INVENTORY_PROPERTIES), null);
        } catch (IOException e) {
            LOGGER.error("Failed to write to properties file", e);
        }
    }

    private int getCountFor(Coin coin) {
        return parseInt((String) properties.getOrDefault(valueOf(coin.denomination), NO_COINS));
    }
}