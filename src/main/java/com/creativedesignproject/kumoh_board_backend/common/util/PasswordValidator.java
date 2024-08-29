package com.creativedesignproject.kumoh_board_backend.common.util;

public class PasswordValidator {
    public static boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false; // 최소 길이 검사 (예: 8자 이상)
        }

        if (!password.matches(".*[A-Z].*")) {
            return false; // 대문자 포함 여부 검사
        }

        if (!password.matches(".*[a-z].*")) {
            return false; // 소문자 포함 여부 검사
        }

        if (!password.matches(".*\\d.*")) {
            return false; // 숫자 포함 여부 검사
        }

        if (!password.matches(".*[!@#$%^&*()].*")) {
            return false; // 특수 문자 포함 여부 검사
        }

        return true;
    }
}
