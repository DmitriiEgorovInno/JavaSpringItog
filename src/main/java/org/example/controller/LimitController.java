package org.example.controller;

import org.example.dto.Limit;
import org.example.service.LimitService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/limits")
public class LimitController {
    private final LimitService limitService;

    public LimitController(LimitService limitService) {
        this.limitService = limitService;
    }

    @GetMapping(value = "/limit/{userId}")
    public Limit getLimitByUserId(@PathVariable("userId") Long userID){
        return limitService.getLimitByUserId(userID);
    }

    @PostMapping(value = "/reducelimit")
    public Limit reduceUserLimit(@RequestParam("userId") Long userId, @RequestParam("sum")BigDecimal sum){
        return limitService.reduceUserLimit(userId,sum);
    }

    @PostMapping("/restorelimit/{userId}")
    public Limit restoreUserLimit(@PathVariable("userId") Long userId){
        return limitService.restoreUserLimit(userId);
    }
}
