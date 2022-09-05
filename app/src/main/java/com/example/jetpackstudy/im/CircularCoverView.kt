package com.example.jetpackstudy.im

import kotlin.jvm.JvmOverloads
import android.os.Build
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import com.example.jetpackstudy.R
import java.lang.Error

class CircularCoverView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var leftTopRadians = 30 //leftTopRadians
    private var leftBottomRadians = 30 //leftBottomRadians
    private var rightTopRadians = 30 //rightTopRadians
    private var rightBottomRadians = 30 //rightBottomRadians
    private var border = 0
    private var coverColor = -0x151516 //color of cover.

    /**
     * set radians of cover.
     */
    fun setRadians(
        leftTopRadians: Int,
        rightTopRadians: Int,
        leftBottomRadians: Int,
        rightBottomRadians: Int,
        border: Int
    ) {
        this.leftTopRadians = leftTopRadians
        this.rightTopRadians = rightTopRadians
        this.leftBottomRadians = leftBottomRadians
        this.rightBottomRadians = rightBottomRadians
        this.border = border
    }

    /**
     * set color of cover.
     *
     * @param coverColor cover's color
     */
    fun setCoverColor(@ColorInt coverColor: Int) {
        this.coverColor = coverColor
    }

    /**
     * create a sector-bitmap as the dst.
     *
     * @param w width of bitmap
     * @param h height of bitmap
     * @return bitmap
     */
    private fun drawSector(w: Int, h: Int): Bitmap {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bm)
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.color = -0x33bc //notice:cannot set transparent color here.otherwise cannot clip at final.
        c.drawArc(
            RectF(
                border.toFloat(),
                border.toFloat(),
                (leftTopRadians * 2 + border).toFloat(),
                (leftTopRadians * 2 + border).toFloat()
            ), 180f, 90f, true, p
        )
        c.drawArc(
            RectF(
                border.toFloat(),
                (height - leftBottomRadians * 2 - border).toFloat(),
                (leftBottomRadians * 2 + border).toFloat(),
                (height - border).toFloat()
            ), 90f, 90f, true, p
        )
        c.drawArc(
            RectF(
                (width - rightTopRadians * 2 - border).toFloat(),
                border.toFloat(),
                (width - border).toFloat(),
                (rightTopRadians * 2 + border).toFloat()
            ), 270f, 90f, true, p
        )
        c.drawArc(
            RectF(
                (width - rightBottomRadians * 2 - border).toFloat(),
                (height - rightBottomRadians * 2 - border).toFloat(),
                (width - border).toFloat(),
                (height - border).toFloat()
            ), 0f, 90f, true, p
        )
        return bm
    }

    /**
     * create a rect-bitmap as the src.
     *
     * @param w width of bitmap
     * @param h height of bitmap
     * @return bitmap
     */
    private fun drawRect(w: Int, h: Int): Bitmap {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bm)
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.color = coverColor
        c.drawRect(
            RectF(
                border.toFloat(),
                border.toFloat(),
                (leftTopRadians + border).toFloat(),
                (leftTopRadians + border).toFloat()
            ), p
        )
        c.drawRect(
            RectF(
                border.toFloat(),
                (height - leftBottomRadians - border).toFloat(),
                (leftBottomRadians + border).toFloat(),
                (height - border).toFloat()
            ), p
        )
        c.drawRect(
            RectF(
                (width - rightTopRadians - border).toFloat(),
                border.toFloat(),
                (width - border).toFloat(),
                (rightTopRadians + border).toFloat()
            ), p
        )
        c.drawRect(
            RectF(
                (width - rightBottomRadians - border).toFloat(),
                (height - rightBottomRadians - border).toFloat(),
                (width - border).toFloat(),
                (height - border).toFloat()
            ), p
        )
        c.drawRect(RectF(0F, 0F, width.toFloat(), border.toFloat()), p)
        c.drawRect(RectF(0F, 0F, border.toFloat(), height.toFloat()), p)
        c.drawRect(RectF((width - border).toFloat(), 0F, width.toFloat(), height.toFloat()), p)
        c.drawRect(RectF(0F, (height - border).toFloat(), width.toFloat(), height.toFloat()), p)
        return bm
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        try {
            val paint = Paint()
            paint.isFilterBitmap = false
            paint.style = Paint.Style.FILL

            //create a canvas layer to show the mix-result
            @SuppressLint("WrongConstant") val sc =
                canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)
            //draw sector-dst-bitmap at first.
            canvas.drawBitmap(drawSector(width, height), 0f, 0f, paint)
            //set Xfermode of paint.
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
            //then draw rect-src-bitmap
            canvas.drawBitmap(drawRect(width, height), 0f, 0f, paint)
            paint.xfermode = null
            //restore the canvas
            canvas.restoreToCount(sc)
        } catch (e: Error) {
        }
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircularCoverView)
        leftTopRadians = typedArray.getDimensionPixelSize(
            R.styleable.CircularCoverView_left_top_radius,
            leftTopRadians
        )
        leftBottomRadians = typedArray.getDimensionPixelSize(
            R.styleable.CircularCoverView_left_bottom_radius,
            leftBottomRadians
        )
        rightTopRadians = typedArray.getDimensionPixelSize(
            R.styleable.CircularCoverView_right_top_radius,
            rightTopRadians
        )
        rightBottomRadians = typedArray.getDimensionPixelSize(
            R.styleable.CircularCoverView_right_bottom_radius,
            rightBottomRadians
        )
        coverColor = typedArray.getColor(R.styleable.CircularCoverView_cover_color, coverColor)
    }
}