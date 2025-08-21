package dev.gababartnicka.kotlinwebjourney.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@ConditionalOnProperty(value = "auditing.enabled", havingValue = "true", matchIfMissing = true)
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
