package epeyk.mobile.baseutil.common

import android.content.Context
import android.widget.TextView
import androidx.databinding.BindingAdapter
import epeyk.mobile.baseutil.R

@BindingAdapter("price")
fun setPrice(textView: TextView, price: Int?) {
    setPrice(textView, price.toString())
}

@BindingAdapter("price")
fun setPrice(textView: TextView, price: String?) {
    textView.text = getPriceFormatted(textView.context, price)
}

fun getPriceFormatted(context: Context, price: String?): String {
    return if (!TextUtils.isEmpty(price)) {
        val formatted = NumberTextWatcherForThousand.getDecimalFormattedString(price!!)
        context.getString(R.string.price_rials, formatted)
    } else {
        context.getString(R.string.price_rials, "0")
    }
}
