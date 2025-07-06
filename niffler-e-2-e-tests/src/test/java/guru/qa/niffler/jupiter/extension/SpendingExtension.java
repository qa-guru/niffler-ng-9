package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

import static guru.qa.niffler.jupiter.extension.TestMethodContextExtension.context;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);

  private final SpendApiClient spendApiClient = new SpendApiClient();
  private final SpendDbClient spendDbClient = new SpendDbClient();

  @Override
  public void beforeEach(ExtensionContext context) {
      User userAnnotation = AnnotationSupport.findAnnotation(
              context.getRequiredTestMethod(),
              User.class
      ).orElse(null);
      if (userAnnotation != null && userAnnotation.spendings().length > 0) {
          Spending spending = userAnnotation.spendings()[0];
          CategoryJson category = new CategoryJson(
                  null,
                  spending.category(),
                  userAnnotation.username(),
                  false
          );
          SpendJson spendJson = new SpendJson(
                  null,
                  new Date(),
                  category,
                  spending.currency(),
                  spending.amount(),
                  spending.description(),
                  userAnnotation.username()
          );
          context.getStore(NAMESPACE).put(
                  context.getUniqueId(),
                  spendDbClient.createSpend(spendJson)
          );
      }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
  }

  @Override
  public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return createdSpending();
  }

  public static SpendJson createdSpending() {
    final ExtensionContext methodContext = context();
    return methodContext.getStore(NAMESPACE)
            .get(methodContext.getUniqueId(), SpendJson.class);
  }
}