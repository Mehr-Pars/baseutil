package epeyk.mobile.baseutil.common

import android.content.Context
import android.graphics.Typeface
import epeyk.mobile.baseutil.R

object FontManager {
    private val typeFaceMap = mutableMapOf<String, Typeface>()

    fun getAppFont(context: Context): Typeface {
        val appFont = context.getString(R.string.app_font)
        return getFont(context, appFont)
    }

    fun getAppFontBold(context: Context): Typeface {
        val appFontBold = context.getString(R.string.app_font_bold)
        return getFont(context, appFontBold)
    }

    fun getAppFontFarsiNumbers(context: Context): Typeface {
        val appFontFarsiNumbers = context.getString(R.string.app_font_farsi_num)
        return getFont(context, appFontFarsiNumbers)
    }

    fun getFont(context: Context, asset: String): Typeface {
        return if (typeFaceMap.containsKey(asset)) {
            typeFaceMap[asset]!!
        } else {
            val typeface = Typeface.createFromAsset(context.assets, asset)
            typeFaceMap[asset] = typeface
            typeface
        }
    }
}