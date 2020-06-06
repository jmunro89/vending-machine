package com.tenx.banking.core.state;

import static java.lang.String.format;

import static com.amazonaws.regions.Regions.EU_WEST_1;

import java.util.Map;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenx.banking.core.model.Coin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class S3InventoryManager implements CoinInventoryManager {
    private static final String PATH_FORMAT = "coin-inventory/%s";
    private static final String BUCKET_NAME = "vending-machine";
    private AmazonS3 s3;
    private ObjectMapper objectMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(S3InventoryManager.class.getName());


    public S3InventoryManager(AmazonS3 s3, ObjectMapper objectMapper) {
        this.s3 = s3;
        this.objectMapper = objectMapper;
    }

    public S3InventoryManager() {
        this.s3 = AmazonS3ClientBuilder.standard()
                .withRegion(EU_WEST_1)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void setCoins(String key, Map<Coin, Integer> coins) {
        try {
            LOGGER.info(String.format("Storing coins: %s to key %s", coins, key));
            s3.putObject(BUCKET_NAME, format(PATH_FORMAT, key), objectMapper.writeValueAsString(coins));
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to map coin inventory to json", e);
        } catch (AmazonS3Exception e) {
            LOGGER.error("Failed to write to S3", e);
        }
    }

    @Override
    public Map<Coin, Integer> getCoins(String key) {
        if (s3.doesObjectExist(BUCKET_NAME, format(PATH_FORMAT, key))) {
            String result = s3.getObjectAsString(BUCKET_NAME, format(PATH_FORMAT, key));
            try {
                return objectMapper.readValue(result, new TypeReference<Map<Coin, Integer>>() {});
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
