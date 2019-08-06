package epeyk.mobile.baseutil.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import epeyk.mobile.baseutil.R

open class RoundedImageView : androidx.appcompat.widget.AppCompatImageView {
    private val clipPath = Path()
    private val rect by lazy {
        RectF(0f, 0f, this.width.toFloat(), this.height.toFloat())
    }

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttributes(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initAttributes(context, attrs)
    }

    private fun initAttributes(ctx: Context, attrs: AttributeSet) {
        val a = ctx.obtainStyledAttributes(attrs, R.styleable.RoundedImageView)
        radius = a.getFloat(R.styleable.RoundedImageView_civ_radius, 18.0f)
        a.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW)
        canvas.clipPath(clipPath)
        super.onDraw(canvas)
    }

    companion object {

        var radius: Float = 0.0f
    }
}
