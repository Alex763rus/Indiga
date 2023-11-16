package com.example.indiga.model.menu.services;

import com.example.indiga.enums.ServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.example.indiga.constant.Constant.Command.COMMAND_SERVICE_1;
import static com.example.indiga.constant.Constant.Command.COMMAND_SERVICE_11;
import static com.example.indiga.enums.ServiceType.SERVICE_11;

@Component(COMMAND_SERVICE_11)
@Slf4j
public class MenuService11 extends MenuServiceBase {

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
        return SERVICE_11;
    }

    @Override
    public String getServiceDescription() {
        return "_- Закуп рекламы_";
    }
}
