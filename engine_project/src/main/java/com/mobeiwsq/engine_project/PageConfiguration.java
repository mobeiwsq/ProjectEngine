package com.mobeiwsq.engine_project;

import android.content.Context;
import com.mobeiwsq.annotation.model.PageInfo;

import java.util.List;

/**
 * 页面配置接口
 *
 * @author : mobeiwsq
 * @since :  2025/4/1 17:52
 */

public interface PageConfiguration<T> {
    /**
     * 注册页面
     *
     * @param context 上下文
     * @return 注册的页面集合
     */
    List<PageInfo> registerPages(Context context);
}
