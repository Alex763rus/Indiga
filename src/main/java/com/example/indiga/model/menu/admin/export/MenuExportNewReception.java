package com.example.indiga.model.menu.admin.export;

import com.example.indiga.model.jpa.User;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.indiga.constant.Constant.Command.COMMAND_EXPORT_NEW_RECEPTION;
import static com.example.indiga.enums.ReceptionExportStatus.EXPORTED_RECEPTION;
import static com.example.indiga.enums.ReceptionExportStatus.NEW_RECEPTION;

@Component(COMMAND_EXPORT_NEW_RECEPTION)
@Slf4j
public class MenuExportNewReception extends MenuExportReceptionBase {

    @Override
    public String getMenuComand() {
        return COMMAND_EXPORT_NEW_RECEPTION;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        try {
            return switch (stateService.getState(user)) {
                case FREE -> exportNewLeadsToExcel(user, update);
                default -> createErrorDefaultMessage(user);
            };
        } catch (Exception ex) {
            log.error(ex.toString());
            return createMessageList(user, ex.toString());
        }
    }

    private List<PartialBotApiMethod> exportNewLeadsToExcel(User user, Update update) {
        val receptions = receptionRepository.findByReceptionExportStatus(NEW_RECEPTION);
        val answer = exportToExcel(user, receptions);
        receptions.forEach(reception -> reception.setReceptionExportStatus(EXPORTED_RECEPTION));
        receptionRepository.saveAll(receptions);
        return answer;
    }

    @Override
    public String getDescription() {
        return "Новые записи";
    }
}
