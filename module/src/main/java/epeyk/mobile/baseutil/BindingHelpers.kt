package epeyk.mobile.baseutil

import android.widget.TextView
import androidx.databinding.BindingAdapter
import epeyk.mobile.baseutil.common.TextUtils

@BindingAdapter("price")
fun setPrice(textView: TextView, price: Int?) {
    setPrice(textView, price.toString())
}

@BindingAdapter("price")
fun setPrice(textView: TextView, price: String?) {
    textView.text = TextUtils.getPriceFormatted(textView.context, price)
}
