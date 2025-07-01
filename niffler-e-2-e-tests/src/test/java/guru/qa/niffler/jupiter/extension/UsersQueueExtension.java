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

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;

public class UsersQueueExtension implements BeforeEachCallback,
        AfterEachCallback,
        ParameterResolver {

    public final static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);
    private static final String userPassword = "secret";

    public record StaticUser(
            String username,
            String password,
            String friend,
            String income,
            String outcome
    ) {}

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUESTS_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUESTS_USERS = new ConcurrentLinkedQueue<>();

    static{
        EMPTY_USERS.add(new StaticUser("user_without_friends", userPassword, null, null, null));
        WITH_FRIEND_USERS.add(new StaticUser("user_with_friend", userPassword, "test1", null, null));
        WITH_INCOME_REQUESTS_USERS.add(new StaticUser("user_with_income", userPassword, null, "test1", null));
        WITH_OUTCOME_REQUESTS_USERS.add(new StaticUser("user_with_outcome", userPassword, null, null, "test1"));
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    public @interface UserType {
        Type value() default EMPTY;

        enum Type {
            EMPTY, WITH_FRIEND, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
        }
    }

    private Queue<StaticUser> getQueueByUserType(UserType userType) {
        return switch (userType.value()) {
            case EMPTY -> EMPTY_USERS;
            case WITH_FRIEND -> WITH_FRIEND_USERS;
            case WITH_INCOME_REQUEST -> WITH_INCOME_REQUESTS_USERS;
            case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUESTS_USERS;
        };
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Map<UserType, Queue<StaticUser>> usersMap = new HashMap<>();

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .map(p -> p.getAnnotation(UserType.class))
                .forEach(
                        userType -> {
                            Optional<StaticUser> user = Optional.empty();
                            StopWatch sw = StopWatch.createStarted();
                            while(user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                                user = Optional.ofNullable(getQueueByUserType(userType).poll());
                            }
                            Allure.getLifecycle().updateTestCase(testCase -> {
                                    testCase.setStart(new Date().getTime());
                            });
                            user.ifPresentOrElse(
                                    u -> usersMap.computeIfAbsent(userType,
                                            k -> new ConcurrentLinkedQueue<>()).add(u),
                                    () -> {throw new IllegalStateException("Can`t obtain user after 30s.");}
                            );
                        });
        context.getStore(NAMESPACE).put(
                context.getUniqueId(),
                usersMap
        );
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Map<UserType, Queue<StaticUser>> usersMap = context.getStore(NAMESPACE).get(
                context.getUniqueId(),
                HashMap.class
        );
        for (Map.Entry<UserType, Queue<StaticUser>> entry : usersMap.entrySet()) {
            if(!entry.getValue().isEmpty()){
                for (StaticUser user : entry.getValue()) {
                    getQueueByUserType(entry.getKey()).add(user);
                }
            } else throw new NullPointerException("No users were found in queue");
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        HashMap<UserType, Queue<StaticUser>> usersMap = extensionContext.getStore(NAMESPACE).get(
                extensionContext.getUniqueId(),
                HashMap.class);
        UserType annotation = parameterContext.getParameter().getAnnotation(UserType.class);

        if (usersMap != null && usersMap.containsKey(annotation)) {
            usersMap.get(annotation).add(usersMap.get(annotation).peek());
            return usersMap.get(annotation).poll();
        } else throw new IllegalStateException("User is not present in current Queue");
    }
}
