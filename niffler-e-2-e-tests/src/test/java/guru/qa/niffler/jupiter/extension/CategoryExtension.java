package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.UUID;

public class CategoryExtension implements ParameterResolver, BeforeTestExecutionCallback, AfterTestExecutionCallback {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                Category.class
        ).ifPresent(
                anno -> {
                    CategoryJson categoryJson = new CategoryJson(
                            null,
                            UUID.randomUUID().toString(),
                            anno.username(),
                            anno.archived()
                    );

                    CategoryJson category = spendApiClient.addCategory(categoryJson);

                    if (anno.archived()) {
                        CategoryJson archivedCategoryJson = new CategoryJson(
                                category.id(),
                                category.name(),
                                category.username(),
                                true
                        );
                        category = spendApiClient.updateCategory(archivedCategoryJson);
                    }

                    context.getStore(NAMESPACE).put(
                            context.getUniqueId(),
                            category
                    );
                }
        );
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson certainTestCategory = context.getStore(NAMESPACE)
                .get(context.getUniqueId(), CategoryJson.class);

        if (!certainTestCategory.archived()) {
            spendApiClient.updateCategory(new CategoryJson(
                            certainTestCategory.id(),
                            certainTestCategory.name(),
                            certainTestCategory.username(),
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
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(CategoryExtension.NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }
}
