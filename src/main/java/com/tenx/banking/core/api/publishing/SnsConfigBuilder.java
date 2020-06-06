package com.tenx.banking.core.api.publishing;

import static com.amazonaws.retry.PredefinedRetryPolicies.DEFAULT_RETRY_CONDITION;
import static com.amazonaws.retry.RetryPolicy.BackoffStrategy.NO_DELAY;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.retry.RetryPolicy;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;

class SnsConfigBuilder {

    private static final int MAX_RETRIES = 5;
    private static final int TIMEOUT_MILLISECONDS = 250;
    private static final boolean HONOR_MAX_RETRIES_IN_CLIENT_CONFIG = false;

    AmazonSNSClientBuilder get() {
        return AmazonSNSClient.builder()
                .withRegion(Regions.EU_WEST_1)
                .withClientConfiguration(
                        new ClientConfiguration()
                                .withConnectionTimeout(TIMEOUT_MILLISECONDS)
                                .withRequestTimeout(TIMEOUT_MILLISECONDS)
                                .withRetryPolicy(new RetryPolicy(DEFAULT_RETRY_CONDITION, NO_DELAY, MAX_RETRIES, HONOR_MAX_RETRIES_IN_CLIENT_CONFIG)));
    }

}
