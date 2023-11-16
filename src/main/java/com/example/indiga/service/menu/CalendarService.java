package com.example.indiga.service.menu;

import com.example.indiga.enums.ServiceType;
import com.example.indiga.model.jpa.User;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalendarService {


    private Map<User, Calendar> calendarTmp = new HashMap<>();

    private Map<User, ServiceType> sevriceTypeTmp = new HashMap<>();

    public void add(User user, Calendar calendar) {
        calendarTmp.put(user, calendar);
    }

    public Calendar getCalendar(User user) {
        return calendarTmp.get(user);
    }

    public Calendar getOrCurrentCalendar(User user) {
        calendarTmp.putIfAbsent(user, Calendar.getInstance());
        return getCalendar(user);
    }


    public void addServiceType(User user, ServiceType serviceType) {
        sevriceTypeTmp.put(user, serviceType);
    }

    public ServiceType getServiceType(User user) {
        return sevriceTypeTmp.get(user);
    }

}
