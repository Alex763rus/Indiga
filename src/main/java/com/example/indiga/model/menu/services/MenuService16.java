package com.example.indiga.model.menu.services;

import com.example.indiga.enums.ServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.example.indiga.constant.Constant.Command.COMMAND_SERVICE_1;
import static com.example.indiga.constant.Constant.Command.COMMAND_SERVICE_16;
import static com.example.indiga.enums.ServiceType.SERVICE_16;

@Component(COMMAND_SERVICE_16)
@Slf4j
public class MenuService16 extends MenuServiceBase {

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
        return SERVICE_16;
    }

    @Override
    public String getServiceDescription() {
        return "_- Фокус на задачах бизнеса_\n" +
                "Вы получите декомпозицию желаемого результата на 3-6 месяцев, включая количество клиентов, средний чек, ряд мероприятий и бизнес-процессов для привлечения клиентов, а также список конкретных задач для достижения результата.";
    }
}
