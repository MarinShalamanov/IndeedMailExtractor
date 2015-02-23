package com.marinshalamanov.indeedextractor;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class IndeedExtractor {

	public final static String CHROME_DRIVER_PATH = "libs\\chromedriver.exe";

	public String email = null;

	public String pass = null;

	public PrintStream pw = System.out;

	private void loadCredentials() {
		Scanner in = new Scanner(System.in);
		
		System.out.print("email: ");
		email = in.nextLine();

		System.out.print("Password: ");
		pass = in.nextLine();
		
		in.close();
	}

	public boolean isValidEmail(String mail) {
		if (!mail.contains("@")) {
			return false;
		}

		if (mail.endsWith("indeedemail.com")) {
			return false;
		}

		String[] invalid = new String[] { "name@company.com", email };

		for (String i : invalid) {
			if (i.equals(mail)) {
				return false;
			}
		}

		return true;
	}

	public void extract(String baseUrl, int firstPage, int lastPage) {
		loadCredentials();
		
		
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);

		WebDriver driver = new ChromeDriver();

		List<String> candidateUrls = new ArrayList<String>();
		Set<String> mails = new HashSet<String>();

		for (int i = firstPage; i <= lastPage; i++) {
			String currUrl = baseUrl + Integer.toString(i);

			driver.get(currUrl);

			try {
				WebElement signinEmail = driver.findElement(By
						.id("signin_email"));
				if (signinEmail != null) {
					
					signinEmail.sendKeys(email);
					
					WebElement signinPass = driver.findElement(By
							.id("signin_password"));
					signinPass.sendKeys(pass);
					
					signinPass.submit();
				}
			} catch (NoSuchElementException e) {
			}

			try {
				Thread.sleep(4000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			List<WebElement> links = driver.findElements(By
					.className("candidate-link"));
			for (WebElement link : links) {
				String currCanditateUrl = link.getAttribute("href");
				candidateUrls.add(currCanditateUrl);
			}
		}

		for (String candidateUrl : candidateUrls) {
			driver.get(candidateUrl);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			Scanner scanner = new Scanner(driver.getPageSource());
			scanner.useDelimiter("[ ><\t\n\r\"':=]/");

			while (scanner.hasNext()) {
				String next = scanner.next();
				if (isValidEmail(next) && !mails.contains(next)) {
					mails.add(next);
					System.out.println(next);
					pw.println(next);
				}
			}

		}

		driver.quit();
	}
	
}
