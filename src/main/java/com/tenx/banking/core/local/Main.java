package com.tenx.banking.core.local;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;

import static com.tenx.banking.core.model.Coin.FIFTY_PENCE;
import static com.tenx.banking.core.model.Coin.FIVE_PENCE;
import static com.tenx.banking.core.model.Coin.ONE_PENNY;
import static com.tenx.banking.core.model.Coin.ONE_POUND;
import static com.tenx.banking.core.model.Coin.TEN_PENCE;
import static com.tenx.banking.core.model.Coin.TWENTY_PENCE;
import static com.tenx.banking.core.model.Coin.TWO_PENCE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.tenx.banking.core.business.ChangeCalculator;
import com.tenx.banking.core.business.GreedyChangeCalculator;
import com.tenx.banking.core.business.VendingMachine;
import com.tenx.banking.core.model.Coin;
import com.tenx.banking.core.state.CoinInventoryManager;
import com.tenx.banking.core.state.PropertiesFileManager;

public class Main {

    private static final String PROPERTIES_FILE = "coin-inventory.properties";

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        final CoinInventoryManager coinInventoryManager = new PropertiesFileManager();
        Map<Coin, Integer> initialCoins = getInitialCoins();
        coinInventoryManager.setCoins(PROPERTIES_FILE, initialCoins);
        VendingMachine vendingMachine = new VendingMachine(PROPERTIES_FILE, coinInventoryManager);
        try{
            while (true) {
                System.out.println("How much change would you like? Please enter a number");
                Collection<Coin> coins = vendingMachine.getChangeFor(parseInt(reader.readLine()));
                System.out.println(format("Here is your change %s", coins.toString()));
            }
        } catch (IllegalStateException e) {
            System.out.println("Sorry, I've run out of coins");
        }
    }

    private static Map<Coin, Integer> getInitialCoins() {
        Map<Coin, Integer> initialCoins = new HashMap<>();
        initialCoins.put(ONE_POUND, 11);
        initialCoins.put(FIFTY_PENCE, 24);
        initialCoins.put(TWENTY_PENCE, 0);
        initialCoins.put(TEN_PENCE, 99);
        initialCoins.put(FIVE_PENCE, 200);
        initialCoins.put(TWO_PENCE, 11);
        initialCoins.put(ONE_PENNY, 23);
        return initialCoins;
    }
}
