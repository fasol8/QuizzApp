package com.sol.quizzapp.domain.model.util

enum class DifficultMode(val value: String) {
    EASY("easy"),
    MEDIUM("medium"),
    HARD("hard");

    companion object {
        fun fromValue(value: String): DifficultMode {
            return entries.find { it.value == value } ?: EASY
        }
    }
}