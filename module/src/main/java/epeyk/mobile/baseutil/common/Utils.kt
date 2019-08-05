package epeyk.mobile.baseutil.common

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.Log
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

    fun getTimeString(millis: Int): String {
        val second = (millis / 1000 % 60).toLong()
        val minute = (millis / (1000 * 60) % 60).toLong()
        val hour = (millis / (1000 * 60 * 60) % 24).toLong()
        return if (hour > 0) String.format("%02d:%02d:%02d", hour, minute, second)
        else String.format("%02d:%02d", minute, second)
    }
}
