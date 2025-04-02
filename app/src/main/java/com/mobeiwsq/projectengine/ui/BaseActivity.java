package com.mobeiwsq.projectengine.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;
import com.mobeiwsq.engine_project.base.XPageActivity;
import com.mobeiwsq.engine_project.base.XPageFragment;
import com.mobeiwsq.engine_project.core.CoreSwitchBean;
import com.mobeiwsq.engine_project.core.PageOption;

import java.io.Serializable;

public class BaseActivity<Binding extends ViewBinding> extends XPageActivity {
    /**
     * ViewBinding
     */
    protected Binding mBinding;

    @Override
    protected View getCustomRootView() {
        mBinding = viewBindingInflate(getLayoutInflater());
        return mBinding != null ? mBinding.getRoot() : null;
    }

    /**
     * 构建ViewBinding
     *
     * @param inflater inflater
     * @return ViewBinding
     */
    @Nullable
    protected Binding viewBindingInflate(LayoutInflater inflater) {
        return null;
    }
    /**
     * 打开fragment
     *
     * @param clazz          页面类
     * @param addToBackStack 是否添加到栈中
     * @return 打开的fragment对象
     */
    public <T extends XPageFragment> T openPage(Class<T> clazz, boolean addToBackStack) {
        CoreSwitchBean page = new CoreSwitchBean(clazz)
                .setAddToBackStack(addToBackStack);
        return (T) openPage(page);
    }

    /**
     * 打开fragment
     *
     * @return 打开的fragment对象
     */
    public <T extends XPageFragment> T openNewPage(Class<T> clazz) {
        CoreSwitchBean page = new CoreSwitchBean(clazz)
                .setNewActivity(true);
        return (T) openPage(page);
    }

    /**
     * 打开一个新的页面【建议只在主tab页使用】
     *
     * @param clazz 页面的类
     * @param key   入参的键
     * @param value 入参的值
     * @param <T>
     * @return
     */
    public <T extends XPageFragment> Fragment openNewPage(Class<T> clazz, String key, String value) {
        CoreSwitchBean page = new CoreSwitchBean(clazz)
                .setNewActivity(true);
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        page.setBundle(bundle);
        return (T) openPage(page);
    }

    public <T extends XPageFragment> Fragment openNewPage(Class<T> clazz, String key, int value) {
        CoreSwitchBean page = new CoreSwitchBean(clazz)
                .setNewActivity(true);
        Bundle bundle = new Bundle();
        bundle.putInt(key, value);
        page.setBundle(bundle);
        return (T) openPage(page);
    }

    /**
     * 切换fragment
     *
     * @param clazz 页面类
     * @return 打开的fragment对象
     */
    public <T extends XPageFragment> T switchPage(Class<T> clazz) {
        return openPage(clazz, false);
    }

    /**
     * 序列化对象
     *
     * @param object
     * @return
     */
//    public String serializeObject(Object object) {
//        return XRouter.getInstance().navigation(SerializationService.class).object2Json(object);
//    }

    public Fragment openPage(PageOption option, String key, Object value) {
        if (value instanceof Integer) {
            option.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            option.putFloat(key, (Float) value);
        } else if (value instanceof String) {
            option.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            option.putBoolean(key, (Boolean) value);
        } else if (value instanceof Long) {
            option.putLong(key, (Long) value);
        } else if (value instanceof Double) {
            option.putDouble(key, (Double) value);
        } else if (value instanceof Parcelable) {
            option.putParcelable(key, (Parcelable) value);
        } else if (value instanceof Serializable) {
            option.putSerializable(key, (Serializable) value);
        } else {
//            option.putString(key, serializeObject(value));
        }
        return option.open(this);
    }

}
