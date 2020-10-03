package com.rojoxpress.slidebutton

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.res.TypedArray
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.content.ContextCompat


open class SlideButton: FrameLayout {

    private var textView: TextView? = null
    private var slideBar: SlideBar? = null
    private var onSlideListener: (()-> Unit)? = null
    private var slideChangeListener: OnSlideChangeListener? = null
    private var offsetThumb = 0

    constructor(context: Context): this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int): super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }


    open fun dpToPixels(dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }

    private fun init(set: AttributeSet?) {
        offsetThumb = dpToPixels(16)
        textView = TextView(context)
        slideBar = SlideBar(context)
        val childParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        childParams.gravity = Gravity.CENTER
        slideBar!!.layoutParams = childParams
        textView!!.layoutParams = childParams
        slideBar!!.progressDrawable = ContextCompat.getDrawable(context, R.drawable.back_slide_layer)
        textView!!.gravity = Gravity.CENTER
        if (set != null) {
            val a: TypedArray = context.obtainStyledAttributes(set, R.styleable.slider_button, 0, 0)
            if (a.hasValue(R.styleable.slider_button_text)) {
                val buttonText = a.getString(R.styleable.slider_button_text)
                setText(buttonText)
            }
            if (a.hasValue(R.styleable.slider_button_thumb)) {
                val thumbDrawable: Drawable? = a.getDrawable(R.styleable.slider_button_thumb)
                slideBar!!.thumb = thumbDrawable!!
            } else {
                slideBar!!.thumb = ContextCompat.getDrawable(context, R.drawable.thumb_def)!!
            }
            if (a.hasValue(R.styleable.slider_button_thumbOffset)) {
                val offset = a.getDimensionPixelSize(R.styleable.slider_button_thumbOffset, dpToPixels(10))
                offsetThumb += offset
            }
            if (a.hasValue(R.styleable.slider_button_sliderBackground)) {
                setBackgroundDrawable(a.getDrawable(R.styleable.slider_button_sliderBackground))
            } else {
                setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.back_slide_button))
            }
            val unitsTextSize = a.getDimensionPixelSize(R.styleable.slider_button_textSize, dpToPixels(20)).toFloat()
            textView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitsTextSize)
            val color = a.getColor(R.styleable.slider_button_textColor, Color.WHITE)
            textView!!.setTextColor(color)
            a.recycle()
        }
        setThumbOffset(offsetThumb)
        this.addView(textView)
        this.addView(slideBar)
    }

    open fun getTexView(): TextView? {
        return textView
    }


    open fun setText(@StringRes res: Int) {
        textView!!.setText(res)
    }

    open fun setText(charSequence: CharSequence?) {
        textView!!.text = charSequence
    }

    open fun setThumb(drawable: Drawable) {
        slideBar!!.thumb = drawable
    }

    open fun setThumbOffset(offset: Int) {
        slideBar!!.thumbOffset = offset
    }

    open fun setOnSlideChangeListener(slideChangeListener: OnSlideChangeListener?) {
        this.slideChangeListener = slideChangeListener
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        slideBar!!.isEnabled = enabled
        textView!!.isEnabled = enabled
        var color = 0
        if (!enabled) {
            color = ContextCompat.getColor(getContext(), R.color.disabled_filter)
            textView!!.visibility = View.GONE
        } else {
            textView!!.visibility = View.VISIBLE
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            slideBar!!.thumb.colorFilter = BlendModeColorFilter(color, BlendMode.XOR)
        } else {
            slideBar!!.thumb.setColorFilter(color, PorterDuff.Mode.XOR)
        }
    }


    inner class SlideBar : AppCompatSeekBar {
        private var thumb: Drawable? = null
        private val seekBarChangeListener: OnSeekBarChangeListener = object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                onSlideChange(i.toFloat() / max)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        }

        constructor(context: Context?) : super(context!!) {
            init()
        }

        constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) {
            init()
        }

        constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
            init()
        }

        override fun setThumb(thumb: Drawable) {
            super.setThumb(thumb)
            this.thumb = thumb
        }

        override fun getThumb(): Drawable {
            return thumb!!
        }

        private fun init() {
            max = 100
            setOnSeekBarChangeListener(seekBarChangeListener)
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouchEvent(event: MotionEvent): Boolean {
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (thumb!!.bounds.contains(event.x.toInt(), event.y.toInt())) {
                    super.onTouchEvent(event)
                } else return false
            } else if (event.action == MotionEvent.ACTION_UP) {
                if (progress > 90) onSlide()
                progress = 0
            } else super.onTouchEvent(event)
            return true
        }

        private fun onSlide() {
            onSlideListener?.apply {
                this()
            }
        }

        private fun onSlideChange(position: Float) {
            slideChangeListener?.onSlideChange(position)

        }
    }
    @Deprecated("use setOnSlideListener instead")
    open fun setSlideButtonListener(onSlideListener: (()->Unit)?) {
        this.onSlideListener = onSlideListener
    }

    open fun setOnSlideListener(onSlideListener: (()->Unit)?) {
        this.onSlideListener = onSlideListener
    }

    interface SlideButtonListener {
        fun onSlide()
    }

    interface OnSlideChangeListener {
        fun onSlideChange(position: Float)
    }

}