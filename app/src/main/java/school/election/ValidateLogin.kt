package school.election

import android.util.Patterns

fun checkPhoneIsValid(phone: String): Boolean {
    if (Patterns.PHONE.matcher(phone).matches()) return true
    return false
}