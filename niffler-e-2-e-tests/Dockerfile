FROM gradle:8.14-jdk21

WORKDIR /home/gradle/niffler-ng-9
ENV TZ=Europe/Moscow
COPY ./build.gradle ./
COPY ./settings.gradle ./
COPY ./gradle.properties ./

COPY ./niffler-e-2-e-tests/build.gradle ./niffler-e-2-e-tests/build.gradle
COPY ./niffler-grpc-common/build.gradle ./niffler-grpc-common/build.gradle

RUN gradle --no-daemon :niffler-e-2-e-tests:testClasses -x test

COPY ./niffler-e-2-e-tests ./niffler-e-2-e-tests
COPY ./niffler-grpc-common ./niffler-grpc-common

CMD gradle test -Dtest.env=docker -Drepository=jpa -Duser.timezone=Europe/Moscow
