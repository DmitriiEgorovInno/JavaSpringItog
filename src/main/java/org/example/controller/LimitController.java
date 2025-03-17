package org.example.controller;

import org.example.Entity.Limit;
import org.example.dto.LimitResponse;
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
    public LimitResponse getLimitByUserId(@PathVariable("userId") Long userID){
        Limit limit = limitService.getLimitByUserId(userID);
        return new LimitResponse(limit);
    }

    @PostMapping(value = "/limit/reduce")
    public LimitResponse reduceUserLimit(@RequestParam("userId") Long userId, @RequestParam("sum")BigDecimal sum){
        Limit limit = limitService.reduceUserLimit(userId,sum);
        return new LimitResponse(limit);
    }

    @PostMapping("/limit/restore/{userId}")
    public LimitResponse restoreUserLimit(@PathVariable("userId") Long userId){
        Limit limit = limitService.restoreUserLimit(userId);
        return new LimitResponse(limit);
    }
}
