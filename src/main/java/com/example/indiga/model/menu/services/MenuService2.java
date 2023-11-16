package com.example.indiga.model.menu.services;

import com.example.indiga.enums.ServiceType;
import com.example.indiga.model.jpa.Reception;
import com.example.indiga.model.jpa.ReceptionRepository;
import com.example.indiga.model.jpa.User;
import com.example.indiga.model.menu.base.Menu;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.tgcommons.model.button.Button;
import org.example.tgcommons.model.button.ButtonsDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.indiga.constant.Constant.Command.COMMAND_SERVICE_1;
import static com.example.indiga.constant.Constant.Command.COMMAND_SERVICE_2;
import static com.example.indiga.constant.Constant.MenuChooseTime.BACK;
import static com.example.indiga.enums.ServiceType.SERVICE_1;
import static com.example.indiga.enums.ServiceType.SERVICE_2;
import static com.example.indiga.enums.State.*;
import static com.example.indiga.enums.TimePeriod.TIME_10_14;
import static com.example.indiga.enums.TimePeriod.TIME_14_19;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.example.tgcommons.constant.Constant.Calendar.*;
import static org.example.tgcommons.constant.Constant.TextConstants.NEW_LINE;
import static org.example.tgcommons.utils.ButtonUtils.createCalendar;
import static org.example.tgcommons.utils.MessageUtils.prepareBold;

@Component(COMMAND_SERVICE_2)
@Slf4j
public class MenuService2 extends MenuServiceBase {

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
        return SERVICE_2;
    }

    @Override
    public String getServiceDescription() {
        return "_- Видео анимирование_\n" +
                "Мы добавляем анимационные элементы и видеоролики, которые помогают усилить брендирование компании или продукта.";
    }

}
