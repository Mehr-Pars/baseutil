package epeyk.mobile.baseutil.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.text.util.Linkify
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import epeyk.mobile.baseutil.R
import epeyk.mobile.baseutil.common.FontManager


open class EditTextCustom : AppCompatEditText {
    private var forceGravity = false
    private var defaultGravity = 0

    constructor(context: Context) : super(context) {
        changeCursor()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initialize(context, attrs)
    }

    private fun initialize(context: Context, attrs: AttributeSet) {
        changeCursor()
        setCustomFont(context, attrs)
        setDefaultGravity(context, attrs)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString().isEmpty())
                //check if length is equal to zero
                    setHintTextColor(textColors.withAlpha(150))
            }
        })
    }

    private fun changeCursor() {
        try {
            val f = TextView::class.java.getDeclaredField("mCursorDrawableRes")
            f.isAccessible = true
            f.set(this, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun setLinksEnabled(enabled: Boolean) {
        if (enabled) {
            setLinkTextColor(resources.getColor(R.color.colorBlue))
            linksClickable = true
            isClickable = true
            autoLinkMask = Linkify.WEB_URLS
        } else {
            linksClickable = false
        }
    }

    private fun setCustomFont(ctx: Context, attrs: AttributeSet) {
        val a = ctx.obtainStyledAttributes(attrs, R.styleable.TextViewCustom)
        val customFont = a.getResourceId(R.styleable.TextViewCustom_customFont, R.font.iransans_farsi_numbers)
        setCustomFont(ctx, customFont)
        a.recycle()
    }

    fun setCustomFont(ctx: Context, resource: Int?): Boolean {
        resource?.let {
            typeface = FontManager.getFont(ctx, resource)
            return true
        }
        return false
    }

    override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (!forceGravity && lengthBefore == 0 && lengthAfter > 0) {
            if (text.subSequence(
                    0,
                    1
                ).toString().matches("[a-zA-Z]".toRegex())
            ) { // check if text starts with english character
                gravity = Gravity.LEFT
            } else if (defaultGravity != 0) {
                gravity = defaultGravity
            }
        }
    }

    fun setDefaultGravity(ctx: Context, attrs: AttributeSet) {
        val a = ctx.obtainStyledAttributes(attrs, R.styleable.TextViewCustom)
        forceGravity = a.getBoolean(R.styleable.TextViewCustom_forceGravity, false)

        if (!forceGravity)
            gravity = Gravity.RIGHT

        defaultGravity = gravity

        //        int gravity = getGravity();
        //        if (gravity != Gravity.CENTER && gravity != Gravity.CENTER_HORIZONTAL && gravity != Gravity.CENTER_VERTICAL && gravity != Gravity.LEFT) {
        //            setGravity(Gravity.RIGHT);
        //        }
    }

    companion object {
        private val TAG = "EditText"
    }
}