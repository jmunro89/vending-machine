package com.tenx.banking.core.state;

import java.util.Map;

import com.tenx.banking.core.model.Coin;

public interface CoinInventoryManager {
    void setCoins(String key, Map<Coin, Integer> coins);

    Map<Coin, Integer> getCoins(String key);
}
