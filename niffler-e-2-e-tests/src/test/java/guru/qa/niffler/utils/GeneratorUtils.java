package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

public class GeneratorUtils {
    private static final Faker faker = new Faker();

    public static String generateUniqueUsername() {
        return faker.name().username() + System.currentTimeMillis();
    }

    public static String generateUniqueCategoryName() {
        return faker.name().title() + System.currentTimeMillis();
    }


}
