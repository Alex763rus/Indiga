package com.example.indiga.model.menu.services;

import com.example.indiga.enums.ServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.example.indiga.constant.Constant.Command.COMMAND_SERVICE_1;
import static com.example.indiga.constant.Constant.Command.COMMAND_SERVICE_18;
import static com.example.indiga.enums.ServiceType.SERVICE_18;

@Component(COMMAND_SERVICE_18)
@Slf4j
public class MenuService18 extends MenuServiceBase {

    @Override
    public String getMenuComand() {
        return COMMAND_SERVICE_1;
    }

    @Override
    public String getDescription() {
        return COMMAND_SERVICE_1;
    }

    @Override
    public ServiceType getServiceType() {
        return SERVICE_18;
    }

    @Override
    public String getServiceDescription() {
        return "_- Знания экспертов_\n" +
                "Работа с нами – это инвестиции в ваш бизнес, которые дадут вам четкий план действий для движения в нужном направлении\n";
    }
}
