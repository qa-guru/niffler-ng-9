package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements
    BeforeEachCallback,
    AfterTestExecutionCallback,
    ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

  private final SpendApiClient spendApiClient = new SpendApiClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
      User user = AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class).orElse(null);
      if (user != null && user.categories().length != 0) {
          CategoryJson createdCategory = createCategory(user);
          if (user.categories()[0].archived()) {
              createdCategory = archiveCategory(createdCategory);
          }
          context.getStore(NAMESPACE).put(context.getUniqueId(), createdCategory);
      }
  }

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {
    CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
    if (category != null && !category.archived()) {
      category = new CategoryJson(
          category.id(),
          category.name(),
          category.username(),
          true
      );
      spendApiClient.updateCategory(category);
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

  private CategoryJson createCategory(User user) {
      CategoryJson categoryJson = new CategoryJson(
              null,
              RandomDataUtils.randomCategoryName(),
              user.username(),
              false
      );
      return spendApiClient.createCategory(categoryJson);
  }

  private CategoryJson archiveCategory(CategoryJson category) {
      CategoryJson archiveCategory = new CategoryJson(
              category.id(),
              category.name(),
              category.username(),
              true
      );
      return spendApiClient.updateCategory(archiveCategory);
  }
}
