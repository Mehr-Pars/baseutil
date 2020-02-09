package epeyk.mobile.baseutil.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import epeyk.mobile.baseutil.showDialog
import java.io.File

object FileUtils {

    fun createDirectory(path: String): Boolean {
        val directory = File(path)
        if (!directory.exists()) {
            directory.mkdir()
            return true
        } else {
            return false
        }
    }

    fun deleteDirectory(dir: String): Boolean {
        return deleteDirectory(File(dir))
    }

    fun deleteDirectory(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (aChildren in children) {
                val success = deleteDirectory(File(dir, aChildren))
                if (!success) {
                    return false
                }
            }
        }
        return dir != null && dir.delete()
    }

    fun fileExist(pathToFile: String): Boolean {
        val file = File(pathToFile)
        return file.exists()
    }

    fun openFile(context: Context, file: File) {
        // Create URI
        val uri = if (Build.VERSION.SDK_INT >= 24) {
            FileProvider.getUriForFile(
                context, context.applicationContext.packageName + ".provider", file
            )
        } else {
            Uri.fromFile(file)
        }

        val path = file.path

        val intent = Intent(Intent.ACTION_VIEW)
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (path.contains(".apk")) {
            // APK file
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
            intent.addCategory("android.intent.category.DEFAULT")
        } else if (path.contains(".doc") || path.contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword")
        } else if (path.contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf")
        } else if (path.contains(".ppt") || path.contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
        } else if (path.contains(".xls") || path.contains(".xlsx") || path.contains(".csv")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel")
        } else if (path.contains(".zip") || path.contains(".rar")) {
            // Zip file
            intent.setDataAndType(uri, "application/zip")
        } else if (path.contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf")
        } else if (path.contains(".wav") || path.contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav")
        } else if (path.contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif")
        } else if (path.contains(".jpg") || path.contains(".jpeg") || path.contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg")
        } else if (path.contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain")
        } else if (path.contains(".3gp") || path.contains(".mpg") || path.contains(".mpeg") || path.contains(
                ".mpe"
            ) || path.contains(
                ".mp4"
            ) || path.contains(".avi")
        ) {
            // Video files
            intent.setDataAndType(uri, "video/*")
        } else {
            //if you want you can also define the intent type for any other file

            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*")
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            context.showDialog("امکان باز کردن این فایل وجود ندارد برای مشاهده فایل به آدرس $path مراجعه کنید")
        }
    }
}
