// Declare the package this configuration class belongs to.
package org.example.Q4;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

// Import ObjectProvider so Spring can supply prototype instances on demand.
import org.springframework.beans.factory.ObjectProvider;

// Import @Bean to register factory methods as Spring-managed beans.
import org.springframework.context.annotation.Bean;

// Import @Configuration to mark this class as a Spring configuration source.
import org.springframework.context.annotation.Configuration;

// Import @Scope to control bean lifecycle (singleton vs prototype).
import org.springframework.context.annotation.Scope;

// Import Spring's predefined scope constants.
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

// Mark this class as a Spring Java-based configuration.
@Configuration
public class AppConfig {

    // Register Table as a prototype bean so each request returns a fresh instance.
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Table table() {
        // Create and return a new Table instance managed by Spring.
        return new Table();
    }

    // Register Client as a prototype bean so each request returns a fresh instance.
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Client client() {
        // Create and return a new Client instance managed by Spring.
        return new Client();
    }

    // Register Restaurant as a singleton bean that depends on Table/Client providers.
    @Bean
    public Restaurant restaurant(ObjectProvider<Table> tableProvider,
                                 ObjectProvider<Client> clientProvider) {

        // Create the Restaurant and inject providers so it can request prototypes later.
        return new Restaurant(2, 3, 1, tableProvider, clientProvider);
    }
}
