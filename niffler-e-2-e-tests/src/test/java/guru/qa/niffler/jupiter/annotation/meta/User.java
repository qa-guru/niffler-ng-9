package guru.qa.niffler.jupiter.annotation.meta;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;

public @interface User {
    Category[] categories () default {};
    Spending[] spendings () default {};
}
