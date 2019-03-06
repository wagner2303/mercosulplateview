package com.wagner2303.mercosulplateview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.withScale
import androidx.core.graphics.withTranslation


class MercosulPlateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr){

    var plate: String = ""
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    var type: MercosulPlateType = MercosulPlateType.Especial
        set(value) {
            field = value
            borderDrawable = ContextCompat.getDrawable(context, R.drawable.border)?.apply {
                setBounds(0,0,400,130)
                setColorFilter(
                    ContextCompat.getColor(context, value.colorId),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
            platePaint = Paint(platePaint).apply {
                color = ContextCompat.getColor(context, value.colorId)
            }
            invalidate()
            requestLayout()
        }

    var country: MercosulCountry = MercosulCountry.BRASIL
        set(value) {
            field = value
            countryFlagDrawable = value.flagRes?.let {
                ContextCompat.getDrawable(context, it)?.apply {
                    setBounds(0,0,200,140)
                }
            }
            mercosurLogoDrawable = ContextCompat.getDrawable(context, value.mercosurLogoRes)?.apply {
                setBounds(0,0,200,130)
            }
            invalidate()
            requestLayout()
        }

    var gravity : Int = Gravity.START or Gravity.TOP
        set(value) {
            if (field == value) return
            field = value
            invalidate()
            requestLayout()
        }

    private val backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.background)?.apply {
        setBounds(0,0,400,130)
    }
    private val upperBarDrawable = ContextCompat.getDrawable(context, R.drawable.upper_bar)?.apply {
        setBounds(0,0,390,33)
    }
    private var borderDrawable : Drawable? = null
    private var countryFlagDrawable : Drawable? = null
    private var mercosurLogoDrawable : Drawable? = null
    private val countryPaint : Paint = Paint().apply {
        typeface = ResourcesCompat.getFont(context, R.font.lato_bold)
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        color = Color.WHITE
        textSize = 16f
    }
    private var platePaint : Paint = Paint().apply {
        typeface = ResourcesCompat.getFont(context, R.font.kraftfahrzeugkennzeichen)
        isAntiAlias = true
        textSize = 86f
        textAlign = Paint.Align.CENTER
    }

    private val debugPaint = Paint().apply {
        color = Color.RED
    }

    init {
        if (attrs != null){
            getValues(context, attrs)
        }
    }

    private fun getValues(context: Context, attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MercosulPlateView,
            0,0
        ).apply {
            try {
                plate = getString(R.styleable.MercosulPlateView_plate) ?: ""
                type = MercosulPlateType.fromId(getInt(R.styleable.MercosulPlateView_type, 0))
                    ?: MercosulPlateType.Particular
                country = MercosulCountry.fromId(getInt(R.styleable.MercosulPlateView_country, 0))
                    ?: MercosulCountry.BRASIL
                gravity = getInt(R.styleable.MercosulPlateView_android_gravity, Gravity.START or Gravity.TOP)
            } finally {
                recycle()
            }
        }
    }

    private var widthPlate = 0
    private var heightPlate = 0
    private var scale = 1f
    private var dWidth = 0f
    private var dHeight = 0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val wAvailable = w - (paddingLeft + paddingRight)
        val hAvailable = h - (paddingTop + paddingBottom)

        if ((wAvailable.toDouble() / hAvailable) >= PLATE_RATIO){
            // fills height
            widthPlate = Math.floor(hAvailable * PLATE_RATIO).toInt()
            heightPlate = Math.round(widthPlate * PLATE_INV_RATIO).toInt()
        } else {
            // fills width
            heightPlate = Math.floor(wAvailable * PLATE_INV_RATIO).toInt()
            widthPlate = Math.round(heightPlate * PLATE_RATIO).toInt()
        }

        scale = widthPlate/400f

        val container = Rect(0,0, wAvailable, hAvailable)
        val platePosition = Rect()
        Gravity.apply(gravity, widthPlate, heightPlate, container, platePosition)
        dWidth = platePosition.left.toFloat() + paddingLeft
        dHeight = platePosition.top.toFloat() + paddingTop
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.withTranslation(dWidth, dHeight) { withScale(scale, scale) {
            backgroundDrawable?.draw(this)
            withTranslation(5f, 5f) {
                upperBarDrawable?.draw(this)
            }
            withTranslation(360f, 14f) { withScale(1/8f, 1/8f){
                countryFlagDrawable?.draw(this)
            }}
            withTranslation(14f, 14f) { withScale( 1/8f, 1/8f) {
                mercosurLogoDrawable?.draw(this)
            }}
            borderDrawable?.draw(this)
            drawText(country.countryName, 200f, 29f, countryPaint)
            drawText(plate, 200f, 110f, platePaint)
        }}
    }

    companion object {
        val PLATE_RATIO = 40.0/13.0
        val PLATE_INV_RATIO = 1 / PLATE_RATIO
    }
}