package com.ibrahim.orderservice.controller;

import com.ibrahim.orderservice.dto.OrderDto;
import com.ibrahim.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/addOrder")
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallBackMethod")
    /*@TimeLimiter(name = "inventory")
    @Retry(name = "inventory")*/
    public CompletableFuture<String> createOrder(@RequestBody OrderDto orderDto){
        log.info("Placing Order------------------------hdfhghdgdhhgd-hdggd-èè------------");
        return CompletableFuture.supplyAsync(()->orderService.createOrder(orderDto));
    }
    public CompletableFuture<String>  fallBackMethod(OrderDto orderDto, RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(()->"Ooops! Something went wrong, please order after some time!");
    }
}
