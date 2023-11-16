package com.example.indiga.model.menu.services;

import com.example.indiga.enums.ServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.example.indiga.constant.Constant.Command.COMMAND_SERVICE_1;
import static com.example.indiga.constant.Constant.Command.COMMAND_SERVICE_17;
import static com.example.indiga.enums.ServiceType.SERVICE_17;

@Component(COMMAND_SERVICE_17)
@Slf4j
public class MenuService17 extends MenuServiceBase {

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
        return SERVICE_17;
    }

    @Override
    public String getServiceDescription() {
        return "_- Гибкость при выполнении задач_\n" +
                "Мы учтем все нюансы и особенности вашей компании и выстроим комфортное и прозрачное взаимодействие, результат нашей работы можно измерить на любом этапе.\n";
    }
}
