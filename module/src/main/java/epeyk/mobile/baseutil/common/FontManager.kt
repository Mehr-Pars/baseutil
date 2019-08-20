package epeyk.mobile.baseutil.common

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import epeyk.mobile.baseutil.R

object FontManager {
    private val typeFaceMap = mutableMapOf<Int, Typeface>()

    fun getAppFont(context: Context): Typeface {
        return getFont(context, R.font.iransans)
    }

    fun getAppFontBold(context: Context): Typeface {
        return getFont(context, R.font.iransans_bold)
    }

    fun getAppFontFarsiNumbers(context: Context): Typeface {
        return getFont(context, R.font.iransans_farsi_numbers)
    }

    fun getFont(context: Context, resource: Int): Typeface {
        return if (typeFaceMap.containsKey(resource)) {
            typeFaceMap[resource]!!
        } else {
            val typeface = ResourcesCompat.getFont(context, resource) ?: Typeface.DEFAULT
            typeFaceMap[resource] = typeface
            typeface
        }
    }
}