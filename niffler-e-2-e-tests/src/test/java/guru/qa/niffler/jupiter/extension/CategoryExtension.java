package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;


public class CategoryExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback, ParameterResolver {

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
                            new Faker().pokemon().name(),
                            anno.username(),
                            false
                    );

                    CategoryJson createdCategory = spendApiClient.addCategory(categoryJson);

                    if(anno.archived()) {
                        CategoryJson archivedCategoryJson = new CategoryJson(
                                createdCategory.id(),
                                createdCategory.name(),
                                createdCategory.username(),
                                true
                        );
                        createdCategory = spendApiClient.updateCategory(archivedCategoryJson);
                    }
                    context.getStore(NAMESPACE).put(
                            context.getUniqueId(),
                            createdCategory
                    );
                }
        );
    }

     @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson categoryJson = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (!categoryJson.archived()) {
            CategoryJson archivedCategoryJson = new CategoryJson(
                    categoryJson.id(),
                    categoryJson.name(),
                    categoryJson.username(),
                    true
            );
            spendApiClient.updateCategory(archivedCategoryJson);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(CategoryExtension.NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }
}