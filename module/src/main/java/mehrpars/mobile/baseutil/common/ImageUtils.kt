package mehrpars.mobile.baseutil.common

import android.annotation.SuppressLint
import android.graphics.*
import android.util.Log
import android.view.View

object ImageUtils {
    @SuppressLint("LongLogTag")
    fun getBitmapFromPath(path: String): Bitmap? {
        try {
            val options = BitmapFactory.Options()
            options.inSampleSize = 1
            options.inDither = false
            options.inPurgeable = true
            options.inPreferredConfig = Bitmap.Config.RGB_565
            var done = false
            var downSampleBy = 1
            var myBitmap: Bitmap? = null

            while (!done) {
                options.inSampleSize = downSampleBy

                try {
                    myBitmap = BitmapFactory.decodeFile(path, options)
                    done = true
                } catch (var7: OutOfMemoryError) {
                    if (downSampleBy == 16) {
                        break
                    }

                    downSampleBy *= 2
                    Log.d("OutOfMemory in down sample size", var7.message)
                    var7.printStackTrace()
                    System.gc()
                }

            }

            return myBitmap
        } catch (var8: OutOfMemoryError) {
            Log.d("OutOfMemory", var8.message)
            var8.printStackTrace()
            System.gc()
            return null
        } catch (var9: Exception) {
            var9.printStackTrace()
            return null
        }
    }

    fun getTintBitmap(bitmap: Bitmap, color: Int): Bitmap {
        val paint = Paint()
        paint.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        val bitmapResult = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmapResult)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return bitmapResult
    }

    fun mergeBitmaps(overlay: Bitmap, bitmap: Bitmap): Bitmap {
        val height = bitmap.height
        val width = bitmap.width

        val combined = Bitmap.createBitmap(width, height, bitmap.config)
        val canvas = Canvas(combined)
        val canvasWidth = canvas.width
        val canvasHeight = canvas.height

        canvas.drawBitmap(bitmap, Matrix(), null)

        val centreX = (canvasWidth - overlay.width) / 2
        val centreY = (canvasHeight - overlay.height) / 2
        canvas.drawBitmap(overlay, centreX.toFloat(), centreY.toFloat(), null)

        return combined
    }

    fun getBitmapFromView(v: View): Bitmap {
        val b = Bitmap.createBitmap(v.layoutParams.width, v.layoutParams.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        v.layout(v.left, v.top, v.right, v.bottom)
        v.draw(c)
        return b
    }
}
