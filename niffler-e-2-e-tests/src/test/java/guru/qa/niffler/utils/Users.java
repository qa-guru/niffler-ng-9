package guru.qa.niffler.utils;

import java.util.Random;

public class Users {
	public static final String SYCHEV_TEST_USER_NAME = "sychevTest";
	public static final String SYCHEV_TEST_USER_PASSWORD = "12341234";

	public static String getRandomUserName() {
		return "testUser" + new Random().nextInt(10000);
	}

	public static String getRandomUserPassword() {
		return "password" + new Random().nextInt(10000);
	}
}
