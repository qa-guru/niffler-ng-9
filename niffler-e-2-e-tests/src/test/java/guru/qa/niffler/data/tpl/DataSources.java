package guru.qa.niffler.data.tpl;

import com.atomikos.jdbc.AtomikosDataSourceBean;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class DataSources {
  private DataSources() {
  }

  private static final Map<String, DataSource> dataSources = new ConcurrentHashMap<>();

  public static DataSource dataSource(String jdbcUrl) {
    return dataSources.computeIfAbsent(
        jdbcUrl,
        key -> {
          AtomikosDataSourceBean dsBean = new AtomikosDataSourceBean();
          final String uniqueId = StringUtils.substringAfter(jdbcUrl, "5432/");
          dsBean.setUniqueResourceName(uniqueId);
          dsBean.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
          Properties props = new Properties();
          props.put("URL", jdbcUrl);
          props.put("user", "postgres");
          props.put("password", "secret");
          dsBean.setXaProperties(props);
          dsBean.setMaxPoolSize(10);

          return dsBean;
        }
    );
  }
}
