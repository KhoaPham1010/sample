package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ElementUtils {
    private static WebDriverWait webDriverWait;

    public static WebElement waitForElementVisible(By byElement, WebDriver searchContext) {
        webDriverWait = new WebDriverWait(searchContext, 30);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(byElement));
        return searchContext.findElement(byElement);
    }

    public static WebElement waitForElementVisible(WebElement element, WebDriver searchContext) {
        webDriverWait = new WebDriverWait(searchContext, 30);
        webDriverWait.until(ExpectedConditions.visibilityOf(element));
        return element;
    }

    public static WebElement waitForElementNotVisible(By byElement, WebDriver searchContext) {
        webDriverWait = new WebDriverWait(searchContext, 10);
        webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(byElement));
        return searchContext.findElement(byElement);
    }

    public static WebElement waitForElementNotVisible(WebElement element, WebDriver searchContext) {
        webDriverWait = new WebDriverWait(searchContext, 10);
        webDriverWait.until(ExpectedConditions.invisibilityOf(element));
        return element;
    }

    public static WebElement waitForElementClickable(By byElement, WebDriver searchContext) {
        webDriverWait = new WebDriverWait(searchContext, 30);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(byElement));
        return searchContext.findElement(byElement);
    }

    public static WebElement waitForElementClickable(WebElement element, WebDriver searchContext) {
        webDriverWait = new WebDriverWait(searchContext, 30);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(element));
        return element;
    }

    public static List<String> getElementTexts(Collection<WebElement> elements) {
        return elements.stream()
            .map(WebElement::getText)
            .collect(toList());
    }
}
