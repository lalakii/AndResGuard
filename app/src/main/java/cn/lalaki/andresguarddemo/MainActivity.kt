package cn.lalaki.andresguarddemo

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            TextView(this).apply {
                text = getString(R.string.question)
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                textSize = 33f
            },
        )
    }
}
