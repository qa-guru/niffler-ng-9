package guru.qa.niffler.jupiter.extension;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public interface SuiteExtension extends BeforeAllCallback{
  /*
  1. Быть уверенным, что SuiteExtension будет выполняться перед каждым тестовым классом
  2. Если выполним код перед загрузкой самого первого тестового класса, то это и будет beforeSuite()
  3. Для 2-го, 3-го и т.д. тестовых классов больше не будем вызывать  beforeSuite()
  4. Когда все-все тесты завершаться, вызовем afterSuite()
   */
  @Override
  default void beforeAll(ExtensionContext context) throws Exception{
    final ExtensionContext rootContext = context.getRoot();
    rootContext.getStore(ExtensionContext.Namespace.GLOBAL)
            .getOrComputeIfAbsent(
                    this.getClass(),
                    key -> {//here only first time
                      return new AutoCloseable()  {
                        @Override
                        public void close() throws Exception{
                          afterSuite();
                        }
                      };
                    }
            );
  }

  default void beforeSuite(ExtensionContext context){}

  default void afterSuite(){}
}
