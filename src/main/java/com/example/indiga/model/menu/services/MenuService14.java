package com.example.indiga.model.menu.services;

import com.example.indiga.enums.ServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.example.indiga.constant.Constant.Command.COMMAND_SERVICE_1;
import static com.example.indiga.constant.Constant.Command.COMMAND_SERVICE_14;
import static com.example.indiga.enums.ServiceType.SERVICE_14;

@Component(COMMAND_SERVICE_14)
@Slf4j
public class MenuService14 extends MenuServiceBase {

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
        return SERVICE_14;
    }

    @Override
    public String getServiceDescription() {
        return "_- Новый канал для продаж_";
    }
}
