package com.scfsoft.config;

import com.scfsoft.sdk.common.providers.ProviderFactory;
import com.scfsoft.system.providers.SystemSubscriberProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author James HE
 */
@Configuration
public class ProviderConfig {

    @Bean
    public ProviderFactory providerFactory(SystemSubscriberProvider systemSubscriberProvider) {
        return ProviderFactory
                .newProviderFactory()
                .setSubscriberProvider(systemSubscriberProvider);
    }

}
