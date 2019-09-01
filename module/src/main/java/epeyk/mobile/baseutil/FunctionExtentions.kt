package epeyk.mobile.baseutil

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import epeyk.mobile.baseutil.common.NumberTextWatcherForThousand
import epeyk.mobile.baseutil.common.TextUtils
import java.util.regex.Pattern

// region View
fun View.visible() {
    visibility = View.VISIBLE
}

fun View.inVisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun ViewGroup.inflate(@LayoutRes resId: Int): View =
    LayoutInflater.from(this.context).inflate(resId, this, false)

//endregion

// region String
fun String.limitString(limit: Int): String {
    return if (length > limit) substring(0, limit - 3) + "..." else this
}

fun String.isNumeric(): Boolean {
    for (c in this.toCharArray())
        if (!Character.isDigit(c)) return false
    return true
}

fun String.extractNumbers(): String {
    val pattern = Pattern.compile("[^0-9]")
    return pattern.matcher(this).replaceAll("")
}
//endregion

// region ImageView
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
//endregion

// region Context
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

val EMPTY_LAMBDA: (Dialog) -> Unit = {}
fun Context.showDialog(
    dialogText: String,
    confirmAction: (Dialog) -> Unit = EMPTY_LAMBDA,
    cancelAction: (Dialog) -> Unit = EMPTY_LAMBDA,
    confirmBtnText: String = getString(R.string.confirm),
    cancelBtnText: String = getString(R.string.cancel),
    hideCancelBtn: Boolean = true,
    cancelable: Boolean = true
) {
    val dialog = Dialog(this)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCanceledOnTouchOutside(cancelable)
    dialog.setCancelable(cancelable)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setContentView(R.layout.dialog_simple)

    dialog.findViewById<TextView>(R.id.text).text = dialogText
    val cancelBtn = dialog.findViewById<TextView>(R.id.cancel)
    val confirmBtn = dialog.findViewById<TextView>(R.id.confirm)
    cancelBtn.text = cancelBtnText
    confirmBtn.text = confirmBtnText
    cancelBtn.setOnClickListener {
        if (cancelAction != EMPTY_LAMBDA) cancelAction(dialog)
        else dialog.dismiss()
    }
    confirmBtn.setOnClickListener {
        if (confirmAction != EMPTY_LAMBDA) confirmAction(dialog)
        else dialog.dismiss()
    }
    if (cancelAction == EMPTY_LAMBDA && hideCancelBtn)
        cancelBtn.visibility = View.GONE

    dialog.show()
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
//endregion

@BindingAdapter("price")
fun setPrice(textView: TextView, price: Int?) {
    setPrice(textView, price.toString())
}

@BindingAdapter("price")
fun setPrice(textView: TextView, price: String?) {
    if (!TextUtils.isEmpty(price)) {
        val formatted =
            NumberTextWatcherForThousand.getDecimalFormattedString(price!!)
        textView.text = textView.context.getString(R.string.price_rials, formatted)
    } else {
        textView.text = textView.context.getString(R.string.price_rials, "0")
    }
}