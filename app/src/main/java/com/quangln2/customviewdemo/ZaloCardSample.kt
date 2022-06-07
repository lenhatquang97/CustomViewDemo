package com.quangln2.customviewdemo

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import kotlin.math.min


private const val PAD_SPACE = 30f

class ZaloCardSample : View {
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : super(context, attrs, defStyleAttr) {
        attributeArray = context.obtainStyledAttributes(attrs, R.styleable.ZaloCardSample, defStyleAttr, 0)
        try{
            headingName = attributeArray?.getString(R.styleable.ZaloCardSample_headingName).toString()
            contentName = attributeArray?.getString(R.styleable.ZaloCardSample_contentName).toString()
            imageSrc = attributeArray?.getResourceId(R.styleable.ZaloCardSample_imageSrc, -1)!!
        } finally {
            attributeArray?.recycle()
        }
    }
    private var headingName = ""
    private var contentName = ""
    private var imageSrc = R.drawable.ic_launcher_foreground
    private var attributeArray: TypedArray? = null


    private val avatarPosition: PointF = PointF(0f, 0f)
    private val headingPosition: PointF = PointF(0f, 0f)
    private val subHeadingPosition: PointF = PointF(0f, 0f)
    private val timePosition: PointF = PointF(0f, 0f)
    private var radius = 0.0f
    private var padding = 30.0f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT
    }
    private val headingStyle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 50.0f
        textAlign = Paint.Align.LEFT
        typeface = Typeface.DEFAULT_BOLD
    }
    private val subHeadingStyle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 40.0f
        textAlign = Paint.Align.LEFT
        typeface = Typeface.DEFAULT
    }
    private val timeStyle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 40.0f
        textAlign = Paint.Align.RIGHT
        typeface = Typeface.DEFAULT
    }
    private val avatarStyle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 20.0f
        strokeCap = Paint.Cap.ROUND
    }

    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0){

    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        radius = (min(width, height) / 2.0 * 0.15).toFloat()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        var heightSpec = 0
        if(layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT){
            radius = ( getScreenWidth() / 2.0 * 0.15).toFloat()
            heightSpec = MeasureSpec.makeMeasureSpec(
                (radius * 2 + PAD_SPACE * 2).toInt(),
                MeasureSpec.AT_MOST)

        } else {
            heightSpec = heightMeasureSpec
        }
        super.onMeasure(widthMeasureSpec,  heightSpec)
    }
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }
    private fun getScreenWidth(): Int {
        return Resources.getSystem().getDisplayMetrics().widthPixels
    }
    fun getCircledBitmap(bitmap: Bitmap): Bitmap? {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle(
            (bitmap.width / 2).toFloat(),
            (bitmap.height / 2).toFloat(), (bitmap.width / 2).toFloat(), paint
        )
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        radius = ( getScreenWidth() / 2.0 * 0.15).toFloat()
        avatarPosition.x = radius + padding
        avatarPosition.y = radius + padding
        canvas?.drawCircle(avatarPosition.x, avatarPosition.y, radius, paint)

        headingPosition.x = 2 * radius + 2* padding
        headingPosition.y = radius / 2.0f + 2 * padding
        canvas?.drawText(headingName, headingPosition.x, headingPosition.y, headingStyle)

        subHeadingPosition.x = 2 * radius + 2* PAD_SPACE
        subHeadingPosition.y = 1.25f * radius + 2 * PAD_SPACE
        canvas?.drawText(contentName, subHeadingPosition.x, subHeadingPosition.y, subHeadingStyle)

        timePosition.x = width - 2 * padding
        timePosition.y = radius / 2.0f + 2 * padding
        canvas?.drawText("20:00", timePosition.x, timePosition.y, timeStyle)

        var bitmap = BitmapFactory.decodeResource(resources,imageSrc)
        bitmap = getCircledBitmap(bitmap)
        val left = (avatarPosition.x - radius).toInt()
        val top = (avatarPosition.y - radius).toInt()
        val rect = Rect(0,0, bitmap.width, bitmap.height)
        val rectF = RectF(left.toFloat(), top.toFloat(), left + 2 * radius, top + 2 * radius)
        canvas?.drawBitmap(bitmap, rect, rectF, avatarStyle)
    }
    private var listener: OnClickListener? = null

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        listener = l
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_UP -> {
                listener?.onClick(this)
            }
        }
        invalidate()
        return true
    }

}