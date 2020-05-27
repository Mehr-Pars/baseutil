package mehrpars.mobile.baseutil.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import mehrpars.mobile.baseutil.R
import mehrpars.mobile.baseutil.databinding.BottomSheetDialogBinding

class BottomSheetDialog : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetDialogBinding
    var confirmAction: (BottomSheetDialogFragment) -> Unit = EMPTY_LAMBDA
    var cancelAction: (BottomSheetDialogFragment) -> Unit = EMPTY_LAMBDA

    companion object {
        val EMPTY_LAMBDA: (BottomSheetDialogFragment) -> Unit = {}
        const val KEY_DIALOG_TEXT = "dialog_text"
        const val KEY_CONFIRM_TEXT = "confirm_text"
        const val KEY_CANCEL_TEXT = "cancel_text"
        const val KEY_SHOW_CANCEL_BTN = "show_cancel_btn"

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

class BottomSheetBuilder(val context: Context) {
    private var mDialogText: String = ""
    private var mConfirmBtnText: String = ""
    private var mCancelBtnText: String = ""
    private var mShowCancelBtn: Boolean = false
    private var mCancelable: Boolean = true
    private var mConfirmAction: (BottomSheetDialogFragment) -> Unit = BottomSheetDialog.EMPTY_LAMBDA
    private var mCancelAction: (BottomSheetDialogFragment) -> Unit = BottomSheetDialog.EMPTY_LAMBDA

    fun setText(@StringRes dialogTextId: Int): BottomSheetBuilder {
        mDialogText = context.getString(dialogTextId)
        return this
    }

    fun setText(dialogText: String): BottomSheetBuilder {
        mDialogText = dialogText
        return this
    }

    fun setConfirmBtnText(@StringRes confirmBtnTextId: Int): BottomSheetBuilder {
        mConfirmBtnText = context.getString(confirmBtnTextId)
        return this
    }

    fun setConfirmBtnText(confirmBtnText: String): BottomSheetBuilder {
        mConfirmBtnText = confirmBtnText
        return this
    }

    fun setCancelBtnText(@StringRes cancelBtnTextId: Int): BottomSheetBuilder {
        mCancelBtnText = context.getString(cancelBtnTextId)
        return this
    }

    fun setCancelBtnText(cancelBtnText: String): BottomSheetBuilder {
        mCancelBtnText = cancelBtnText
        return this
    }

    fun setShowCancelBtn(showCancelBtn: Boolean): BottomSheetBuilder {
        mShowCancelBtn = showCancelBtn
        return this
    }

    fun setCancelable(cancelable: Boolean): BottomSheetBuilder {
        mCancelable = cancelable
        return this
    }

    fun setConfirmAction(confirmAction: (BottomSheetDialogFragment) -> Unit): BottomSheetBuilder {
        mConfirmAction = confirmAction
        return this
    }

    fun setCancelAction(cancelAction: (BottomSheetDialogFragment) -> Unit): BottomSheetBuilder {
        mCancelAction = cancelAction
        return this
    }

    fun build(): BottomSheetDialog {
        val bottomSheet = BottomSheetDialog().apply {
            arguments = Bundle().apply {
                putString(BottomSheetDialog.KEY_DIALOG_TEXT, mDialogText)
                putString(BottomSheetDialog.KEY_CONFIRM_TEXT, mConfirmBtnText)
                putString(BottomSheetDialog.KEY_CANCEL_TEXT, mCancelBtnText)
                putBoolean(BottomSheetDialog.KEY_SHOW_CANCEL_BTN, mShowCancelBtn)
            }
        }
        bottomSheet.confirmAction = mConfirmAction
        bottomSheet.cancelAction = mCancelAction
        bottomSheet.isCancelable = mCancelable

        return bottomSheet
    }

    fun show() {
        val fragmentManager: FragmentManager = when (context) {
            is Fragment -> context.childFragmentManager
            is FragmentActivity -> context.supportFragmentManager
            else -> throw Exception("context is not type of Fragment nor FragmentActivity")
        }
        build().show(fragmentManager, "BottomSheet")
    }
}