package com.spribe.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    public final static Logger LOGGER = LogManager.getLogger(RetryAnalyzer.class);

    private int retryCount = 0;

    int maxRetryCount = ConfigReader.getIntProperty("retry_count");

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            LOGGER.info("Retry {} for test method: {}", retryCount, result.getName());
            return true;
        }
        return false;
    }
}
