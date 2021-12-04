package com.ej.happybirthdayapp.model

data class BirthdayAge(val age: Int, val timeType: TimeType)

enum class TimeType {
    MONTHS, YEARS
}