package com.mobeiwsq.engine_project.view.widget.edittext

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.animation.Animation
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import androidx.appcompat.widget.AppCompatEditText
import com.mobeiwsq.engine_project.R
import com.mobeiwsq.engine_project.utils.DensityUtils
import com.mobeiwsq.engine_project.utils.ResUtils

/**
 * 带删除按钮的输入框
 *
 * @author : mobeiwsq
 * @since :  2025/4/17 10:08
 */
class ClearEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.attr.ClearEditTextStyle
) :
    AppCompatEditText(context, attrs, defStyle), OnFocusChangeListener, TextWatcher {
    /**
     * 增大点击区域
     */
    private var mExtraClickArea = 0

    /**
     * 删除按钮的引用
     */
    private var mClearDrawable: Drawable? = null

    init {
        initAttrs(context, attrs, defStyle)
    }

    fun setExtraClickAreaSize(extraClickArea: Int): ClearEditText {
        mExtraClickArea = extraClickArea
        return this
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        mExtraClickArea = DensityUtils.dp2px(context, 20f)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClearEditText, defStyleAttr, 0)
        mClearDrawable = ResUtils.getDrawableAttrRes(getContext(), typedArray, R.styleable.ClearEditText_cet_clearIcon)
        val iconSize = typedArray.getDimensionPixelSize(R.styleable.ClearEditText_cet_clearIconSize, 0)
        typedArray.recycle()

        if (mClearDrawable == null) {
            //获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
            mClearDrawable = compoundDrawablesRelative[2]
            if (mClearDrawable == null) {
                mClearDrawable = ResUtils.getVectorDrawable(context, R.drawable.ic_default_clear_btn)
            }
        }
        if (iconSize != 0) {
            mClearDrawable?.setBounds(0, 0, iconSize, iconSize)
        } else {
            mClearDrawable?.setBounds(
                0,
                0,
                mClearDrawable?.intrinsicWidth ?: iconSize,
                mClearDrawable?.intrinsicHeight ?: iconSize
            )
        }
        setClearIconVisible(false)
        onFocusChangeListener = this
        addTextChangedListener(this)
    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件
     * 当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和
     * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向没有考虑
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (compoundDrawablesRelative[2] != null) {
            if (event.action == MotionEvent.ACTION_UP) {
                val touchable = isTouchable(event)
                if (touchable) {
                    this.setText("")
                }
            }
        }

        return super.onTouchEvent(event)
    }

    private fun isTouchable(event: MotionEvent): Boolean {
        return if (isRtl()) {
            event.x > paddingLeft - mExtraClickArea && event.x < paddingLeft + (mClearDrawable?.intrinsicWidth
                ?: 0) + mExtraClickArea
        } else {
            event.x > width - paddingRight - (mClearDrawable?.intrinsicWidth
                ?: 0) - mExtraClickArea && event.x < width - paddingRight + mExtraClickArea
        }
    }

    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus) {
            val length = if (text != null) text!!.length else 0
            setClearIconVisible(length > 0)
        } else {
            setClearIconVisible(false)
        }
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    protected fun setClearIconVisible(visible: Boolean) {
        val end = if (visible) mClearDrawable else null
        setCompoundDrawablesRelative(
            compoundDrawablesRelative[0],
            compoundDrawablesRelative[1], end, compoundDrawablesRelative[3]
        )
    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    override fun onTextChanged(
        s: CharSequence, start: Int, count: Int,
        after: Int
    ) {
        setClearIconVisible(s.isNotEmpty())
    }

    override fun beforeTextChanged(
        s: CharSequence?, start: Int, count: Int,
        after: Int
    ) {
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        setClearIconVisible(false)
    }

    /**
     * 设置晃动动画
     */
    fun setShakeAnimation() {
        this.animation = shakeAnimation(5)
    }

    private fun isRtl(): Boolean {
        return layoutDirection == LAYOUT_DIRECTION_RTL
    }

    companion object {
        /**
         * 晃动动画
         *
         * @param counts 1秒钟晃动多少下
         * @return
         */
        fun shakeAnimation(counts: Int): Animation {
            val translateAnimation: Animation = TranslateAnimation(0f, 10f, 0f, 0f)
            translateAnimation.interpolator = CycleInterpolator(counts.toFloat())
            translateAnimation.duration = 1000
            return translateAnimation
        }
    }
}



