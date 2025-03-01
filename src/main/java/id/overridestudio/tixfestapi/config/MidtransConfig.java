package id.overridestudio.tixfestapi.config;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.service.MidtransCoreApi;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MidtransConfig {

    @Value("${midtrans.servey-key}")
    private String midtransServerKey;

    @Value("${midtrans.client-key}")
    private String midtransClientKey;

    @Value("${midtrans.isProduction}")
    private Boolean isProduction;

    @Bean
    public MidtransCoreApi midtransSdkConfig() {
        Config config = Config.builder()
                .setServerKey(midtransServerKey)
                .setClientKey(midtransClientKey)
                .setIsProduction(isProduction)
                .enableLog(true)
                .build();

        return new ConfigFactory(config).getCoreApi();
    }
}
