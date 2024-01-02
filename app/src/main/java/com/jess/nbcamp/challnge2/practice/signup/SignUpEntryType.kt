package com.jess.nbcamp.challnge2.practice.signup

enum class SignUpEntryType {
    CREATE,
    UPDATE
    ;

    companion object {

        fun getEntryType(ordinal: Int?): SignUpEntryType =
            SignUpEntryType.values().firstOrNull {
                it.ordinal == ordinal
            } ?: CREATE
    }
}