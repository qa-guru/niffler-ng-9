package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

public class GeneratorUtils {
    Faker faker = new Faker();

    public String generateUniqueUsername() {
        return faker.name().username() + System.currentTimeMillis();
    }

    public String generateUniqueCategoryName() {
        return faker.name().title() + System.currentTimeMillis();
    }


}
