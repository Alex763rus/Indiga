package com.example.indiga.model.menu.services;

import com.example.indiga.enums.ServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.example.indiga.constant.Constant.Command.*;
import static com.example.indiga.enums.ServiceType.SERVICE_2;
import static com.example.indiga.enums.ServiceType.SERVICE_4;

@Component(COMMAND_SERVICE_4)
@Slf4j
public class MenuService4 extends MenuServiceBase {

    @Override
    public String getMenuComand() {
        return COMMAND_SERVICE_4;
    }

    @Override
    public String getDescription() {
        return COMMAND_SERVICE_4;
    }

    @Override
    public ServiceType getServiceType() {
        return SERVICE_4;
    }

    @Override
    public String getServiceDescription() {
        return "_- Улучшенная навигация сайта_\n" +
                "Создание веб-сайта для вашего бизнеса поможет вам привлечь огромную аудиторию.";
    }
}
