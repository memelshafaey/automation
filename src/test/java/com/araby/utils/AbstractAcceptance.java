package com.araby.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import io.github.bonigarcia.wdm.WebDriverManager;

public class AbstractAcceptance {

	public static WebDriver driver;

	@BeforeClass // == before all classes
	@Parameters({ "browser" })
	public void openBrowser(@Optional String chrome) {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		//driver.manage().window().maximize();
		driver.get(Constant.DEFAULT_LANDING);
	}

//	public static void openBrowser() {
//
//		WebDriverManager.chromedriver().setup();
//		WebDriver driver = new ChromeDriver();
//		driver.get("https://staging-uri.araby.deals/ar-EG/");
//		driver.manage().window().maximize();
//
//	}

	@AfterClass()
	public void destroy() {
		driver.close();
	}

}
