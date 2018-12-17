package com.ex.forblog;

import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class AppEventHandler {

    @EventListener
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public void handle(AppEvent event){
        System.out.println(Thread.currentThread().toString());
        System.out.println("AppEventHandler = " +event.getData());
    }
}
