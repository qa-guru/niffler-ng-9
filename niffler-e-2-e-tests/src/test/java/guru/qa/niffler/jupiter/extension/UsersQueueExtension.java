package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements
    BeforeTestExecutionCallback,
    AfterTestExecutionCallback,
    ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(
        String username,
        String password,
        String friend,
        String income,
        String outcome) {}
  private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
  private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
  private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
  private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();

static {
    EMPTY_USERS.add(new StaticUser("duck", "12345", null, null, null));
    WITH_FRIEND_USERS.add(new StaticUser("friend1", "12345", "friend2", null, null));
    WITH_INCOME_REQUEST_USERS.add(new StaticUser("sam", "12345", null, "masha", null));
    WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("masha", "12345", null, null, "sam"));
}

  @Target(ElementType.PARAMETER)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface UserType {
      Type value () default Type.EMPTY;
      enum Type {
          EMPTY, WITH_FRIEND, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
      }
  }

  private Optional<StaticUser> selectUser(UserType.Type type) {
      switch (type) {
          case EMPTY -> {
              return Optional.ofNullable(EMPTY_USERS.poll());
          }
          case WITH_FRIEND -> {
              return Optional.ofNullable((WITH_FRIEND_USERS).poll());
          }
          case WITH_INCOME_REQUEST -> {
              return Optional.ofNullable((WITH_INCOME_REQUEST_USERS).poll());
          }
          case WITH_OUTCOME_REQUEST -> {
              return Optional.ofNullable((WITH_OUTCOME_REQUEST_USERS).poll());
          }
          default -> {
              return Optional.empty();
          }
      }
  }

  @Override
  public void beforeTestExecution(ExtensionContext context) {
    Arrays.stream(context.getRequiredTestMethod().getParameters())
        .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
        .findFirst()
        .map(p -> p.getAnnotation(UserType.class))
        .ifPresent(ut -> {
          Optional<StaticUser> user = Optional.empty();
          StopWatch sw = StopWatch.createStarted();
          while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                user = selectUser(ut.value());
              }

          Allure.getLifecycle().updateTestCase(testCase ->
              testCase.setStart(new Date().getTime())
          );
          user.ifPresentOrElse(
              u ->
                      ((Map<UserType, StaticUser>) context.getStore (NAMESPACE)
                              .getOrComputeIfAbsent (
                                      context.getUniqueId (), key -> new HashMap<> ()
                              )).put (ut, u),
//                  context.getStore(NAMESPACE).put(
//                      context.getUniqueId(),
//                      u
//                  ),
              () -> {
                throw new IllegalStateException("Can`t obtain user after 30s.");
              }
          );
        });
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
      Map<UserType, StaticUser> map = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
      for (Map.Entry<UserType, StaticUser> e : map.entrySet()) {
          if (e.getKey().value().equals(UserType.Type.EMPTY)) EMPTY_USERS.add(e.getValue());
          if (e.getKey().value().equals(UserType.Type.WITH_FRIEND)) WITH_FRIEND_USERS.add(e.getValue());
          if (e.getKey().value().equals(UserType.Type.WITH_INCOME_REQUEST)) WITH_INCOME_REQUEST_USERS.add(e.getValue());
          if (e.getKey().value().equals(UserType.Type.WITH_OUTCOME_REQUEST))
              WITH_OUTCOME_REQUEST_USERS.add(e.getValue());

      }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
        && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
  }

  @Override
  public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
      Map<UserType, StaticUser> userMap = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);

      if (userMap == null) {
          throw new ParameterResolutionException("User map not found in ExtensionContext store.");
      }

      UserType userType = extractUserType(parameterContext);

      StaticUser user = userMap.get(userType);
      if (user == null) {
          throw new ParameterResolutionException("No StaticUser found for UserType: " + userType);
      }

      return user;
  }

    private UserType extractUserType(ParameterContext parameterContext) {
        UserType annotation = parameterContext.getParameter().getAnnotation(UserType.class);
        if (annotation == null) {
            throw new ParameterResolutionException("Missing @UserTypeSource annotation on parameter.");
        }
        return annotation;
    }
}
