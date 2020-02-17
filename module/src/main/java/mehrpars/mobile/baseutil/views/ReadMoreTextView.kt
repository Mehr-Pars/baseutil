/*
 * Copyright (C) 2016 Borja Bravo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mehrpars.mobile.baseutil.views

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import mehrpars.mobile.baseutil.R

open class ReadMoreTextView constructor(context: Context, attrs: AttributeSet) :
    TextViewCustom(context, attrs) {

    private var mText: CharSequence? = null
    private var bufferType: BufferType? = null
    private var readMore = true
    private var trimLength: Int = 0
    private var trimCollapsedText: CharSequence? = null
    private var trimExpandedText: CharSequence? = null
    private val viewMoreSpan: ReadMoreClickableSpan
    private var colorClickableText: Int = 0
    private var showTrimExpandedText: Boolean = false

    private var trimMode: Int = 0
    private var lineEndIndex: Int = 0
    private var trimLines: Int = 0

    private val displayableText: CharSequence?
        get() = getTrimmedText(mText)

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ReadMoreTextView)
        this.trimLength = typedArray.getInt(R.styleable.ReadMoreTextView_trimLength, DEFAULT_TRIM_LENGTH)
        val resourceIdTrimCollapsedText =
            typedArray.getResourceId(R.styleable.ReadMoreTextView_trimCollapsedText, R.string.read_more)
        val resourceIdTrimExpandedText =
            typedArray.getResourceId(R.styleable.ReadMoreTextView_trimExpandedText, R.string.read_less)
        this.trimCollapsedText = resources.getString(resourceIdTrimCollapsedText)
        this.trimExpandedText = resources.getString(resourceIdTrimExpandedText)
        this.trimLines = typedArray.getInt(R.styleable.ReadMoreTextView_trimLines, DEFAULT_TRIM_LINES)
        this.colorClickableText = typedArray.getColor(
            R.styleable.ReadMoreTextView_colorClickableText,
            ContextCompat.getColor(context, R.color.colorPrimaryDark)
        )
        this.showTrimExpandedText =
            typedArray.getBoolean(R.styleable.ReadMoreTextView_showTrimExpandedText, DEFAULT_SHOW_TRIM_EXPANDED_TEXT)
        this.trimMode = typedArray.getInt(R.styleable.ReadMoreTextView_trimMode, TRIM_MODE_LINES)
        typedArray.recycle()

        viewMoreSpan = ReadMoreClickableSpan()
        onGlobalLayoutLineEndIndex()
        setText()
    }

    private fun setText() {
        super.setText(displayableText, bufferType)
        movementMethod = LinkMovementMethod.getInstance()
        highlightColor = Color.TRANSPARENT
    }

    override fun setText(text: CharSequence, type: BufferType) {
        this.mText = text
        bufferType = type
        setText()
    }

    private fun getTrimmedText(text: CharSequence?): CharSequence? {
        if (trimMode == TRIM_MODE_LENGTH) {
            if (text != null && text.length > trimLength) {
                return if (readMore) {
                    updateCollapsedText()
                } else {
                    updateExpandedText()
                }
            }
        }
        if (trimMode == TRIM_MODE_LINES) {
            if (text != null && lineEndIndex > 0) {
                if (readMore) {
                    if (layout.lineCount > trimLines) {
                        return updateCollapsedText()
                    }
                } else {
                    return updateExpandedText()
                }
            }
        }
        return text
    }

    private fun updateCollapsedText(): CharSequence {
        val textStripped = mText!!.toString().replace("\n", " ")
        var trimEndIndex = textStripped.length
        when (trimMode) {
            TRIM_MODE_LINES -> {
                trimEndIndex = lineEndIndex - (ELLIPSIZE.length + trimCollapsedText!!.length + 1)
                if (trimEndIndex < 0) {
                    if (trimLength > textStripped.length)
                        trimEndIndex = textStripped.length
                    else
                        trimEndIndex = trimLength + 1
                }
            }
            TRIM_MODE_LENGTH -> trimEndIndex = trimLength + 1
        }
        val s = SpannableStringBuilder(textStripped, 0, trimEndIndex)
            .append(ELLIPSIZE)
            .append(trimCollapsedText)
        return addClickableSpan(s, trimCollapsedText!!)
    }

    private fun updateExpandedText(): CharSequence? {
        if (showTrimExpandedText) {
            val s = SpannableStringBuilder(mText, 0, mText!!.length).append(trimExpandedText)
            return addClickableSpan(s, trimExpandedText!!)
        }
        return mText
    }

    private fun addClickableSpan(s: SpannableStringBuilder, trimText: CharSequence): CharSequence {
        s.setSpan(viewMoreSpan, s.length - trimText.length, s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return s
    }

    fun setTrimLength(trimLength: Int) {
        this.trimLength = trimLength
        setText()
    }

    fun setColorClickableText(colorClickableText: Int) {
        this.colorClickableText = colorClickableText
    }

    fun setTrimCollapsedText(trimCollapsedText: CharSequence) {
        this.trimCollapsedText = trimCollapsedText
    }

    fun setTrimExpandedText(trimExpandedText: CharSequence) {
        this.trimExpandedText = trimExpandedText
    }

    fun setTrimMode(trimMode: Int) {
        this.trimMode = trimMode
    }

    fun setShowTrimExpandedText(showTrimExpandedText: Boolean) {
        this.showTrimExpandedText = showTrimExpandedText
        if (!showTrimExpandedText)
            readMore = false

        setText()
    }

    fun setTrimLines(trimLines: Int) {
        this.trimLines = trimLines
    }

    private inner class ReadMoreClickableSpan : ClickableSpan() {
        override fun onClick(widget: View) {
            readMore = !readMore
            setText()
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = colorClickableText
        }
    }

    private fun onGlobalLayoutLineEndIndex() {
        if (trimMode == TRIM_MODE_LINES) {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val obs = viewTreeObserver
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        obs.removeOnGlobalLayoutListener(this)
                    } else {
                        obs.removeGlobalOnLayoutListener(this)
                    }
                    refreshLineEndIndex()
                    setText()
                }
            })
        }
    }

    private fun refreshLineEndIndex() {
        try {
            if (trimLines == 0) {
                lineEndIndex = layout.getLineEnd(0)
            } else if (trimLines > 0 && lineCount >= trimLines) {
                lineEndIndex = layout.getLineEnd(trimLines - 1)
            } else {
                lineEndIndex = INVALID_END_INDEX
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun collapse() {
        if (!readMore) {
            readMore = true
            setText()
        }
    }

    fun expand() {
        if (readMore) {
            readMore = false
            setText()
        }
    }

    companion object {

        private val TRIM_MODE_LINES = 0
        private val TRIM_MODE_LENGTH = 1
        private val DEFAULT_TRIM_LENGTH = 240
        private val DEFAULT_TRIM_LINES = 2
        private val INVALID_END_INDEX = -1
        private val DEFAULT_SHOW_TRIM_EXPANDED_TEXT = true
        private val ELLIPSIZE = " ... "
    }
}
