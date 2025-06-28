package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);

  private final SpendApiClient spendApiClient = new SpendApiClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
      User user = AnnotationSupport.findAnnotation(
              context.getRequiredTestMethod(),
              User.class
      ).orElse(null);
      if (user != null && user.spending().length != 0) {
          SpendJson spendJson = createSpendJson(user, user.spending()[0]);
          context.getStore(NAMESPACE).put(
              context.getUniqueId(),
              spendApiClient.addSpend(spendJson)
          );
      }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
  }

  @Override
  public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), SpendJson.class);
  }

  private SpendJson createSpendJson(User user, Spending spending) {
    CategoryJson category = new CategoryJson(
            null,
            spending.category(),
            user.username(),
            false
    );

    return new SpendJson(
            null,
            new Date(),
            category,
            spending.currency(),
            spending.amount(),
            spending.description(),
            user.username()
    );
  }
}
