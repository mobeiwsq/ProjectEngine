package com.mobeiwsq.engine_project.view.title

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.mobeiwsq.engine_project.R
import com.mobeiwsq.engine_project.utils.Utils

/**
 * 标题栏
 *
 * @author : mobeiwsq
 * @since :  2025/4/9 15:12
 */

class TitleBar : ViewGroup, View.OnClickListener {

    val STATUS_BAR_HEIGHT_RES_NAME: String = "status_bar_height"
    val CENTER_CENTER: Int = 0
    val CENTER_LEFT: Int = 1
    val CENTER_RIGHT: Int = 2

    /**
     * 文字默认白色
     */
    var DEFAULT_TEXT_COLOR: Int = Color.WHITE

    /**-------TitleBar的几个主要布局控件 start-----------**/
    private lateinit var mLeftText: TextView
    private lateinit var mRightLayout: LinearLayout
    private lateinit var mCenterLayout: LinearLayout
    private lateinit var mCenterText: TextView
    private lateinit var mSubTitleText: TextView
    private lateinit var mCustomCenterView: View
    private lateinit var mDividerView: View
    /**-------TitleBar的几个主要布局控件 end-----------**/


    /**
     * 是否是沉浸模式
     */
    private var mImmersive = false

    /**
     * 屏幕宽
     */
    private var mScreenWidth = 0

    /**
     * 标题栏的高度
     */
    private var mBarHeight = 0

    /**
     * 状态栏的高度
     */
    private var mStatusBarHeight = 0

    /**
     * 点击动作控件的padding
     */
    private var mActionPadding = 0

    /**
     * 左右侧文字的padding
     */
    private var mSideTextPadding = 0
    private var mCenterGravity = 0

    private var mSideTextSize = 0
    private var mTitleTextSize = 0
    private var mSubTitleTextSize = 0
    private var mActionTextSize = 0

    private var mSideTextColor = 0
    private var mTitleTextColor = 0
    private var mSubTitleTextColor = 0
    private var mActionTextColor = 0

    private var mLeftImageResource: Drawable? = null
    private var mLeftTextString: String? = null
    private var mTitleTextString: String? = null
    private var mSubTextString: String? = null
    private var mDividerColor = 0
    private var mDivideHeight = 0
    private var mIsUseThemeColor = false


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.PageTitleBarStyle)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(context, attrs, defStyleAttr)
        init(context)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        if (isInEditMode) {
            return
        }

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar, defStyleAttr, 0)
        if (typedArray != null) {
            mBarHeight = typedArray.getDimensionPixelSize(
                R.styleable.TitleBar_tb_barHeight,
                Utils.resolveDimension(
                    getContext(),
                    R.attr.actionbar_height,
                    Utils.getDimensionPixelSize(getContext(), R.dimen.default_actionbar_height)
                )
            )

            mImmersive = typedArray.getBoolean(
                R.styleable.TitleBar_tb_immersive,
                Utils.resolveBoolean(context, R.attr.actionbar_immersive)
            )

            mActionPadding = typedArray.getDimensionPixelSize(
                R.styleable.TitleBar_tb_actionPadding,
                Utils.resolveDimension(
                    context,
                    R.attr.actionbar_action_padding,
                    Utils.getDimensionPixelSize(getContext(), R.dimen.default_action_padding)
                )
            )
            mSideTextPadding = typedArray.getDimensionPixelSize(
                R.styleable.TitleBar_tb_sideTextPadding,
                Utils.resolveDimension(
                    context,
                    R.attr.actionbar_side_text_padding,
                    Utils.getDimensionPixelSize(getContext(), R.dimen.default_sidetext_padding)
                )
            )
            mCenterGravity = typedArray.getInt(R.styleable.TitleBar_tb_centerGravity, CENTER_CENTER)

            mSideTextSize = typedArray.getDimensionPixelSize(
                R.styleable.TitleBar_tb_sideTextSize,
                Utils.resolveDimension(
                    context,
                    R.attr.actionbar_action_text_size,
                    Utils.getDimensionPixelSize(getContext(), R.dimen.default_action_text_size)
                )
            )
            mTitleTextSize = typedArray.getDimensionPixelSize(
                R.styleable.TitleBar_tb_titleTextSize,
                Utils.resolveDimension(
                    context,
                    R.attr.actionbar_title_text_size,
                    Utils.getDimensionPixelSize(getContext(), R.dimen.default_title_text_size)
                )
            )
            mSubTitleTextSize = typedArray.getDimensionPixelSize(
                R.styleable.TitleBar_tb_subTitleTextSize,
                Utils.resolveDimension(
                    context,
                    R.attr.actionbar_sub_text_size,
                    Utils.getDimensionPixelSize(getContext(), R.dimen.default_sub_text_size)
                )
            )
            mActionTextSize = typedArray.getDimensionPixelSize(
                R.styleable.TitleBar_tb_actionTextSize,
                Utils.resolveDimension(
                    context,
                    R.attr.actionbar_action_text_size,
                    Utils.getDimensionPixelSize(getContext(), R.dimen.default_action_text_size)
                )
            )

            mSideTextColor = typedArray.getColor(
                R.styleable.TitleBar_tb_sideTextColor,
                Utils.resolveColor(getContext(), R.attr.actionbar_text_color, DEFAULT_TEXT_COLOR)
            )
            mTitleTextColor = typedArray.getColor(
                R.styleable.TitleBar_tb_titleTextColor,
                Utils.resolveColor(getContext(), R.attr.actionbar_text_color, DEFAULT_TEXT_COLOR)
            )
            mSubTitleTextColor = typedArray.getColor(
                R.styleable.TitleBar_tb_subTitleTextColor,
                Utils.resolveColor(getContext(), R.attr.actionbar_text_color, DEFAULT_TEXT_COLOR)
            )
            mActionTextColor = typedArray.getColor(
                R.styleable.TitleBar_tb_actionTextColor,
                Utils.resolveColor(getContext(), R.attr.actionbar_text_color, DEFAULT_TEXT_COLOR)
            )

            mLeftImageResource =
                Utils.getDrawableAttrRes(getContext(), typedArray, R.styleable.TitleBar_tb_leftImageResource)
            mLeftTextString = typedArray.getString(R.styleable.TitleBar_tb_leftText)
            mTitleTextString = typedArray.getString(R.styleable.TitleBar_tb_titleText)
            mSubTextString = typedArray.getString(R.styleable.TitleBar_tb_subTitleText)
            mDividerColor = typedArray.getColor(R.styleable.TitleBar_tb_dividerColor, Color.TRANSPARENT)
            mDivideHeight =
                typedArray.getDimensionPixelSize(R.styleable.TitleBar_tb_dividerHeight, Utils.dp2px(getContext(), 1F))
            mIsUseThemeColor = typedArray.getBoolean(R.styleable.TitleBar_tb_useThemeColor, true)

            typedArray.recycle()
        }

    }

    private fun init(context: Context) {
        mScreenWidth = resources.displayMetrics.widthPixels
        if (mImmersive) {
            mStatusBarHeight = getStatusBarHeight()
        }
        initView(context)
    }

    private fun initView(context: Context) {
        mLeftText = TextView(context)
        mCenterLayout = LinearLayout(context)
        mRightLayout = LinearLayout(context)
        mDividerView = View(context)

        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)

        mLeftText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSideTextSize.toFloat())
        mLeftText.setTextColor(mSideTextColor)
        mLeftText.text = mLeftTextString
        if (mLeftImageResource != null) {
            mLeftText.setCompoundDrawablesWithIntrinsicBounds(mLeftImageResource, null, null, null)
        }
        mLeftText.setSingleLine()

        mLeftText.gravity = Gravity.CENTER_VERTICAL
        mLeftText.setPadding(mSideTextPadding, 0, mSideTextPadding, 0)

        mCenterText = TextView(context)
        mSubTitleText = TextView(context)
        if (!TextUtils.isEmpty(mSubTextString)) {
            mCenterLayout.orientation = LinearLayout.VERTICAL
        }
        mCenterText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize.toFloat())
        mCenterText.setTextColor(mTitleTextColor)
        mCenterText.text = mTitleTextString
        mCenterText.setSingleLine()
        mCenterText.ellipsize = TextUtils.TruncateAt.MARQUEE

        mSubTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSubTitleTextSize.toFloat())
        mSubTitleText.setTextColor(mSubTitleTextColor)
        mSubTitleText.text = mSubTextString
        mSubTitleText.setSingleLine()
        mSubTitleText.setPadding(0, Utils.dp2px(getContext(), 2f), 0, 0)
        mSubTitleText.ellipsize = TextUtils.TruncateAt.END

        if (mCenterGravity == CENTER_LEFT) {
            setCenterGravity(Gravity.CENTER_VERTICAL or Gravity.START)
        } else if (mCenterGravity == CENTER_RIGHT) {
            setCenterGravity(Gravity.CENTER_VERTICAL or Gravity.END)
        } else {
            setCenterGravity(Gravity.CENTER)
        }
        mCenterLayout.addView(mCenterText)
        mCenterLayout.addView(mSubTitleText)

        mRightLayout.setPadding(mSideTextPadding, 0, mSideTextPadding, 0)

        mDividerView.setBackgroundColor(mDividerColor)

        addView(mLeftText, layoutParams)
        addView(mCenterLayout)
        addView(mRightLayout, layoutParams)
        addView(mDividerView, LayoutParams(LayoutParams.MATCH_PARENT, mDivideHeight))

        if (mIsUseThemeColor) {
            val backgroundDrawable: Drawable? = Utils.resolveDrawable(getContext(), R.attr.actionbar_background)
            if (backgroundDrawable != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    background = backgroundDrawable
                } else {
                    setBackgroundDrawable(backgroundDrawable)
                }
            } else {
                setBackgroundColor(
                    Utils.resolveColor(
                        context,
                        R.attr.actionbar_color,
                        resources.getColor(R.color.default_actionbar_color)
                    )
                )
            }
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val height: Int
        if (heightMode != MeasureSpec.EXACTLY) {
            height = mBarHeight + mStatusBarHeight
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mBarHeight, MeasureSpec.EXACTLY)
        } else {
            height = MeasureSpec.getSize(heightMeasureSpec) + mStatusBarHeight
        }

        measureChild(mLeftText, widthMeasureSpec, heightMeasureSpec)
        measureChild(mRightLayout, widthMeasureSpec, heightMeasureSpec)
        if (mLeftText.measuredWidth > mRightLayout.measuredWidth) {
            mCenterLayout.measure(
                MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * mLeftText.measuredWidth, MeasureSpec.EXACTLY),
                heightMeasureSpec
            )
        } else {
            mCenterLayout.measure(
                MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * mRightLayout.measuredWidth, MeasureSpec.EXACTLY),
                heightMeasureSpec
            )
        }
        measureChild(mDividerView, widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        mLeftText.layout(0, mStatusBarHeight, mLeftText.measuredWidth, mLeftText.measuredHeight + mStatusBarHeight)
        mRightLayout.layout(
            mScreenWidth - mRightLayout.measuredWidth, mStatusBarHeight,
            mScreenWidth, mRightLayout.measuredHeight + mStatusBarHeight
        )
        if (mCenterGravity == CENTER_LEFT) {
            mCenterLayout.layout(
                mLeftText.measuredWidth, mStatusBarHeight,
                mScreenWidth - mLeftText.measuredWidth, measuredHeight
            )
        } else if (mCenterGravity == CENTER_RIGHT) {
            mCenterLayout.layout(
                mRightLayout.measuredWidth, mStatusBarHeight,
                mScreenWidth - mRightLayout.measuredWidth, measuredHeight
            )
        } else {
            if (mLeftText.measuredWidth > mRightLayout.measuredWidth) {
                mCenterLayout.layout(
                    mLeftText.measuredWidth, mStatusBarHeight,
                    mScreenWidth - mLeftText.measuredWidth, measuredHeight
                )
            } else {
                mCenterLayout.layout(
                    mRightLayout.measuredWidth, mStatusBarHeight,
                    mScreenWidth - mRightLayout.measuredWidth, measuredHeight
                )
            }
        }

        mDividerView.layout(0, measuredHeight - mDividerView.measuredHeight, measuredWidth, measuredHeight)
    }

    override fun onClick(v: View?) {
        // 处理点击事件
    }

    /**
     * 计算状态栏高度高度
     * getStatusBarHeight
     *
     * @return
     */
    fun getStatusBarHeight(): Int {
        return getInternalDimensionSize(Resources.getSystem(), STATUS_BAR_HEIGHT_RES_NAME)
    }

    private fun getInternalDimensionSize(res: Resources, key: String): Int {
        var result = 0
        val resourceId = res.getIdentifier(key, "dimen", "android")
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 设置中间内容的对齐方式
     *
     * @param gravity
     * @return
     */
    fun setCenterGravity(gravity: Int): TitleBar {
        mCenterLayout.gravity = gravity
        mCenterText.gravity = gravity
        mSubTitleText.gravity = gravity
        return this
    }

    /**
     * 设置左侧图标
     *
     * @param leftImageDrawable
     * @return
     */
    fun setLeftImageDrawable(leftImageDrawable: Drawable): TitleBar {
        mLeftImageResource = leftImageDrawable
        if (mLeftText != null) {
            mLeftText.setCompoundDrawablesWithIntrinsicBounds(mLeftImageResource, null, null, null)
        }
        return this
    }

    /**
     * 设置左侧点击事件
     *
     * @param l
     * @return
     */
    fun setLeftClickListener(l: OnClickListener?): TitleBar {
        mLeftText.setOnClickListener(l)
        return this
    }

    /**
     * 设置标题文字
     *
     * @param title
     * @return
     */
    fun setTitle(title: CharSequence): TitleBar {
        var index = title.toString().indexOf("\n")
        if (index > 0) {
            setTitle(title.subSequence(0, index), title.subSequence(index + 1, title.length), LinearLayout.VERTICAL)
        } else {
            index = title.toString().indexOf("\t")
            if (index > 0) {
                setTitle(
                    title.subSequence(0, index),
                    "  " + title.subSequence(index + 1, title.length),
                    LinearLayout.HORIZONTAL
                )
            } else {
                mCenterText.text = title
                mSubTitleText.visibility = GONE
            }
        }
        return this
    }

    /**
     * 设置标题和副标题的文字
     *
     * @param title       标题
     * @param subTitle    副标题
     * @param orientation 对齐方式
     * @return
     */
    fun setTitle(title: CharSequence?, subTitle: CharSequence?, orientation: Int): TitleBar {
        mCenterLayout.orientation = orientation
        mCenterText.text = title

        mSubTitleText.text = subTitle
        mSubTitleText.visibility = VISIBLE
        return this
    }
}