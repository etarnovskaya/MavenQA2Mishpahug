package ru.stqa.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.Set;

public class HomePageHelper extends PageBase{
    @FindBy(id = "idsignin")
    WebElement loginIcon;

    @FindBy(className = "navi-item")
    List<WebElement> iconList;

    @FindBy(id = "ihome")
    WebElement homeAuthIcon;

    @FindBy(id = "profile")
    WebElement profileIcon;

    @FindBy(name = "selectholidays")
    WebElement filterHoliday;

    @FindBy(name = "selectfood")
    WebElement filterFood;

    @FindBy(name = "selectconfession")
    WebElement filterConf;

    @FindBy(xpath = "//div[@class='itemEventInsert']")
    List<WebElement> listEvents;

    ViewEventPageHelper viewEvent;

    public HomePageHelper(WebDriver driver) {
        super(driver);
    }

    public HomePageHelper waitUntilPageIsLoaded(){

        waitUntilElementClickable(loginIcon,20);
        waitUntilAllElementsVisible(listEvents,20);

        return this;
    }

    public HomePageHelper openLoginPage() {
        clickButton(loginIcon);
        return this;
    }



    public boolean homeIconIsDisplayed() {
        return  iconList.get(0).isDisplayed();
    }

    public boolean loginIconIsDisplayed() {
        return iconList.get(1).isDisplayed();
    }

    public boolean registrationIconIsDisplayed() {
        return iconList.get(2).isDisplayed();
    }

    public boolean homeAuthIconIsHidden() {
        return !homeAuthIcon.isDisplayed();
    }

    public boolean profileIconIsHidden() {
        return !profileIcon.isDisplayed();
    }


    public HomePageHelper chooseFilterHoliday(String value) {
    Select selector = new Select(filterHoliday);
    selector.selectByValue(value);
    return this;
    }

    public HomePageHelper chooseFilterFood(String value) {
        Select selector = new Select(filterFood);
        selector.selectByValue(value);
        return this;
    }

    public HomePageHelper chooseFilterConf(String value) {
        Select selector = new Select(filterConf);
        selector.selectByValue(value);
        return this;
    }

    public HomePageHelper waitEventsListReloaded() {
        try{
            new WebDriverWait(driver, 10)
                    .until(ExpectedConditions.visibilityOfAllElements(listEvents));
        } catch(Exception e){
            try {
                Thread.sleep(1000);
                waitUntilAllElementsVisible(listEvents,20);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        return this;
    }

    public boolean isEventsHoliday(String value) {
        int counter = 0;
        for(WebElement element: listEvents){
            if(element.findElement(By.className("holidayItemEvents"))
                    .getText().equals(value)) counter++;
        }
        return counter == listEvents.size();
    }


    public boolean isEventsContainsPreference(String value) {
        int counter = 0;
        for(WebElement element: listEvents){
            if(element.findElement(By.className("preferenceItemEvents"))
                    .getText().contains(value)) counter++;
        }
        return counter == listEvents.size();
    }

    public boolean isEventsHolidayInWindow(String value) {
        int counter = 0;
        for(WebElement event: listEvents){
            //System.out.println("Title: " + event.findElement(By.className("divTitleItemEvents")).getText());
            if(getHolidayFromEventInWindow(event).contains(value)) counter++;
        }
        return counter == listEvents.size();
    }

    private String getHolidayFromEventInWindow(WebElement event) {
        String mainHandle = driver.getWindowHandle();
        clickButton(event.findElement(By.xpath(".//span[@class= 'moreItemEvents']")));
        waitUntilNumberOfWindowsToBe(2,15);
        driver.switchTo().window(getAnotherHandle(mainHandle));

        viewEvent = PageFactory.initElements(driver, ViewEventPageHelper.class);
        viewEvent.waitUntilPageIsLoaded();

        String holidayBanner = viewEvent.getHolidayBanner();
        driver.close();
        driver.switchTo().window(mainHandle);
        return holidayBanner;
    }


}
