package com.example.indiga.enums;

public enum UserRole {

    BLOCKED("Заблокирован"),
    UNREGISTERED("Незарегистрирован"),
    EMPLOYEE("Пользователь"),
    MANAGER("Менеджер"),
    ADMIN("Администратор");

    private String title;

    UserRole(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
