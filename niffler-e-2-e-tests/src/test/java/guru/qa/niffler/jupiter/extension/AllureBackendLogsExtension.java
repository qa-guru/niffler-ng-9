package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.TestResult;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class AllureBackendLogsExtension implements SuiteExtension {

  private static final Logger LOG = LoggerFactory.getLogger(AllureBackendLogsExtension.class);

  private static final boolean inDocker = "docker".equals(System.getProperty("test.env"));
  private static final String caseName = "Niffler backend logs";
  private static final Set<String> services = Set.of(
      "niffler-auth",
      "niffler-currency",
      "niffler-gateway",
      "niffler-spend",
      "niffler-userdata"
  );

  @SneakyThrows
  @Override
  public void afterSuite() {
    LOG.info("### Collect backend logs");
    final AllureLifecycle allureLifecycle = Allure.getLifecycle();
    final String caseId = UUID.randomUUID().toString();
    allureLifecycle.scheduleTestCase(new TestResult().setUuid(caseId).setName(caseName));
    allureLifecycle.startTestCase(caseId);

    for (String serviceName : services) {
      addAttachmentForService(allureLifecycle, serviceName);
    }

    allureLifecycle.stopTestCase(caseId);
    allureLifecycle.writeTestCase(caseId);
  }

  private static void addAttachmentForService(AllureLifecycle allureLifecycle, String serviceName) throws IOException {
    final Path logPath = resolveLogPath(serviceName);
    if (!Files.exists(logPath)) {
      LOG.warn("### Backend log file not found: {}", logPath);
      return;
    }
    allureLifecycle.addAttachment(
        serviceName + " log",
        "text/html",
        ".log",
        Files.newInputStream(logPath)
    );
  }

  private static Path resolveLogPath(String serviceName) {
    final String baseDir = inDocker
        ? "/logs" :
        "./logs";
    return Path.of(baseDir, serviceName, "app.log");
  }
}
