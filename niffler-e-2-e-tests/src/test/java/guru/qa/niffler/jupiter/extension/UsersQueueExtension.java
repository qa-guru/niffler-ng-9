package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(
            String username,
            String password,
            String friend,
            String income_requests,
            String outcome_requests
    ) {
    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USERS.add(new StaticUser(
                "testUser1", "12345", null, null, null));
        WITH_FRIEND_USERS.add(new StaticUser(
                "testUser2", "23456", "testUser4", null, null));
        WITH_INCOME_REQUEST_USERS.add(new StaticUser(
                "testUser3", "34567", null, "testUser2", null));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser(
                "testUser2", "23456", null, null, "testUser3"));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type value() default Type.EMPTY;

        enum Type {
            EMPTY,
            WITH_FRIEND,
            WITH_INCOME_REQUEST,
            WITH_OUTCOME_REQUEST
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .map(p -> p.getAnnotation(UserType.class))
                .forEach(ut -> {
                            Optional<StaticUser> user = Optional.empty();
                            StopWatch sw = StopWatch.createStarted();
                            while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                                user = Optional.ofNullable(getQueueByUserType(ut).poll());
                            }
                            Allure.getLifecycle().updateTestCase(testCase ->
                                    testCase.setStart(new Date().getTime())
                            );
                            user.ifPresentOrElse(
                                    u ->
                                            ((Map<UserType, StaticUser>) context.getStore(NAMESPACE).getOrComputeIfAbsent(
                                                    context.getUniqueId(),
                                                    key -> new HashMap<>()))
                                                    .put(ut, u),
                                    () -> {
                                        throw new IllegalStateException("Can't find user after 30 sec");
                                    }
                            );
                        }
                );
    }


    @Override
    public void afterEach(ExtensionContext context) {
        Map<UsersQueueExtension.UserType, Queue<UsersQueueExtension.StaticUser>> usersMap = context.getStore(NAMESPACE).get(
                context.getUniqueId(),
                Map.class
        );
        for (Map.Entry<UsersQueueExtension.UserType, Queue<UsersQueueExtension.StaticUser>> entry : usersMap.entrySet()) {
            for (UsersQueueExtension.StaticUser user : entry.getValue()) {
                getQueueByUserType(entry.getKey()).add(user);
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public UsersQueueExtension.StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext
            extensionContext) throws ParameterResolutionException {
        Map<UsersQueueExtension.UserType, UsersQueueExtension.StaticUser> map = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
        return map.get(parameterContext.getParameter().getAnnotation(UsersQueueExtension.UserType.class));
    }

    private Queue<StaticUser> getQueueByUserType(UserType userType) {
        return switch (userType.value()) {
            case EMPTY -> EMPTY_USERS;
            case WITH_FRIEND -> WITH_FRIEND_USERS;
            case WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST_USERS;
            case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST_USERS;
        };
    }
}