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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
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
            String outcome,
            UserType.Type type) {

    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> USERS_WITH_FRIEND = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> USERS_WITH_INCOME_REQUEST = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> USERS_WITH_OUTCOME_REQUEST = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USERS.add(new StaticUser("dima", "12345", null, null, null, UserType.Type.EMPTY));
        USERS_WITH_FRIEND.add(new StaticUser("vadim", "052322", "marina", null, null, UserType.Type.WITH_FRIEND));
        USERS_WITH_INCOME_REQUEST.add(new StaticUser("max", "12345", null, "marina", null, UserType.Type.WITH_INCOME_REQUEST));
        USERS_WITH_OUTCOME_REQUEST.add(new StaticUser("alex", "12345", null, null, "donald", UserType.Type.WITH_OUTCOME_REQUEST));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type value() default Type.EMPTY;

        enum Type {
            EMPTY, WITH_FRIEND, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
        }
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        Map<UserType, StaticUser> userMap = new HashMap<>();
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .findFirst()
                .map(p -> p.getAnnotation(UserType.class))
                .ifPresent(userType -> {
                    Optional<StaticUser> user = Optional.empty();
                    StopWatch sw = StopWatch.createStarted();
                    while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                        user = Optional.ofNullable(getQueueByUserType(userType).poll());
                    }
                    Allure.getLifecycle().updateTestCase(testCase ->
                            testCase.setStart(new Date().getTime())
                    );
                    user.ifPresentOrElse(
                            staticUser -> ((Map<UserType, StaticUser>) context.getStore(NAMESPACE)
                                    .getOrComputeIfAbsent(
                                            context.getUniqueId(),
                                            value -> new HashMap<>()
                                    )).put(userType, staticUser),
                            () -> {
                                throw new IllegalStateException("Can`t obtain user after 30s.");
                            }
                    );
                });
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        Map<UserType, StaticUser> map = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
        for (Map.Entry<UserType, StaticUser> entry : map.entrySet()) {
            Queue<StaticUser> queue = getQueueByUserType(entry.getKey());
            queue.add(entry.getValue());
        }
    }

    private Queue<StaticUser> getQueueByUserType(UserType userType) {
        return switch (userType.value()) {
            case EMPTY -> EMPTY_USERS;
            case WITH_FRIEND -> USERS_WITH_FRIEND;
            case WITH_INCOME_REQUEST -> USERS_WITH_INCOME_REQUEST;
            case WITH_OUTCOME_REQUEST -> USERS_WITH_OUTCOME_REQUEST;
        };
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Map<UserType, StaticUser> users = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
        StaticUser user = users.get(parameterContext.getParameter().getAnnotation(UserType.class));
        return user;
    }
}
