package com.araby.functions;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.araby.subscription.SubscriptionTest;
import com.araby.utils.Constant;

public class Portal {
	WebDriver driver;
	SubscriptionTest subscription;

	public void Login(WebDriver driver) throws InterruptedException {

		Thread.sleep(5000);

		// sign in button
		WebElement signIn_Btn = driver.findElement(By.cssSelector(".d-none > .btn"));
		signIn_Btn.click();

		Thread.sleep(4000);
		// enter user name
		WebElement username_Input = driver.findElement(By.id("usernameInput"));
		username_Input.click();
		username_Input.sendKeys(Constant.EG_MOBINIL_TESTNUMBER);

		Thread.sleep(4000);
		// enter password
		WebElement password_Input = driver.findElement(By.id("passwordInput"));
		password_Input.click();
		password_Input.sendKeys("2795");

		WebElement login_Btn = driver.findElement(By.id("loginBtn"));
		login_Btn.click();

	}

	public void logout(WebDriver driver) throws InterruptedException {
		Thread.sleep(5000);
		driver.findElement(By.linkText("اهلاَ 201144383361")).click();
		driver.findElement(By.linkText("تسجيل الخروج")).click();
	}

	public void AllStoreURL(WebDriver driver) {
		// check All Stores URL
		WebElement allStore_Btn = driver.findElement(By.cssSelector(
				"body > div.container-fluid > div > div.col-xl-3.col-lg-3.col-md-3.col-sm-3.col-xs-12.d-none.d-sm-block > div.side-stores > ul > li.nav-item.active > a"));
		allStore_Btn.click();
		Assert.assertEquals(checkURL("/stores?st=KD9yOzgv-F0"), true, "All Store URL is not correct");

	}

	public void homePageTitle(WebDriver driver) {
		String expectedTitle;
		String actualTitle = driver.getTitle();

		// get the URL to check page's culture
		boolean arabic = driver.getCurrentUrl().contains("ar");

		if (arabic)
			expectedTitle = "الص�?حة الرئيسية - Araby.Deals";
		else
			expectedTitle = "Home Page - Araby.Deals";

		Assert.assertEquals(actualTitle, expectedTitle, "Title is not correct ");
	}

	public void viewCoupon(WebDriver driver) throws InterruptedException {
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".col-lg-3:nth-child(1) .button")).click();
		// get the URL to check page's culture
		boolean URLtitle = driver.getCurrentUrl().contains("landing");

		if (URLtitle) {
			// subscribe
			try {
				subscription.doSubscription(driver);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			// click on coupon to go to store link
			Thread.sleep(3000);
			driver.findElement(By.cssSelector(".clickToCopyBtn")).click();
			Thread.sleep(10000);
			driver.navigate().back();
		}
	}

	private boolean checkURL(String keyword) {
		return driver.getCurrentUrl().contains(keyword);
	}

}
