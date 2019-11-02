package epeyk.mobile.baseutil.common

import java.util.*
import java.util.regex.Pattern

object TextUtils {
    var englishNumbers: HashMap<Char, String> = object : HashMap<Char, String>() {
        init {
            put('\u06F0', "0")
            put('\u06F1', "1")
            put('\u06F2', "2")
            put('\u06F3', "3")
            put('\u06F4', "4")
            put('\u06F5', "5")
            put('\u06F6', "6")
            put('\u06F7', "7")
            put('\u06F8', "8")
            put('\u06F9', "9")
        }
    }
    var persianNumbers: HashMap<Char, Char> = object : HashMap<Char, Char>() {
        init {
            put('0', '\u06F0')
            put('1', '\u06F1')
            put('2', '\u06F2')
            put('3', '\u06F3')
            put('4', '\u06F4')
            put('5', '\u06F5')
            put('6', '\u06F6')
            put('7', '\u06F7')
            put('8', '\u06F8')
            put('9', '\u06F9')
        }
    }

    fun getFileNameFromUrl(url: String): String {
        return url.replace("http://", "").split("/".toRegex()).last()
    }

    fun isEmpty(s: String?): Boolean {
        return s == null || s == "null" || s.length == 0
    }

    fun isNumericString(s: String): Boolean {
        return isMatch(s, "\\d+(?:\\.\\d+)?")
    }

    fun toEnglishDigit(input: String): String {
        val result = StringBuilder("")
        for (i in 0 until input.length) {
            val character = input[i]
            if (englishNumbers.containsKey(character)) {
                result.append(englishNumbers[character])
            } else {
                result.append(input[i])
            }
        }
        return result.toString()
    }

    fun toPersianDigit(input: String): String {
        val result = StringBuilder("")
        for (i in 0 until input.length) {
            val character = input[i]
            if (persianNumbers.containsKey(character)) {
                result.append(persianNumbers[character])
            } else {
                result.append(input[i])
            }
        }
        return result.toString()
    }

    fun isValidCellPhone(phoneNumber: String): Boolean {
        return !isEmpty(phoneNumber) && isMatch(phoneNumber, "^(?i)(09\\d{9})$")
    }

    fun isValidPhone(phoneNumber: String): Boolean {
        return !isEmpty(phoneNumber) && phoneNumber.startsWith("0") && phoneNumber.length == 11
    }

    fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun isMatch(s: String, pattern: String): Boolean {
        return try {
            val patt = Pattern.compile(pattern)
            val matcher = patt.matcher(s)
            matcher.matches()
        } catch (e: RuntimeException) {
            false
        }

    }
}
