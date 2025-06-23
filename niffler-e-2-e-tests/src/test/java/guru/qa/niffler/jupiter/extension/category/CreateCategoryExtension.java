package guru.qa.niffler.jupiter.extension.category;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Random;

public class CreateCategoryExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {
	public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateCategoryExtension.class);
	private final SpendApiClient spendApiClient = new SpendApiClient();

	@Override
	public void beforeEach(ExtensionContext context) {
		AnnotationSupport.findAnnotation(
				context.getRequiredTestMethod(),
				Category.class
		).ifPresent(
				category -> {
					String categoryName = "Category Name " + new Random().nextInt(10000);
					CategoryJson categoryJson = new CategoryJson(
							null,
							categoryName,
							category.username(),
							category.isArchived()
					);
					CategoryJson createdCategory = spendApiClient.addCategory(categoryJson);
					if (category.isArchived()) {
						CategoryJson archivedCategory = new CategoryJson(
								createdCategory.id(),
								categoryName,
								category.username(),
								true
						);
						createdCategory = spendApiClient.updateCategory(archivedCategory);
					}
					context.getStore(NAMESPACE).put(
							context.getUniqueId(),
							createdCategory
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
		return extensionContext.getStore(CreateCategoryExtension.NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
	}

	@Override
	public void afterTestExecution(ExtensionContext context) {
		AnnotationSupport.findAnnotation(
				context.getRequiredTestMethod(),
				Category.class
		).ifPresent(category -> {
			CategoryJson createdCategory = (CategoryJson) context.getStore(NAMESPACE).get(context.getUniqueId());
			if (!createdCategory.archived()) {
				CategoryJson archivedCategory = new CategoryJson(
						createdCategory.id(),
						createdCategory.name(),
						createdCategory.username(),
						true
				);
				CategoryJson updatedCategory = spendApiClient.updateCategory(archivedCategory);
				context.getStore(NAMESPACE).put(
						context.getUniqueId(),
						updatedCategory
				);
			}
		});
	}
}
