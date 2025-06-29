package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements BeforeEachCallback,  AfterTestExecutionCallback, ParameterResolver{

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
    private final SpendApiClient spendApiClient = new SpendApiClient();
    Faker faker = new Faker();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                Category.class
        ).ifPresent(
                anno -> {
                    CategoryJson categoryJson = new CategoryJson(
                            null,
                            faker.regexify("[a-z]{6}"),
                            anno.username(),
                            false
                    );
                    CategoryJson createdCategory = spendApiClient.addCategory(categoryJson);

                    if (anno.archived()) {
                        CategoryJson archivedCategoryJson = new CategoryJson(
                                createdCategory.id(),
                                createdCategory.name(),
                                createdCategory.username(),
                                true
                        );
                        createdCategory = spendApiClient.updateCategory(archivedCategoryJson);
                    }

                    context.getStore(NAMESPACE).put(context.getUniqueId(), createdCategory);
                }
        );
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson category =
                context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);

        if (!category.archived()) {
            spendApiClient.updateCategory(
                    new CategoryJson(
                            category.id(),
                            category.name(),
                            category.username(),
                            true
                    )
            );
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }
}
