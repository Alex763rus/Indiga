package com.example.indiga.enums;

public enum TimePeriod {

    TIME_10_14("c 10:00 до 14:00"),
    TIME_14_19("c 14:00 до 19:00");

    private String title;

    TimePeriod(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
