package mehrpars.mobile.baseutil.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatSpinner
import mehrpars.mobile.baseutil.R
import java.util.*

class CustomSpinner : AppCompatSpinner {
    private var mListeners: ArrayList<OnItemSelectListener>? = null

    constructor(context: Context) : super(context, null) {
        initialize()
    }

    constructor(context: Context, mode: Int) : super(context, null, R.attr.spinnerStyle, mode) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet) : super(
        context, attrs, R.attr.spinnerStyle
    ) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr, -1
    ) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, mode: Int) : super(
        context, attrs, defStyleAttr, mode, null
    ) {
        initialize()
    }

    private fun initialize() {
        onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                mListeners?.forEach { it.onItemSelected(p0, p1, position, p3) }
            }
        }
    }

    fun addOnItemSelectedListener(listener: OnItemSelectListener) {
        if (mListeners == null)
            mListeners = ArrayList()

        mListeners?.add(listener)
    }

    interface OnItemSelectListener {

        fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
    }

}