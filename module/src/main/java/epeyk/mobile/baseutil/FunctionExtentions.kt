package epeyk.mobile.baseutil

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt


fun String.limitString(limit: Int): String {
    return if (length > limit) substring(0, limit - 3) + "..." else this
}

fun String.isNumeric(): Boolean {
    for (c in this.toCharArray())
        if (!Character.isDigit(c)) return false
    return true
}

fun ImageView.setTint(@ColorInt color: Int) {
    setColorFilter(color)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        imageTintList = ColorStateList.valueOf(color)
    }
}

fun ImageView.grayScale() {
    val matrix = ColorMatrix()
    matrix.setSaturation(0f)
    val filter = ColorMatrixColorFilter(matrix)
    colorFilter = filter
}

fun Activity.hideKeyBoard() {
    try {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

fun Context.makeToast(text: String, duration: Int, type: Int): Toast? {
    return makeToast(text, duration, EnumToastType.byValue(type))
}

fun Context.makeToast(text: String, type: EnumToastType): Toast? {
    return makeToast(text, Toast.LENGTH_SHORT, type)
}

fun Context.makeToast(text: String, duration: Int, type: EnumToastType): Toast? {
    try {
        val inflater = LayoutInflater.from(this)
        val layout = inflater.inflate(R.layout.toast_layout, null, false)
        val tv = layout.findViewById<View>(R.id.text) as TextView
        tv.text = text
        when (type) {
            EnumToastType.TOAST_TYPE_NORMAL -> tv.setTextColor(resources.getColor(R.color.colorGrayDark))
            EnumToastType.TOAST_TYPE_ERROR -> tv.setTextColor(resources.getColor(R.color.colorError))
            EnumToastType.TOAST_TYPE_SUCCESS -> tv.setTextColor(resources.getColor(R.color.colorSuccess))
        }
        val toast = Toast(applicationContext)
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.duration = duration
        toast.view = layout
        toast.show()

        return toast
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return null
}

enum class EnumToastType(val value: Int) {
    TOAST_TYPE_NORMAL(0), TOAST_TYPE_SUCCESS(1), TOAST_TYPE_ERROR(-1);

    companion object {
        fun byValue(`val`: Int): EnumToastType {
            for (item in values()) {
                if (item.value == `val`)
                    return item
            }
            return TOAST_TYPE_NORMAL
        }
    }
}
