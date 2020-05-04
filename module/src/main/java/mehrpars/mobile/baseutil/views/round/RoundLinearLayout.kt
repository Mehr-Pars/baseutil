package vgame.ir.view.component.round

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout

class RoundLinearLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {
    /** use delegate to set attr  */
    val delegate: RoundViewDelegate

    init {
        delegate = RoundViewDelegate(this, context, attrs!!)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (delegate.isWidthHeightEqual!! && width > 0 && height > 0) {
            val max = Math.max(width, height)
            val measureSpec = View.MeasureSpec.makeMeasureSpec(max, View.MeasureSpec.EXACTLY)
            super.onMeasure(measureSpec, measureSpec)
            return
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (delegate.isRadiusHalfHeight!!) {
            delegate.cornerRadius = height / 2
        } else {
            delegate.setBgSelector()
        }
    }
}
