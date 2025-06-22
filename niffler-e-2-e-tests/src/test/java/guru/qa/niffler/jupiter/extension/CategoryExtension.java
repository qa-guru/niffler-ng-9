package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class CategoryExtension implements BeforeEachCallback, AfterEachCallback,ParameterResolver {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                Category.class
        ).ifPresent(
                anno -> {
                    String uniqueName = anno.name() + "_" + UUID.randomUUID();

                    CategoryJson categoryJson = new CategoryJson(
                            UUID.randomUUID(),
                            uniqueName,
                            anno.username(),
                            anno.isArchive()
                    );
                    context.getStore(NAMESPACE).put(
                            context.getUniqueId(),
                            spendApiClient.createCategory(categoryJson)
                    );
                }
        );
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                Category.class
        ).ifPresent(anno -> {
            CategoryJson createdCategory = context.getStore(NAMESPACE)
                    .get(context.getUniqueId(), CategoryJson.class);

            if (createdCategory != null) {
                CategoryJson updatedCategory = new CategoryJson(
                        createdCategory.id(),
                        createdCategory.name(),
                        createdCategory.username(),
                        true
                );
                spendApiClient.updateCategory(updatedCategory);
            }
        });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryExtension.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryExtension.class);
    }
}
