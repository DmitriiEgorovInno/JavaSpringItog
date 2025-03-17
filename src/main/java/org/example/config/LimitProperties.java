package org.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "limit")
public class LimitProperties {
    private BigDecimal initialUserLimit;
    private Long resetInterval;

    public BigDecimal getInitialUserLimit(){
        return initialUserLimit;
    }

    public void setInitialUserLimit(BigDecimal initialUserLimit){
        this.initialUserLimit=initialUserLimit;
    }

    public Long getResetInterval(){
        return resetInterval;
    }

    public void setResetInterval(Long resetInterval){
        this.resetInterval=resetInterval;
    }
}
