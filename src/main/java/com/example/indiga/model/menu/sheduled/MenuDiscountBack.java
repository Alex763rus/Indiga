package com.example.indiga.model.menu.sheduled;

import com.example.indiga.model.jpa.User;
import com.example.indiga.model.menu.base.Menu;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.tgcommons.model.button.Button;
import org.example.tgcommons.model.button.ButtonsDescription;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

import static com.example.indiga.constant.Constant.Command.COMMAND_DISCOUNT;
import static com.example.indiga.constant.Constant.Command.COMMAND_FEED_BACK;

@Component(COMMAND_DISCOUNT)
@Slf4j
public class MenuDiscountBack extends Menu {

    @Override
    public String getMenuComand() {
        return COMMAND_DISCOUNT;
    }

    @Override
    public String getDescription() {
        return COMMAND_DISCOUNT;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        try {
            return freelogic(user, update);
        } catch (Exception ex) {
            log.error(ex.toString());
            return createMessageList(user, ex.toString());
        }
    }

    private List<PartialBotApiMethod> freelogic(User user, Update update) {
        val discounts = discountRepository.findAll();
        if (discounts.isEmpty()) {
            return createMessageList(user, "Акций пока нет");
        }
        return createMessageList(user, discounts.get(0).getDescription());
    }

}
