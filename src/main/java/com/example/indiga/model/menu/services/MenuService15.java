package com.example.indiga.model.menu.services;

import com.example.indiga.enums.ServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.example.indiga.constant.Constant.Command.COMMAND_SERVICE_1;
import static com.example.indiga.constant.Constant.Command.COMMAND_SERVICE_15;
import static com.example.indiga.enums.ServiceType.SERVICE_15;

@Component(COMMAND_SERVICE_15)
@Slf4j
public class MenuService15 extends MenuServiceBase {

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
        return SERVICE_15;
    }

    @Override
    public String getServiceDescription() {
        return "_- Экономия ресурсов компании_\n" +
                "Мы проведем детальный анализ вашего бизнеса, учтем ваш контекст и используем наш опыт для создания долгосрочной и эффективной маркетинговой стратегии.\n";
    }
}
