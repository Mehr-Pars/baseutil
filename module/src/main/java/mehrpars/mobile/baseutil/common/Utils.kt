package mehrpars.mobile.baseutil.common

import android.content.Context
import mehrpars.mobile.baseutil.views.simplecropview.CopyFileToCache
import java.io.FileInputStream
import java.io.IOException
import java.nio.charset.Charset
import java.text.DecimalFormat
import java.util.*

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
            dataDir = context.packageManager.getPackageInfo(context.packageName, 0)
                .applicationInfo.dataDir
        } catch (ex: Exception) {
        }
        return dataDir
    }


    fun getPhoto_B64(context: Context, path: String): String? {
        val c = Calendar.getInstance()
        val profPic_name = c.time.time.toString() + ".png"
        val copyfile = CopyFileToCache(context)
        var Base64Picture: String? = null
        if (!path.isNullOrEmpty())
            Base64Picture = copyfile.Copy(path, profPic_name)

        return Base64Picture
    }

    fun getCurrentWidth(width: Int, context: Context): Int {
        return (width.toFloat() * (Utils.getWidth(context) as Float / 320.0f)).toInt()
    }

    fun getCurrentHeight(height: Int, context: Context): Int {
        return (height.toFloat() * (Utils.getHeight(context) as Float / 480.0f)).toInt()
    }

    fun getHeight(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.heightPixels
    }

    fun getWidth(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.widthPixels
    }

}
