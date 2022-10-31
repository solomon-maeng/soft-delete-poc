package com.rebwon.softdeletepoc;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@TestConfiguration
public class TestConfig {

    @Bean
    public TransactionTemplate transactionTemplate(
        PlatformTransactionManager platformTransactionManager) {
        var template = new TransactionTemplate();
        template.setTransactionManager(platformTransactionManager);
        template.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        template.setTimeout(TransactionDefinition.TIMEOUT_DEFAULT);
        return template;
    }
}
