package com.araby.subscription;

import java.io.IOException;

//import org.graalvm.compiler.debug.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.araby.utils.AbstractAcceptance;
import com.araby.utils.Constant;
import com.araby.utils.MySmsClient;

public class SubscriptionTest extends AbstractAcceptance {

	/*
	 * @Test public void unSub() throws IOException { MySmsClient.unsub(); }
	 */

	public void doSubscription(WebDriver driver) throws IOException, InterruptedException {

		// Enter mobile input number
		Thread.sleep(10000);
		WebElement mobileNumberInput = driver.findElement(By.id("msisdn"));
		mobileNumberInput.sendKeys(Constant.DEFAULT_MOBILENUMBER);

		// Click subscribe now button
		WebElement subscribeBtn = driver.findElement(By.id("subscribeBtn"));
		subscribeBtn.click();

		String lastMsgPinCode = null;
//        do {
//            Thread.sleep(10000);
//            System.out.println("Fetching pincode");
//            lastMsgPinCode = MySmsClient.getCurrentPinCode(Constant.DEFAULT_SENDER);
//            System.out.println("Pincode value" + lastMsgPinCode);
//            if (lastMsgPinCode == null) {
//                System.out.println("Wait " + (Constant.TEST_INTERVAL / 1000) + " seconds");
//                Thread.sleep(Constant.TEST_INTERVAL);
//            }
//        } while (lastMsgPinCode == null);

		WebElement okBtn = driver.findElement(By.id("btnOK"));
		while (lastMsgPinCode == null) {
			Thread.sleep(10000);
			System.out.println("Fetching pincode");
			lastMsgPinCode = MySmsClient.getCurrentPinCode(Constant.DEFAULT_SENDER);
//			if (lastMsgPinCode == null) {
//
//				// If Error Message Appeared, click OK button then wait 2 minutes after that
//				// click on subscribe now button.
//				if (okBtn != null) {
//					// click on OK button
//					okBtn.click();
//					
//					// Wait 2 minutes 
//					Thread.sleep(120000);
//					
//					// get subscribe now button
//					WebElement subScribeBtn = driver.findElement(By.id("subscribeBtn"));
//					
//					// check if subscribe now button exists, click on it to 
//					if (subScribeBtn != null) {
//						subScribeBtn.click();
//					}
//
//				}
//
//			}

			System.out.println("Pincode value " + lastMsgPinCode);
		}

		// enter pin code
		driver.findElement(By.id("pinCode")).sendKeys(lastMsgPinCode);

		// click verify button (subscribe)
		driver.findElement(By.id("verifyBtn")).click();

		Thread.sleep(5000);

		if (okBtn != null)
			okBtn.click();

		Thread.sleep(5000);
		// THEN
//		Assertions.assertThat(driver.getCurrentUrl()).startsWith("https://staging-uri.araby.deals/ar");
	}
}