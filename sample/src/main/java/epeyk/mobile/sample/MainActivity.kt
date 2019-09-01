package epeyk.mobile.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import epeyk.mobile.baseutil.EnumToastType
import epeyk.mobile.baseutil.makeToast
import epeyk.mobile.baseutil.showDialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.showSimpleDialog).setOnClickListener {
            showDialog(getString(R.string.dialog_text), {
                makeToast("Confirm Clicked", EnumToastType.TOAST_TYPE_SUCCESS)
                it.dismiss()
            }, hideCancelBtn = false)
        }
    }
}
