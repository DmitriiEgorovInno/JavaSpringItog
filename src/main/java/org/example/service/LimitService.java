package org.example.service;

import org.example.config.LimitProperties;
import org.example.Entity.Limit;
import org.example.dto.LimitResponse;
import org.example.repository.LimitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class LimitService {

    private static final Logger log = LoggerFactory.getLogger(LimitService.class);

    private final LimitRepository limitRepository;
    private final LimitProperties limitProperties;

    public LimitService(LimitRepository limitRepository, LimitProperties limitProperties) {
        this.limitRepository = limitRepository;
        this.limitProperties = limitProperties;
    }

    public Limit getLimitByUserId(Long userId){
        return limitRepository.findTopByUserIdOrderByCreatedAtDesc(userId)
                .orElseGet(()-> {
                    Limit newLimit = new Limit(userId, limitProperties.getInitialUserLimit());
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
        //нашли последнюю созданную запись с лимитом по юзеру
        Limit limit=getLimitByUserId(userId);
        log.info("Последний созданный лимит по юзеру "+limit);
        // если лимит уже 10000 его не надо восстанавливать, поэтому просто вернем его
        if (limit.getValue().compareTo(limitProperties.getInitialUserLimit())==0){
            log.info("Последняя запись лимита по юзеру 10000, восстанавливать не нужно "+limit);
            return limit;}
        //иначе если платеж не прошел удаляем последнюю созданную запись
        log.info("Удаляем последнюю запись лимита по юзеру "+limit);
        limitRepository.delete(limit);
        // возвращаем последнюю запись лимита по юзеру после удаления
        return getLimitByUserId(userId);
    }

    //@Scheduled(fixedRateString = "#{executorProperties.resetInterval}") // вариант для теста
    @Scheduled(cron = "@daily") //вариант для запуска каждый день
    @Transactional
    public void resetAllLimits(){
        log.info("Обновляем все лимиты");
        limitRepository.updateAllLimits(limitProperties.getInitialUserLimit());
    }
}
