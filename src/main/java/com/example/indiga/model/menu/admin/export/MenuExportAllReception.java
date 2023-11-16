package com.example.indiga.model.menu.admin.export;

import com.example.indiga.model.jpa.User;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.indiga.constant.Constant.Command.COMMAND_EXPORT_ALL_RECEPTION;

@Component(COMMAND_EXPORT_ALL_RECEPTION)
@Slf4j
public class MenuExportAllReception extends MenuExportReceptionBase {

    @Override
    public String getMenuComand() {
        return COMMAND_EXPORT_ALL_RECEPTION;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        try {
            return switch (stateService.getState(user)) {
                case FREE -> exportAllReceptionToExcel(user, update);
                default -> createErrorDefaultMessage(user);
            };
        } catch (Exception ex) {
            log.error(ex.toString());
            return createMessageList(user, ex.toString());
        }
    }

    private List<PartialBotApiMethod> exportAllReceptionToExcel(User user, Update update) {
        val userList = receptionRepository.findAll();
        return exportToExcel(user, userList);
    }

    @Override
    public String getDescription() {
        return "Выгрузка всех записей";
    }
}
