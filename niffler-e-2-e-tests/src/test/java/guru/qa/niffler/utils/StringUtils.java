package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

public class StringUtils {

    public static String getRandomName() {
        return new Faker().funnyName().name();
    }
}
