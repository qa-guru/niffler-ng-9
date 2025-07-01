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
import java.util.stream.IntStream;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.TYPE.*;

public class UsersQueueExtension implements
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(String username, String password, String friend, String income, String outcome) {
    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIENDS_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USERS.add(new StaticUser("userEmpty1", "12345", null, null, null));
        EMPTY_USERS.add(new StaticUser("userEmpty2", "12345", null, null, null));
        WITH_FRIENDS_USERS.add(new StaticUser("userWithFriend1", "12345", "userWithFriend2", null, null));
        WITH_FRIENDS_USERS.add(new StaticUser("userWithFriend2", "12345", "userWithFriend1", null, null));
        WITH_INCOME_REQUEST_USERS.add(new StaticUser("userWithIncomeRequest", "12345", null, "userWithOutcomeRequest", null));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("userWithOutcomeRequest", "12345", null, null, "userWithIncomeRequest"));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        TYPE value() default EMPTY;

        enum TYPE {
            EMPTY, WITH_FRIEND, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
        }
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        Map<Integer, Map<UserType.TYPE, StaticUser>> users = new LinkedHashMap<>();

        List<UserType> userTypes = Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .map(p -> p.getAnnotation(UserType.class))
                .toList();

        IntStream.range(0, userTypes.size())
                .forEach(i -> {
                    Optional<StaticUser> user = Optional.empty();
                    StopWatch sw = StopWatch.createStarted();
                    while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                        switch (userTypes.get(i).value()) {
                            case EMPTY:
                                user = Optional.ofNullable(EMPTY_USERS.poll());
                                user.ifPresent(u -> users.put(i, Map.of(EMPTY, u)));
                                break;
                            case WITH_FRIEND:
                                user = Optional.ofNullable(WITH_FRIENDS_USERS.poll());
                                user.ifPresent(u -> users.put(i, Map.of(WITH_FRIEND, u)));
                                break;
                            case WITH_INCOME_REQUEST:
                                user = Optional.ofNullable(WITH_INCOME_REQUEST_USERS.poll());
                                user.ifPresent(u -> users.put(i, Map.of(WITH_INCOME_REQUEST, u)));
                                break;
                            case WITH_OUTCOME_REQUEST:
                                user = Optional.ofNullable(WITH_OUTCOME_REQUEST_USERS.poll());
                                user.ifPresent(u -> users.put(i, Map.of(WITH_OUTCOME_REQUEST, u)));
                                break;
                        }
                    }
                });

        Allure.getLifecycle().updateTestCase(testCase ->
                testCase.setStart(new Date().getTime())
        );

        if (!users.isEmpty()) {
            context.getStore(NAMESPACE).put(
                    context.getUniqueId(),
                    users);
        } else {
            throw new IllegalStateException("Can`t obtain users after 30s.");
        }
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        Map<Integer, Map<UserType.TYPE, StaticUser>> users = context.getStore(NAMESPACE).get(
                context.getUniqueId(),
                HashMap.class
        );
        if (!users.isEmpty()) {
            users.values().forEach(map -> {
                map.forEach((key, value) -> {
                    switch (key) {
                        case EMPTY:
                            EMPTY_USERS.add(value);
                            break;
                        case WITH_FRIEND:
                            WITH_FRIENDS_USERS.add(value);
                            break;
                        case WITH_INCOME_REQUEST:
                            WITH_INCOME_REQUEST_USERS.add(value);
                            break;
                        case WITH_OUTCOME_REQUEST:
                            WITH_OUTCOME_REQUEST_USERS.add(value);
                            break;
                    }
                });
            });
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Map<Integer, Map<UserType.TYPE, StaticUser>> users = (Map<Integer, Map<UserType.TYPE, StaticUser>>) extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId());
        return users.get(parameterContext.getIndex()).get(AnnotationSupport.findAnnotation(parameterContext.getParameter(), UserType.class)
                .get().value());
    }


}
