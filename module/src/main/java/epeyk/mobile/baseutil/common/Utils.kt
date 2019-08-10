package epeyk.mobile.baseutil.common

import android.content.Context
import android.graphics.Typeface
import epeyk.mobile.baseutil.R
import java.io.FileInputStream
import java.io.IOException
import java.nio.charset.Charset
import java.text.DecimalFormat

object Utils {
    fun roundDecimals(d: Double, decimalCount: Int): Double {
        val pattern = StringBuilder("#.#")
        for (i in 0 until decimalCount - 1)
            pattern.append("#")
        val twoDForm = DecimalFormat(pattern.toString())
        return java.lang.Double.valueOf(twoDForm.format(d))
    }

    fun loadStringFromAsset(context: Context, filename: String): String? {
        var json: String? = null
        try {
            val `is` = context.assets.open(filename)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }

    fun loadStringFromPath(filename: String): String? {
        var json: String? = null
        try {
            val `is` = FileInputStream(filename)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }

    fun getAppDataDirectory(context: Context): String {
        var dataDir = ""

        try {
            dataDir = context.packageManager.getPackageInfo(context.packageName, 0).applicationInfo.dataDir
        } catch (ex: Exception) {
        }
        return dataDir
    }

    fun getAppFont(context: Context): Typeface {
        return Typeface.createFromAsset(context.assets, context.getString(R.string.app_font))
    }

    fun getAppFontBold(context: Context): Typeface {
        return Typeface.createFromAsset(context.assets, context.getString(R.string.app_font_bold))
    }

    fun getAppFontFarsiNumbers(context: Context): Typeface {
        return Typeface.createFromAsset(context.assets, context.getString(R.string.app_font_farsi_num))
    }
}
