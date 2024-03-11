package eu.trans.nbp.client.configuration;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OkHttpClientConfigurer {

    @Bean
    OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .build();
    }
}
