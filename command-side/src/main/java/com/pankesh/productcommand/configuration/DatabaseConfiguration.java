package com.pankesh.productcommand.configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.PooledDataSource;
import com.pankesh.productcommand.configuration.properties.DatasourceProperties;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

@Configuration
public class DatabaseConfiguration {


    private static final Logger LOG = LoggerFactory.getLogger(DatabaseConfiguration.class);
    @Inject
    private DatasourceProperties dataSourceProperties;

    @Bean(destroyMethod = "close")
    public PooledDataSource dataSource(){
        try {

            ComboPooledDataSource datasource = new ComboPooledDataSource();
            datasource.setJdbcUrl(dataSourceProperties.getUrl());
            datasource.setDriverClass(dataSourceProperties.getDriverClassName());
            datasource.setUser(dataSourceProperties.getUsername());
            datasource.setPassword(dataSourceProperties.getPassword());
            // Configure connection pool
            configureConnectionPool(datasource, dataSourceProperties.getConnectionPool());
            return datasource;
        }catch (Exception e){
            return null;
        }

    }
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
            JpaTransactionManager transactionManager = new JpaTransactionManager();
            transactionManager.setEntityManagerFactory(entityManagerFactory);
            transactionManager.setDefaultTimeout(dataSourceProperties.getTransactionTimeout());
            return transactionManager;

    }

    @Bean
    @PersistenceContext(unitName = "eventStore",type = PersistenceContextType.EXTENDED)
    public EntityManagerFactory entityManagerFactory(PropertiesContainer datasourceProperties) {
             LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
            entityManagerFactoryBean.setJpaVendorAdapter(vendorAdaptor());
            entityManagerFactoryBean.setDataSource(dataSource());
            entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
            //entityManagerFactoryBean.setPackagesToScan("com.pankesh.productcommand.aggregates");
            entityManagerFactoryBean.setJpaProperties(datasourceProperties.getProperties());
            entityManagerFactoryBean.afterPropertiesSet();
            return entityManagerFactoryBean.getObject();

    }
    @Bean
    @ConfigurationProperties("datasource.properties")
    public PropertiesContainer datasourceProperties() {
        return new PropertiesContainer();
    }

    private void configureConnectionPool(ComboPooledDataSource datasource, DatasourceProperties.ConnectionPoolProperties connectionPoolProperties) {
        datasource.setMaxPoolSize(connectionPoolProperties.getMaxPoolSize());
        datasource.setMinPoolSize(connectionPoolProperties.getMinPoolSize());
        datasource.setInitialPoolSize(connectionPoolProperties.getInitialPoolSize());
        datasource.setMaxStatements(connectionPoolProperties.getMaxStatements());
        datasource.setCheckoutTimeout(connectionPoolProperties.getCheckoutTimeout());
    }
    private HibernateJpaVendorAdapter vendorAdaptor() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        return vendorAdapter;
    }

}
