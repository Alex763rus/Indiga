package com.example.indiga.model.menu.admin.export;

import com.example.indiga.model.jpa.Reception;
import com.example.indiga.model.jpa.User;
import com.example.indiga.model.menu.base.Menu;
import lombok.val;
import org.example.tgcommons.model.wrapper.SendDocumentWrap;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class MenuExportReceptionBase extends Menu {

    protected List<PartialBotApiMethod> exportToExcel(User user, List<Reception> data) {
        List<List<String>> excelData = new ArrayList<>();
        val headers = new ArrayList<String>(
                List.of("№", "ID записи:", "Чат ИД:", "Логин:", "ФИО:", "Компания:", "Телефон:", "Услуга:", "Дата записи:", "Время записи:", "Дата оформления:")
        );
        excelData.add(headers);

        for (int i = 0; i < data.size(); ++i) {
            val reception = data.get(i);
            val client = reception.getUser();
            excelData.add(
                    Arrays.asList(
                            String.valueOf(i + 1)
                            , String.valueOf(reception.getReceptionId())
                            , String.valueOf(client.getChatId())
                            , client.getUserName()
                            , client.getInputFio()
                            , client.getInputCompany()
                            , client.getInputPhone()
                            , reception.getServiceType().getTitle()
                            , reception.getReceptionDate().toString()
                            , reception.getReceptionTime().getTitle()
                            , reception.getReceptionRegistrationDate().toString()
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
