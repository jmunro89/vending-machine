package com.tenx.banking.core.api.publishing;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublishingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublishingService.class);
    private static final String TOPIC = "TOPIC_NAME";

    private final AmazonSNS snsClient;
    private final String topic;

    PublishingService(AmazonSNS snsClient, String topic) {
        this.snsClient = snsClient;
        this.topic = topic;
    }

    public PublishingService() {
        this(new SnsConfigBuilder().get().build(), System.getenv(TOPIC));
    }

    public void publish(String message) {
        PublishRequest publishRequest = new PublishRequest(this.topic, message);
        LOGGER.info("Publish Request: {}", publishRequest.toString());
        PublishResult publishResult = this.snsClient.publish(publishRequest);
        LOGGER.info("Message successfully published with id: {}", publishResult.getMessageId());
    }

}
