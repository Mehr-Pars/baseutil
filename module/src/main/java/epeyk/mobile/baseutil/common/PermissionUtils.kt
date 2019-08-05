package epeyk.mobile.baseutil.common

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import epeyk.mobile.baseutil.EnumToastType
import epeyk.mobile.baseutil.R
import epeyk.mobile.baseutil.makeToast
import java.util.*

/**
 * Created by Arzesh on 21/01/2018.
 */

class PermissionUtils {
    private val commandQueue = LinkedList<Command>()
    private var activity: Activity? = null

    fun safeCall(command: Command, vararg permissions: String) {
        commandQueue.add(command)
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            requestPermissions(*permissions)
        } else {
            for (i in commandQueue.indices)
                commandQueue.remove().onExecute()
        }
    }

    fun safeCall(command: Command, permission: String) {
        commandQueue.add(command)
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            requestPermission(permission)
        } else {
            for (i in commandQueue.indices)
                commandQueue.remove().onExecute()
        }
    }

    private fun requestPermissions(vararg permissions: String) {
        Dexter.withActivity(activity)
            .withPermissions(
                *permissions
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        for (i in commandQueue.indices)
                            commandQueue.remove().onExecute()
                    } else if (report.isAnyPermissionPermanentlyDenied) {
                        showSettingsDialog()
                    } else {
                        for (i in commandQueue.indices)
                            commandQueue.remove().onCancel()
                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).withErrorListener { error ->
                activity!!.makeToast(
                    activity!!.getString(R.string.error_occurred),
                    EnumToastType.TOAST_TYPE_ERROR
                )
            }
            .onSameThread()
            .check()
    }

    private fun requestPermission(permission: String) {
        Dexter.withActivity(activity)
            .withPermission(permission)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    for (i in commandQueue.indices)
                        commandQueue.remove().onExecute()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    if (response.isPermanentlyDenied) {
                        showSettingsDialog()
                    } else {
                        for (i in commandQueue.indices)
                            commandQueue.remove().onCancel()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle(activity!!.getString(R.string.need_permissions))
        builder.setMessage(activity!!.getString(R.string.need_permissions_msg))
        builder.setCancelable(false)
        builder.setPositiveButton(activity!!.getString(R.string.open_setting)) { dialog, which ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(activity!!.getString(R.string.cancel)) { dialog, which ->
            dialog.cancel()
            for (i in commandQueue.indices)
                commandQueue.remove().onCancel()
        }
        builder.show()

    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity!!.packageName, null)
        intent.data = uri
        activity!!.startActivityForResult(intent, 101)
    }

    companion object {

        fun getInstance(activity: Activity): PermissionUtils {
            val permissionUtils = PermissionUtils()
            permissionUtils.activity = activity
            return permissionUtils
        }
    }
}
