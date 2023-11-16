package com.example.indiga.model.menu.services;

import com.example.indiga.enums.ServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.example.indiga.constant.Constant.Command.*;
import static com.example.indiga.enums.ServiceType.SERVICE_2;
import static com.example.indiga.enums.ServiceType.SERVICE_3;

@Component(COMMAND_SERVICE_3)
@Slf4j
public class MenuService3 extends MenuServiceBase {

    @Override
    public String getMenuComand() {
        return COMMAND_SERVICE_3;
    }

    @Override
    public String getDescription() {
        return COMMAND_SERVICE_3;
    }

    @Override
    public ServiceType getServiceType() {
        return SERVICE_3;
    }

    @Override
    public String getServiceDescription() {
        return "_- Карточка товара на сайте_\n" +
                "Часто используется в презентациях, образовательных видео, инфографиках, демонстрациях продуктов и услуг.";
    }
}
