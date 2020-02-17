package epeyk.mobile.baseutil

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.gson.JsonArray
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.readystatesoftware.systembartint.SystemBarTintManager
import epeyk.mobile.baseutil.common.NumberTextWatcherForThousand
import epeyk.mobile.baseutil.common.TextUtils
import epeyk.mobile.baseutil.views.CustomSpinner
import java.io.Serializable
import java.util.*
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

fun View.hideKeyBoard() {
    clearFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyBoard() {
    try {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        requestFocus()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun View.getCenter(): Pair<Int, Int> {
    val cx = (this.x + this.width / 2).toInt()
    val cy = (this.y + this.height / 2).toInt()
    return Pair(cx, cy)
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
fun AppCompatActivity.initStatusBar(colorResource: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        val tintManager = SystemBarTintManager(this)
        tintManager.isStatusBarTintEnabled = true
        tintManager.setTintColor(ContextCompat.getColor(this, colorResource))
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = resources.getColor(colorResource)
        window.navigationBarColor = resources.getColor(colorResource)
    }
}

fun Activity.hideKeyBoard() {
    try {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.showKeyBoard() {
    try {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
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
//endregion

// region Json
fun JsonObject.optString(key: String): String {
    val element = get(key)
    if (element != null && element !== JsonNull.INSTANCE)
        return element.asString

    return ""
}

fun JsonObject.optInt(key: String, defaultValue: Int = 0): Int {
    val element = get(key)
    if (element != null && element !== JsonNull.INSTANCE)
        return element.asInt

    return defaultValue
}

fun JsonObject.optLong(key: String, defaultValue: Long = 0L): Long {
    val element = get(key)
    if (element != null && element !== JsonNull.INSTANCE)
        return element.asLong

    return defaultValue
}

fun JsonObject.optBoolean(key: String, defaultValue: Boolean = false): Boolean {
    val element = get(key)
    if (element != null && element !== JsonNull.INSTANCE)
        return element.asBoolean

    return defaultValue
}

fun List<String>.toJsonArray(): JsonArray {
    val array = JsonArray()
    forEach { array.add(it) }

    return array
}
//endregion

// region Intent
fun Intent.putExtra(name: String, value: Any) {
    when (value) {
        is Boolean -> putExtra(name, value)
        is Byte -> putExtra(name, value)
        is Char -> putExtra(name, value)
        is Short -> putExtra(name, value)
        is Int -> putExtra(name, value)
        is Long -> putExtra(name, value)
        is Float -> putExtra(name, value)
        is Double -> putExtra(name, value)
        is String -> putExtra(name, value)
        is CharSequence -> putExtra(name, value)
        is Parcelable -> putExtra(name, value)
        is Serializable -> putExtra(name, value)
        is Bundle -> putExtra(name, value)
    }
}
//endregion

// region Other
fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(Locale("en"), this).toDouble()
// endregion

// region BindingAdapter
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

@BindingAdapter("showStrike")
fun showStrike(textView: TextView, show: Boolean?) {
    if (show == true) {
        textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        textView.paintFlags = Paint.LINEAR_TEXT_FLAG
    }
}


// for CustomSpinner
@BindingAdapter(
    value = ["entries", "selectedValue", "selectedValueAttrChanged"], requireAll = false
)
fun <T : CharSequence> setEntries(
    spinner: CustomSpinner,
    entries: Array<T>?,
    selectedValue: String?,
    newTextAttrChanged: InverseBindingListener?
) {
    if (entries != null) {
        val oldAdapter = spinner.adapter
        var changed = true
        if (oldAdapter != null && oldAdapter.count == entries.size) {
            changed = false
            for (i in entries.indices) {
                if (entries[i] != oldAdapter.getItem(i)) {
                    changed = true
                    break
                }
            }
        }
        if (changed) {
            val adapter = ArrayAdapter<CharSequence>(
                spinner.context,
                R.layout.simple_spinner_item, entries
            )
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    } else {
        spinner.adapter = null
    }

    newTextAttrChanged?.let {
        spinner.addOnItemSelectedListener(object : CustomSpinner.OnItemSelectListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                newTextAttrChanged.onChange()
            }
        })
    }

    if (selectedValue != null) {
        val pos = (spinner.adapter as ArrayAdapter<String>).getPosition(selectedValue)
        spinner.setSelection(pos, true)
    }

}

// for CustomSpinner
@InverseBindingAdapter(attribute = "selectedValue", event = "selectedValueAttrChanged")
fun captureSelectedValue(spinner: CustomSpinner): String {
    return spinner.selectedItem as String
}

// endregion