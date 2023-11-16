package com.example.indiga.model.menu.admin.export;

import com.example.indiga.model.jpa.User;
import com.example.indiga.model.menu.base.Menu;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.tgcommons.model.wrapper.SendDocumentWrap;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.indiga.constant.Constant.Command.COMMAND_EXPORT_ALL_USERS;

@Component(COMMAND_EXPORT_ALL_USERS)
@Slf4j
public class MenuExportAllUsers extends Menu {

    @Override
    public String getMenuComand() {
        return COMMAND_EXPORT_ALL_USERS;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        try {
            return switch (stateService.getState(user)) {
                case FREE -> exportAllUsersToExcel(user, update);
                default -> createErrorDefaultMessage(user);
            };
        } catch (Exception ex) {
            log.error(ex.toString());
            return createMessageList(user, ex.toString());
        }
    }

    private List<PartialBotApiMethod> exportAllUsersToExcel(User user, Update update) {
        val users = userService.getAllUsers();
        val answer = exportToExcel(user, users);
        return answer;
    }

    @Override
    public String getDescription() {
        return "Все пользователи:";
    }

    protected List<PartialBotApiMethod> exportToExcel(User user, List<User> data) {
        List<List<String>> excelData = new ArrayList<>();
        val headers = new ArrayList<String>(
                List.of("№", "Чат ИД:", "FirstName:", "LastName:", "UserName", "ФИО:", "Компания:", "Телефон:", "Роль:", "Дата регистрации:")
        );
        excelData.add(headers);

        for (int i = 0; i < data.size(); ++i) {
            val client = data.get(i);
            excelData.add(
                    Arrays.asList(
                            String.valueOf(i + 1)
                            , String.valueOf(client.getChatId())
                            , client.getFirstName()
                            , client.getLastName()
                            , client.getUserName()
                            , client.getInputFio()
                            , client.getInputCompany()
                            , client.getInputPhone()
                            , client.getUserRole().getTitle()
                            , client.getRegisteredAt().toString()
                    )
            );
        }
        stateService.refreshUser(user);
        return SendDocumentWrap.init()
                .setChatIdLong(user.getChatId())
                .setDocument(excelService.createExcelDocument("Записи", excelData, 8))
                .setCaption(getDescription())
                .build().createMessageList();
    }
}
