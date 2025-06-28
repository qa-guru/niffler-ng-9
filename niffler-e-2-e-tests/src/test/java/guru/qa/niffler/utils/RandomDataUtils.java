package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomDataUtils {

    private static final Faker faker = new Faker();

    public static String randomUserName() {
        return faker.name().username();
    }

    public static String randomName() {
        return faker.name().firstName();
    }

    public static String randomSurname() {
        return faker.name().lastName();
    }

    public static String randomCategoryName() {
        return faker.beer().name();
    }

    public static String randomSentence(int wordsCount) {
        if (wordsCount <= 0) {
            throw new IllegalArgumentException("Word count must be positive");
        }
        return IntStream.range(0, wordsCount)
                .mapToObj(i -> faker.lorem().word())
                .collect(Collectors.joining(" "))
                .concat(".");
    }
}
