
package com.mobeiwsq.engine_project.view.imageview.preview.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.mobeiwsq.engine_project.R;
import com.mobeiwsq.engine_project.utils.CollectionUtils;
import com.mobeiwsq.engine_project.view.imageview.preview.MediaLoader;
import com.mobeiwsq.engine_project.view.imageview.preview.PreviewBuilder;
import com.mobeiwsq.engine_project.view.imageview.preview.enitity.IPreviewInfo;
import com.mobeiwsq.engine_project.view.imageview.preview.view.BezierBannerView;
import com.mobeiwsq.engine_project.view.imageview.preview.view.PhotoViewPager;
import com.mobeiwsq.engine_project.view.imageview.preview.view.SmoothImageView;

import java.util.ArrayList;
import java.util.List;

import static com.mobeiwsq.engine_project.view.imageview.preview.ui.BasePhotoFragment.*;


/**
 * 图片预览页面
 *
 */
public class PreviewActivity extends AppCompatActivity {
    public static final String KEY_IMAGE_PATHS = "com.mobeiwsq.engine.widget.preview.KEY_IMAGE_PATHS";
    public static final String KEY_POSITION = "com.mobeiwsq.engine.widget.preview.KEY_POSITION";
    public static final String KEY_TYPE = "com.mobeiwsq.engine.widget.preview.KEY_TYPE";
    public static final String KEY_IS_SHOW = "com.mobeiwsq.engine.widget.preview.KEY_IS_SHOW";
    public static final String KEY_DURATION = "com.mobeiwsq.engine.widget.preview.KEY_DURATION";
    public static final String KEY_IS_FULLSCREEN = "com.mobeiwsq.engine.widget.preview.KEY_IS_FULLSCREEN";
    public static final String KEY_CLASSNAME = "com.mobeiwsq.engine.widget.preview.KEY_CLASSNAME";

    private boolean mIsTransformOut = false;
    /*** 图片的地址***/
    private List<IPreviewInfo> mImgUrls;
    /*** 当前图片的位置 ***/
    private int mCurrentIndex;
    /*** 图片的展示的Fragment***/
    private List<BasePhotoFragment> fragments = new ArrayList<>();
    /*** 展示图片的viewPager ***/
    private PhotoViewPager mViewPager;
    /*** 显示图片数**/
    private TextView mTvIndex;
    /***指示器控件**/
    private BezierBannerView mBezierBannerView;
    /***指示器类型枚举***/
    private PreviewBuilder.IndicatorType mType;
    /***默认显示***/
    private boolean mIsShow = true;

    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initArgs();
        if (getLayoutId() == 0) {
            setContentView(R.layout.preview_activity_image_photo);
        } else {
            setContentView(getLayoutId());
        }
        initView();
    }

    @Override
    protected void onDestroy() {
        MediaLoader.get().clearMemory(this);
        if (mViewPager != null) {
            mViewPager.setAdapter(null);
            mViewPager.clearOnPageChangeListeners();
            mViewPager.removeAllViews();
            mViewPager = null;
        }
        if (fragments != null) {
            fragments.clear();
            fragments = null;
        }
        if (mImgUrls != null) {
            mImgUrls.clear();
            mImgUrls = null;
        }
        super.onDestroy();
    }

    /**
     * 初始化参数
     */
    private void initArgs() {
        mImgUrls = getIntent().getParcelableArrayListExtra(KEY_IMAGE_PATHS);
        mCurrentIndex = getIntent().getIntExtra(KEY_POSITION, -1);
        mType = (PreviewBuilder.IndicatorType) getIntent().getSerializableExtra(KEY_TYPE);
        mIsShow = getIntent().getBooleanExtra(KEY_IS_SHOW, true);
        int duration = getIntent().getIntExtra(KEY_DURATION, 300);
        boolean isFullscreen = getIntent().getBooleanExtra(KEY_IS_FULLSCREEN, false);
        if (isFullscreen) {
            setTheme(android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        }
        try {
            SmoothImageView.setDuration(duration);
            Class<? extends BasePhotoFragment> clazz;
            clazz = (Class<? extends BasePhotoFragment>) getIntent().getSerializableExtra(KEY_CLASSNAME);
            initFragment(mImgUrls, mCurrentIndex, clazz);
        } catch (Exception e) {
            initFragment(mImgUrls, mCurrentIndex, BasePhotoFragment.class);
        }

    }

    /**
     * 初始化
     *
     * @param imgUrls      集合
     * @param currentIndex 选中索引
     * @param className    显示Fragment
     **/
    protected void initFragment(List<IPreviewInfo> imgUrls, int currentIndex, Class<? extends BasePhotoFragment> className) {
        if (imgUrls != null) {
            int size = imgUrls.size();
            for (int i = 0; i < size; i++) {
                fragments.add(BasePhotoFragment.
                        newInstance(className, imgUrls.get(i),
                                currentIndex == i,
                                getIntent().getBooleanExtra(KEY_SING_FILING, false),
                                getIntent().getBooleanExtra(KEY_DRAG, false),
                                getIntent().getFloatExtra(KEY_SENSITIVITY, 0.5f),
                                getIntent().getIntExtra(KEY_PROGRESS_COLOR, R.color.config_color_main_theme))
                );
            }
        } else {
            finish();
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mViewPager = findViewById(R.id.viewPager);
        //viewPager的适配器
        PhotoPagerAdapter adapter = new PhotoPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(mCurrentIndex);
        mViewPager.setOffscreenPageLimit(3);
        mBezierBannerView = findViewById(R.id.bezierBannerView);
        mTvIndex = findViewById(R.id.tv_index);
        if (mType == PreviewBuilder.IndicatorType.Dot) {
            mBezierBannerView.setVisibility(View.VISIBLE);
            mBezierBannerView.attachToViewpager(mViewPager);
        } else {
            mTvIndex.setVisibility(View.VISIBLE);
            mTvIndex.setText(getString(R.string.preview_count_string, (mCurrentIndex + 1), getImgSize()));
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    //当被选中的时候设置小圆点和当前位置
                    if (mTvIndex != null) {
                        mTvIndex.setText(getString(R.string.preview_count_string, (position + 1), getImgSize()));
                    }
                    mCurrentIndex = position;
                    mViewPager.setCurrentItem(mCurrentIndex, true);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
        if (fragments.size() == 1) {
            if (!mIsShow) {
                mBezierBannerView.setVisibility(View.GONE);
                mTvIndex.setVisibility(View.GONE);
            }
        }
        mViewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mViewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                BasePhotoFragment fragment = CollectionUtils.getListItem(fragments, mCurrentIndex);
                if (fragment != null) {
                    fragment.transformIn();
                }
            }
        });
    }

    private int getImgSize() {
        return CollectionUtils.getSize(mImgUrls);
    }

    /***退出预览的动画***/
    public void transformOut() {
        if (mIsTransformOut) {
            return;
        }
        mIsTransformOut = true;
        int currentItem = mViewPager.getCurrentItem();
        if (currentItem < getImgSize()) {
            BasePhotoFragment fragment = fragments.get(currentItem);
            if (mTvIndex != null) {
                mTvIndex.setVisibility(View.GONE);
            } else {
                mBezierBannerView.setVisibility(View.GONE);
            }
            fragment.changeBg(Color.TRANSPARENT);
            fragment.transformOut(new SmoothImageView.onTransformListener() {
                @Override
                public void onTransformCompleted(SmoothImageView.Status status) {
                    exit();
                }
            });
        } else {
            exit();
        }
    }

    @Override
    public void finish() {
        BasePhotoFragment.listener = null;
        super.finish();
    }

    /***
     * 得到PhotoFragment集合
     * @return List
     * **/
    public List<BasePhotoFragment> getFragments() {
        return fragments;
    }

    /**
     * 关闭页面
     */
    private void exit() {
        finish();
        overridePendingTransition(0, 0);
    }

    /***
     * 得到PhotoViewPager
     * @return PhotoViewPager
     * **/
    public PhotoViewPager getViewPager() {
        return mViewPager;
    }

    /***
     * 自定义布局内容
     * @return int
     ***/
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void onBackPressed() {
        transformOut();
    }

    /**
     * pager的适配器
     */
    private class PhotoPagerAdapter extends FragmentPagerAdapter {

        PhotoPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments == null ? 0 : fragments.size();
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            super.destroyItem(container, position, object);
        }
    }


}
