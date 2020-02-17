package mehrpars.mobile.baseutil.views

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.core.animation.addListener

/**
 * Created by Ali Arasteh on 1/3/2017.
 */

class MyOnClickListener(private val context: Context, private val listener: View.OnClickListener?) :
    View.OnClickListener {
    private var soundResource = 0
    private var mp: MediaPlayer? = null
    private val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.8f, 1.0f)
    private val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.8f, 1.0f)

    constructor(context: Context, soundResource: Int, listener: View.OnClickListener) : this(context, listener) {
        this.soundResource = soundResource
    }

    override fun onClick(view: View) {
        ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY).apply {
            duration = 500
            interpolator = OvershootInterpolator()
            addListener(onEnd = {
                listener?.onClick(view)
            })
        }.start()

        if (soundResource != 0 && soundEnabled()) {
            mp = MediaPlayer.create(context, soundResource)
            mp!!.start()
            mp!!.setOnCompletionListener { mep ->
                mep.stop()
                mep.reset()
                mep.release()
                mp = null
            }
        }
    }

    private fun soundEnabled(): Boolean {
        val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return am.ringerMode == AudioManager.RINGER_MODE_NORMAL
    }
}
