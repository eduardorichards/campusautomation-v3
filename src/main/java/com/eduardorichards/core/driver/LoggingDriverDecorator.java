package com.eduardorichards.core.driver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingDriverDecorator implements WebDriverListener {

    private static final Logger log = LoggerFactory.getLogger(LoggingDriverDecorator.class);

    public WebDriver decorate(WebDriver driver) {
        return new EventFiringDecorator<>(this).decorate(driver);
    }

    @Override
    public void beforeGet(WebDriver driver, String url) {
        log.info("Navigating to: {}", url);
    }

    @Override
    public void beforeFindElement(WebDriver driver, By locator) {
        log.debug("Finding element: {}", locator);
    }

    @Override
    public void beforeClick(WebElement element) {
        log.info("Clicking element");
    }

    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        log.debug("Typing into element");
    }

    @Override
    public void beforeClear(WebElement element) {
        log.debug("Clearing element");
    }

    @Override
    public void beforeQuit(WebDriver driver) {
        log.info("Quitting driver");
    }

    @Override
    public void onError(Object target, Method method, Object[] args, InvocationTargetException e) {
        log.error("WebDriver error in: {}: {}", method.getName(), e.getTargetException().getMessage());
    }
}