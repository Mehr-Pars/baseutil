package epeyk.mobile.baseutil.views

import android.content.Context
import android.graphics.Typeface
import android.text.util.Linkify
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import epeyk.mobile.baseutil.R

open class TextViewCustom : AppCompatTextView {
    private var forceGravity = false
    private var defaultGravity = 0

    companion object {
        var typeFaceMap = mutableMapOf<String, Typeface>()
    }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initialize(context, attrs)
    }

    private fun initialize(context: Context, attrs: AttributeSet) {
        setCustomFont(context, attrs)
        setDefaultGravity(context, attrs)
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
        val customFont = a.getString(R.styleable.TextViewCustom_customFont)
        setCustomFont(ctx, customFont)
        a.recycle()
    }

    fun setCustomFont(ctx: Context, asset: String?): Boolean {
        val tf: Typeface

        if (typeFaceMap.containsKey(asset)) {
            tf = typeFaceMap[asset]!!
        } else {
            tf = Typeface.createFromAsset(ctx.assets, asset)
            typeFaceMap[asset!!] = tf
        }
        typeface = tf
        return true
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
}