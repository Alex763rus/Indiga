package com.example.indiga.model.menu.services;

import com.example.indiga.enums.ServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.example.indiga.constant.Constant.Command.*;
import static com.example.indiga.enums.ServiceType.SERVICE_2;
import static com.example.indiga.enums.ServiceType.SERVICE_5;

@Component(COMMAND_SERVICE_5)
@Slf4j
public class MenuService5 extends MenuServiceBase {

    @Override
    public String getMenuComand() {
        return COMMAND_SERVICE_5;
    }

    @Override
    public String getDescription() {
        return COMMAND_SERVICE_5;
    }

    @Override
    public ServiceType getServiceType() {
        return SERVICE_5;
    }

    @Override
    public String getServiceDescription() {
        return "_- Продающий дизайн_\n" +
                "Мы создаем индивидуальные, удобные веб-сайты с учетом ваших конкретных потребностей.";
    }
}
