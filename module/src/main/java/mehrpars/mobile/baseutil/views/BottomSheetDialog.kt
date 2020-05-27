package mehrpars.mobile.baseutil.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import mehrpars.mobile.baseutil.R
import mehrpars.mobile.baseutil.databinding.BottomSheetDialogBinding

class BottomSheetDialog : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetDialogBinding
    var confirmAction: (BottomSheetDialogFragment) -> Unit = EMPTY_LAMBDA
    var cancelAction: (BottomSheetDialogFragment) -> Unit = EMPTY_LAMBDA

    companion object {
        val EMPTY_LAMBDA: (BottomSheetDialogFragment) -> Unit = {}
        private const val KEY_DIALOG_TEXT = "dialog_text"
        private const val KEY_CONFIRM_TEXT = "confirm_text"
        private const val KEY_CANCEL_TEXT = "cancel_text"
        private const val KEY_SHOW_CANCEL_BTN = "show_cancel_btn"

        fun getInstance(
            dialogText: String,
            confirmBtnText: String = "",
            cancelBtnText: String = "",
            showCancelBtn: Boolean = false
        ): BottomSheetDialog {
            return BottomSheetDialog().apply {
                arguments = Bundle().apply {
                    putString(KEY_DIALOG_TEXT, dialogText)
                    putString(KEY_CONFIRM_TEXT, confirmBtnText)
                    putString(KEY_CANCEL_TEXT, cancelBtnText)
                    putBoolean(KEY_SHOW_CANCEL_BTN, showCancelBtn)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_dialog, container, false)

        binding.cancelBtn.setOnClickListener {
            if (cancelAction != EMPTY_LAMBDA) cancelAction(this)
            else dialog?.dismiss()
        }
        binding.confirmBtn.setOnClickListener {
            if (confirmAction != EMPTY_LAMBDA) confirmAction(
                this
            )
            else dialog?.dismiss()
        }

        arguments?.let {
            binding.title = it.getString(KEY_DIALOG_TEXT)
            binding.confirm = it.getString(KEY_CONFIRM_TEXT)
            binding.cancel = it.getString(KEY_CANCEL_TEXT)
            val showCancelBtn = it.getBoolean(KEY_SHOW_CANCEL_BTN, false)
            if (cancelAction == EMPTY_LAMBDA && !showCancelBtn)
                binding.cancelBtn.visibility = View.GONE
        }

        return binding.root
    }

}