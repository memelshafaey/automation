package com.araby;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.araby.subscription.SubscriptionTest;
import com.araby.utils.Constant;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ArabydealsTest {

	WebDriver driver;
	SubscriptionTest subscription;

	@BeforeTest
	public void Test() {

		WebDriverManager.chromedriver().setup();
		this.driver = new ChromeDriver();
		driver.get(Constant.DEFAULT_LANDING);
		driver.manage().window().maximize();
	}

	@Test(priority = 1)
	public void login_user() throws InterruptedException {
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

	public void logout() throws InterruptedException {
		Thread.sleep(5000);
		driver.findElement(By.linkText("اهلاَ 201144383361")).click();
		driver.findElement(By.linkText("تسجيل الخروج")).click();
	}

	@Test(priority = 2)
	public void homePageTitle() throws InterruptedException {
		String expectedTitle;
		String actualTitle = driver.getTitle();

		// get the URL to check page's culture
		boolean arabic = driver.getCurrentUrl().contains("ar");

		if (arabic)
			expectedTitle = "الصفحة الرئيسية - Araby.Deals";
		else
			expectedTitle = "Home Page - Araby.Deals";

		Assert.assertEquals(actualTitle, expectedTitle, "Title is not correct ");
	}

	@Test(priority = 3)
	public void viewCoupon() throws InterruptedException {
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

	@Test(priority = 4)
	public void checkAllStoresURL() {
		// check All Stores URL
		WebElement allStore_Btn = driver.findElement(By.cssSelector(
				"body > div.container-fluid > div > div.col-xl-3.col-lg-3.col-md-3.col-sm-3.col-xs-12.d-none.d-sm-block > div.side-stores > ul > li.nav-item.active > a"));
		allStore_Btn.click();
		Assert.assertEquals(checkURL("/stores?st=KD9yOzgv-F0"), true, "All Store URL is not correct");
	}

	// checkCategoriesURL
	@Test(priority = 5)
	public void checkCategoriesURL() {
		// all categories button
		driver.findElement(By.id("categoriesDropDown")).click();
		driver.findElement(By.cssSelector(
				"body > header > div.middle-navbar > nav > div > div.userAreaAndSearch > div > ul > li.nav-item.dropdown.show > div > a:nth-child(1)"))
				.click();
		Assert.assertEquals(checkURL("/categories"), true, "All Categories URL is not correct");

	}

	// checkStoreIdURL
	@Test(priority = 6)
	public void checkStoreIdURL() {
		// store click url
		driver.findElement(By.cssSelector(
				"body > div.container-fluid > div > div.col-xl-3.col-lg-3.col-md-3.col-sm-3.col-xs-12.d-none.d-sm-block > div.side-stores > ul > li:nth-child(3) > a"))
				.click();

		Assert.assertEquals(checkURL("/stores?st="), true, "Store ID URL is not correct");
	}

	@Test(priority = 7)
	public void checkSearchURL() {
		driver.findElement(By.id("searchKeywordDesktop")).click();
		driver.findElement(By.id("searchKeywordDesktop")).sendKeys("jumia");
		driver.findElement(By.cssSelector(
				"body > header > div.middle-navbar > nav > div > div.userAreaAndSearch > div > ul > li.nav-item.formContainer.d-none.d-sm-block > form > button"))
				.click();

		Assert.assertEquals(checkURL("jumia/_s"), true, "Searh result URL is not correct");
	}

	/*
	 * 
	 * // TO BE CONTINUE SEO ALL URLs
	 */
	private boolean checkURL(String keyword) {
		return driver.getCurrentUrl().contains(keyword);
	}

	// CHECK URLS
	@Test(priority = 8)
	public void checkPrivacyURL() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".mr-auto > .nav-item:nth-child(3) > .nav-link")).click();
		Assert.assertEquals(checkURL("/Privacy"), true, "Privacy URL is not correct");

	}

	@Test(priority = 9)
	public void checkAboutArabyDealsURL() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".left-menu > .nav-item:nth-child(2) > .nav-link")).click();
		Assert.assertEquals(checkURL("/About"), true, "About Araby Deals URL is not correct");
	}

	@Test(priority = 10)
	public void checkCultureURL() throws InterruptedException {
		Thread.sleep(2000);
		boolean EGY_Culture = driver.getCurrentUrl().contains("ar-EG");
		if (EGY_Culture) {
			System.out.print("Araby Deals Culture = Egypt AR ");
		} else {
			boolean UAE_culture = driver.getCurrentUrl().contains("ar-AE");
			if (UAE_culture) {
				System.out.print("Araby Deals Culture = UAE AR ");
			} else {
				System.out.print("Araby Deals Culture = UAE En ");
			}
		}

	}

	@Test(priority = 11)
	public void loginPopUp() throws InterruptedException {

		// wait 5 seconds
		Thread.sleep(5000);

		// click on sign button
		driver.findElement(By.cssSelector(".d-none > .btn")).click();
		Thread.sleep(3000);
		// forget password button
		driver.findElement(By.cssSelector(".forgetBtn > a")).click();

		// resend password
		driver.findElement(By.cssSelector(".btn_resendPassword")).click();

	}

}
