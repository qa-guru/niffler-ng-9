package guru.qa.niffler.service;

import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nullable;
import java.net.CookieManager;
import java.net.CookiePolicy;

public abstract class RestClient {

  protected static final Config CFG = Config.getInstance();

  private final OkHttpClient client;
  private final Retrofit retrofit;

  public RestClient(String baseUrl) {
    this(baseUrl, false, JacksonConverterFactory.create(), HttpLoggingInterceptor.Level.HEADERS, null);
  }

  public RestClient(String baseUrl, boolean followRedirect) {
    this(baseUrl, followRedirect, JacksonConverterFactory.create(), HttpLoggingInterceptor.Level.HEADERS, null);
  }

  public RestClient(String baseUrl, boolean followRedirect, Converter.Factory converterFactory) {
    this(baseUrl, followRedirect, converterFactory, HttpLoggingInterceptor.Level.HEADERS, null);
  }

  public RestClient(String baseUrl, Converter.Factory converterFactory) {
    this(baseUrl, false, converterFactory, HttpLoggingInterceptor.Level.HEADERS, null);
  }

  public RestClient(String baseUrl, boolean followRedirect, Converter.Factory converterFactory, Interceptor... interceptors) {
    this(baseUrl, followRedirect, converterFactory, HttpLoggingInterceptor.Level.HEADERS, interceptors);
  }

  public RestClient(String baseUrl, boolean followRedirect, Converter.Factory converterFactory, HttpLoggingInterceptor.Level level, @Nullable Interceptor... interceptors) {
    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
        .followRedirects(followRedirect);

    if (interceptors != null) {
      for (Interceptor interceptor : interceptors) {
        clientBuilder.addNetworkInterceptor(interceptor);
      }
    }

    clientBuilder
        .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(level))
        .cookieJar(
            new JavaNetCookieJar(
                new CookieManager(
                    ThreadSafeCookieStore.INSTANCE,
                    CookiePolicy.ACCEPT_ALL
                )

            )
        );

    this.client = clientBuilder.build();
    this.retrofit = new Retrofit.Builder()
        .client(this.client)
        .baseUrl(baseUrl)
        .addConverterFactory(converterFactory)
        .build();
  }

  protected <T> T create(final Class<T> service) {
    return this.retrofit.create(service);
  }
}
