package rojoxpress.slideexample

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.rojoxpress.slidebutton.SlideButton
import com.rojoxpress.slidebutton.SlideButton.OnSlideChangeListener
import com.rojoxpress.slidebutton.SlideButton.SlideButtonListener

open class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<View>(R.id.progress) as TextView
        val slideButton = findViewById<View>(R.id.slide_button) as SlideButton
        val switchCompat = findViewById<View>(R.id.switch_) as SwitchCompat

        slideButton.setOnSlideListener {
            Toast.makeText(this@MainActivity, "UNLOCKED", Toast.LENGTH_SHORT).show()
        }

        slideButton.setOnSlideChangeListener(object : OnSlideChangeListener {
            override fun onSlideChange(position: Float) {
                textView.text = "Progress: $position"
            }
        })

        switchCompat.setOnCheckedChangeListener { compoundButton, b -> slideButton.isEnabled = b }
    }

}