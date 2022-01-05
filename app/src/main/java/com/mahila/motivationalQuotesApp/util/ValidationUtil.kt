package com.mahila.motivationalQuotesApp.util

import java.util.regex.Pattern

object ValidationUtil {


    fun isValidPassword(
        inputString: String,
        minLength: Int = 8,
        maxLength: Int = 30,
        containSpecialCharacter: Int = 1,
        containNumbers: Int = 1,
        containCapitalLetters: Int = 1
    ): Boolean {
        var regexString = getInputRegexString(
            minLength,
            maxLength,
            containSpecialCharacter,
            containNumbers,
            containCapitalLetters
        )
        var validationRegex = Regex(regexString)
        return inputString.matches(validationRegex)
    }

    private fun getInputRegexString(
        minLength: Int,
        maxLength: Int,
        containSpecialCharacter: Int = -1,
        containNumbers: Int = -1,
        containCapitalLetters: Int = -1
    ): String {
        var enableCapitalMandatory = "?!"
        var enableCapitalOptional = ""
        when (containCapitalLetters) {
            2 -> {
                enableCapitalMandatory = ""
                enableCapitalOptional = "*?"
            }
            1 -> {
                enableCapitalMandatory = "?="
                enableCapitalOptional = ""
            }
            -1 -> {
                enableCapitalMandatory = "?!"
                enableCapitalOptional = ""
            }
        }
        var enableSpecialCharMandatory = "?!"
        var enableSpecialCharOptional = ""
        when (containSpecialCharacter) {
            2 -> {
                enableSpecialCharMandatory = ""
                enableSpecialCharOptional = "*?"
            }
            1 -> {
                enableSpecialCharMandatory = "?="
                enableSpecialCharOptional = ""
            }
            -1 -> {
                enableSpecialCharMandatory = "?!"
                enableSpecialCharOptional = ""
            }
        }
        var enableNumbersMandatory = "?!"
        var enableNumbersOptional = ""
        when (containNumbers) {
            2 -> {
                enableNumbersMandatory = ""
                enableNumbersOptional = "*?"
            }
            1 -> {
                enableNumbersMandatory = "?="
                enableNumbersOptional = ""
            }
            -1 -> {
                enableNumbersMandatory = "?!"
                enableNumbersOptional = ""
            }
        }
        return "^(?=.*[a-z])(${enableCapitalMandatory}.*[A-Z]${enableCapitalOptional})(${enableNumbersMandatory}.*\\d${enableNumbersOptional})" +
                "(${enableSpecialCharMandatory}.*[-+!@#\$%^&*,?]${enableSpecialCharOptional}).{${minLength},${maxLength}}$"
    }

    fun isValidEmail(
        userEmail: String
    ): Boolean {
        //return android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()
        val email = Pattern.compile(

            "[a-zA-Z0-9\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        return email.matcher(userEmail).matches()
    }

}