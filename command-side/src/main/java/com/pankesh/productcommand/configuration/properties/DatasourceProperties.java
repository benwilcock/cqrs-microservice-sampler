package com.pankesh.productcommand.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "datasource",ignoreUnknownFields = true)
public class DatasourceProperties {

    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private int transactionTimeout = 5;
    private ConnectionPoolProperties connectionPool = new ConnectionPoolProperties();

    public ConnectionPoolProperties getConnectionPool() {
        return connectionPool;
    }

    public void setConnectionPool(ConnectionPoolProperties connectionPool) {
        this.connectionPool = connectionPool;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public int getTransactionTimeout() {
        return transactionTimeout;
    }

    public void setTransactionTimeout(int transactionTimeout) {
        this.transactionTimeout = transactionTimeout;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static class ConnectionPoolProperties {
        private int initialPoolSize = 20;
        private int maxPoolSize = 20;
        private int minPoolSize = 20;
        private int maxStatements = 50;
        private int checkoutTimeout = 6000;

        public int getInitialPoolSize() {
            return initialPoolSize;
        }

        public void setInitialPoolSize(int initialPoolSize) {
            this.initialPoolSize = initialPoolSize;
        }

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getMinPoolSize() {
            return minPoolSize;
        }

        public void setMinPoolSize(int minPoolSize) {
            this.minPoolSize = minPoolSize;
        }

        public int getMaxStatements() {
            return maxStatements;
        }

        public void setMaxStatements(int maxStatements) {
            this.maxStatements = maxStatements;
        }

        public int getCheckoutTimeout() {
            return checkoutTimeout;
        }

        public void setCheckoutTimeout(int checkoutTimeout) {
            this.checkoutTimeout = checkoutTimeout;
        }
    }
}
