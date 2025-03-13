package org.example.service;

import org.example.config.ExecutorProperties;
import org.example.dto.Limit;
import org.example.repository.LimitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class LimitService {

    private static final Logger log = LoggerFactory.getLogger(LimitService.class);

    private final LimitRepository limitRepository;
    private final ExecutorProperties executorProperties;
    public LimitService(LimitRepository limitRepository, ExecutorProperties executorProperties) {
        this.limitRepository = limitRepository;
        this.executorProperties = executorProperties;
    }

    public Limit getLimitByUserId(Long userId){
        return limitRepository.findTopByUserIdOrderByCreatedAtDesc(userId).orElseGet(()->{
            Limit newLimit = new Limit(userId,executorProperties.getInitialUserLimit());
            return limitRepository.save(newLimit);
        });
    }

    public Limit reduceUserLimit(Long userId, BigDecimal sum){
        Limit limit = getLimitByUserId(userId);
        BigDecimal newSum = limit.getValue().subtract(sum);
        Limit newLimit = new Limit(userId,newSum);
        if (newLimit.getValue().intValue()<0)
            throw new IllegalArgumentException("Сумма превышает лимит");
        return limitRepository.save(newLimit);
    }

    public Limit restoreUserLimit(Long userId){
        Limit limit=getLimitByUserId(userId);
        if (limit.getValue().compareTo(executorProperties.getInitialUserLimit())==0)
            return new Limit(userId,executorProperties.getInitialUserLimit());
        limitRepository.delete(limit);
        return getLimitByUserId(userId);
    }

    //@Scheduled(fixedRateString = "#{executorProperties.resetInterval}") // вариант для теста
    @Scheduled(cron = "@daily") //вариант для запуска каждый день
    @Transactional
    public void resetAllLimits(){
        log.info("Обновляем все лимиты");
        limitRepository.updateAllLimits(executorProperties.getInitialUserLimit());
    }
}
