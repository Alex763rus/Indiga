package com.example.indiga.model.menu.services;

import com.example.indiga.enums.ServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.example.indiga.constant.Constant.Command.COMMAND_SERVICE_1;
import static com.example.indiga.enums.ServiceType.SERVICE_1;

@Component(COMMAND_SERVICE_1)
@Slf4j
public class MenuService1 extends MenuServiceBase {

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
        return SERVICE_1;
    }

    @Override
    public String getServiceDescription() {
        return "_- Дизайн оборудования_\n" +
                "Мы разрабатываем формы и геометрии объектов. Этот процесс является важным для многих отраслей, от промышленного проектирования до создания художественных объектов.";
    }
}
