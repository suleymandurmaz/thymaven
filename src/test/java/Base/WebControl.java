package Base;

import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import webdriver.Driver;

import java.time.Duration;

public abstract class WebControl {

    protected By locator;
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected String label = "";
    protected WebElement element;
    private final static Duration WAIT_TIME = Duration.ofSeconds(30);
    private static final Logger LOGGER = LoggerFactory.getLogger(WebControl.class);


    public WebControl() {
    }

    public WebControl(By locator) {
        this.locator = locator;
        label = locator.toString();
        driver = Driver.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10), Duration.ofMillis(300));
        this.element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }



    public WebElement getRootElement() {
        return waitForControl(locator);
    }



    public WebElement waitForControl(By locator) {
        return  wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }


    public WebElement waitForControlElement(WebElement control, By locator) {
        return wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(control, locator));
    }


    public void clear() {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), WAIT_TIME);
        wait.until((driver) -> {
            try {
                WebElement elem = driver.findElement(locator);
                elem.clear();
                return true;
            } catch (InvalidElementStateException | NoSuchElementException e) {

                return false;
            }
        });
    }

//    public void click() {
//        wait.until(ExpectedConditions.elementToBeClickable(element));
//        try {
//            element.click();
//        } catch(ElementClickInterceptedException | StaleElementReferenceException e) {
//            wait.until((driver) -> {
//                try {
//                    element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
//                    wait.until(ExpectedConditions.elementToBeClickable(element));
//                    element.click();
//                    return true;
//                } catch (ElementClickInterceptedException | StaleElementReferenceException e2) {
//
//                    return false;
//                }
//            });



            public void click() {
                wait.until(ExpectedConditions.elementToBeClickable(element));

                try {
                    element.click();
                } catch (ElementClickInterceptedException | StaleElementReferenceException a) {

                }
                wait.until((driver1) -> {
                    try {
                        new Actions(driver1).moveToElement(element).click();
                        return true;
                    } catch (java.lang.Exception e2) {
                        try {
                            ((JavascriptExecutor) driver1).executeScript("arguments[0].click()'", element);
                            return true;
                        } catch (java.lang.Exception e3) {
                            return false;


                        }
                    }

                });
            }

    public void click(WebElement element, By locator) {
        wait.until((driver) -> {
            try {
                WebElement elem = element.findElement(locator);
                if (elem.isDisplayed() && elem.isEnabled()) {
                    elem.click();
                    return true;
                }
                return false;
            } catch (ElementClickInterceptedException | StaleElementReferenceException e) {

                return false;
            }
        });

    }


    public void waitUntilJSReady() {
        try {
            ExpectedCondition<Boolean> jsLoad = driver -> ((JavascriptExecutor) Driver.getDriver())
                    .executeScript("return document.readyState").toString().equals("complete");
            JavascriptExecutor jsExec = (JavascriptExecutor) Driver.getDriver();
            boolean jsReady = jsExec
                    .executeScript("return document.readyState")
                    .toString().equals("complete");
            WebDriverWait jsWait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
            if (!jsReady) {
                jsWait.until(jsLoad);
            }
        } catch (WebDriverException ignored) {
        }
    }

    public void scrollIntoView(By locator){
        WebDriver driver = Driver.getDriver();
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(false);", driver.findElement(locator)
        );
    }

    public void scrollDownUntilVisible(WebElement panel, By itemLocator){
        WebDriver driver = Driver.getDriver();
        new Actions(driver)
                .moveToElement(panel)
                .click()
                .perform();
        int i = 0;
        while (!driver.findElement(itemLocator).isDisplayed()) {
            new Actions(driver)
                    .sendKeys(Keys.PAGE_DOWN)
                    .build()
                    .perform();
            if (i++>20)
                break;
        }
    }

//    public void hover(){
//        try {
//            new Actions(driver).moveToElement(element).perform();
//        }catch (Exception e){
//            takeAllureScreenShot(e.getMessage());
//            e.printStackTrace();
//        }
//    }

//    public void takeAllureScreenShot(String text){
//        Allure.addAttachment(text,
//                new ByteArrayInputStream(
//                        ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES))
//        );

    public void sendKeys(WebElement element, String text) {
        //wait.until(ExpectedConditions.elementToBeClickable(element)).click();

        wait.until(driver1 -> {
            try {
                element.clear();
                element.sendKeys(text);
                return true;
            } catch (java.lang.Exception e1) {
                try {
                    element.clear();
                    new Actions(driver1).moveToElement(element).sendKeys(text).perform();
                    return true;
                } catch (java.lang.Exception e2) {
                    try {
                        element.clear();
                        ((JavascriptExecutor) driver1).executeScript("arguments[0].value='" + text + "'", element);
                        return true;
                    } catch (java.lang.Exception e3) {
                        return false;
                    }
                }
            }
        });
    }

    public void sendKeys(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        sendKeys(element, text);
    }

}
