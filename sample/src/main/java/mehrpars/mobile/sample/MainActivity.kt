package mehrpars.mobile.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import mehrpars.mobile.baseutil.EnumToastType
import mehrpars.mobile.baseutil.makeToast
import mehrpars.mobile.baseutil.showDialog

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
