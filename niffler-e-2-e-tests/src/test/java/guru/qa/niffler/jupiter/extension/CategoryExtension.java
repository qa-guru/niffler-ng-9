package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final SpendApiClient spendApiClient = new SpendApiClient();
    private static final Faker faker = new Faker();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                Category.class
        ).ifPresent(anno -> {
                    CategoryJson categoryJson = spendApiClient.addCategory(new CategoryJson(null, faker.funnyName().name(), anno.userName(), anno.archived()));
                    if (anno.archived()) {
                        CategoryJson archived = new CategoryJson(categoryJson.id(), categoryJson.name(), categoryJson.username(), false);
                        categoryJson = spendApiClient.updateCategory(archived);
                    }
                    context.getStore(NAMESPACE).put(
                            context.getUniqueId(),
                            categoryJson
                    );
                }
        );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(CategoryExtension.NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson categoryJson = context.getStore(CategoryExtension.NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (!categoryJson.archived()) {
            CategoryJson archived = new CategoryJson(categoryJson.id(), categoryJson.name(), categoryJson.username(), true);
            spendApiClient.updateCategory(archived);
        }
    }
}
