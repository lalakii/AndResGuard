package cn.lalaki.andresguarddemo

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.security.SecureRandom
import java.util.concurrent.atomic.AtomicBoolean

class MainActivity : AppCompatActivity(), Runnable, View.OnClickListener {
    private lateinit var mTextView: TextView
    private var pause = AtomicBoolean(false)
    private val mSecureRandom by lazy {
        SecureRandom()
    }
    private var mMaxWith: Int = 0
    private var mMaxHeight: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        mTextView = findViewById(R.id.textView)
        mTextView.setOnClickListener(this)
    }

    override fun onStop() {
        super.onStop()
        pause.set(true)
    }

    override fun onResume() {
        super.onResume()
        pause.set(false)
        mTextView.post(this)
    }

    override fun run() {
        if (mMaxWith == 0 || mMaxHeight == 0) {
            val parent = mTextView.parent
            if (parent is ViewGroup) {
                mMaxWith = parent.width
                mMaxHeight = parent.height
            }
        }
        val viewWidth = mTextView.measuredWidth
        val viewHeight = mTextView.measuredHeight
        val maxX = mMaxWith - viewWidth
        val maxY = mMaxHeight - viewHeight
        val randomX = mSecureRandom.nextInt(maxX + 1).toFloat()
        val randomY = mSecureRandom.nextInt(maxY + 1).toFloat()
        mTextView.setTextColor(randColor())
        mTextView.setBackgroundColor(randColor())
        mTextView.translationX = randomX
        mTextView.translationY = randomY
        mTextView.textSize = mSecureRandom.nextInt(45) + 7f
        if (!pause.get()) {
            mTextView.postDelayed(this, 220)
        }
    }

    private fun randColor(): Int {
        return Color.argb(
            255,
            mSecureRandom.nextInt(256),
            mSecureRandom.nextInt(256),
            mSecureRandom.nextInt(256)
        )
    }

    override fun onClick(view: View?) {
        mTextView.removeCallbacks(this)
        mTextView.post {
            mTextView.setTextColor(getColor(R.color.purple_700))
            mTextView.textSize = 45f
            mTextView.setBackgroundColor(getColor(R.color.white))
            val layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.gravity = Gravity.CENTER
            mTextView.translationX = 0f
            mTextView.translationY = 0f
            mTextView.layoutParams = layoutParams
            mTextView.invalidate()
        }
        Toast.makeText(this, R.string.attach, Toast.LENGTH_SHORT).show()
    }
}
