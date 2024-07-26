package com.training.configuration;

import feign.RetryableException;
import feign.Retryer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class CustomRetryer implements Retryer {
    private final int maxAttempts;
    private final long backoff;
    private int attempt;

    public CustomRetryer() {
        this(100, 3);
    }

    public CustomRetryer(long backoff, int maxAttempts) {
        this.backoff = backoff;
        this.maxAttempts = maxAttempts;
        this.attempt = 1;
    }

    @Override
    public void continueOrPropagate(RetryableException e) {
        if (attempt++ >= maxAttempts) {
            throw e;
        }
        try {
            log.warn("Retry attempt {} due to {}", attempt - 1, e.getMessage());
            TimeUnit.MILLISECONDS.sleep(backoff);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public Retryer clone() {
        return new CustomRetryer(backoff, maxAttempts);
    }
}
