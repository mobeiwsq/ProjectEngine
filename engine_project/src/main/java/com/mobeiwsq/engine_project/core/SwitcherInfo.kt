package com.mobeiwsq.engine_project.core

import com.google.gson.annotations.SerializedName

/**
* 页面切换所需实体类
*
* @author : mobeiwsq
* @since :  2025/4/9 10:00
*/

const val KEY_PAGE_NAME = "name"
const val KEY_PAGE_CLAZZ = "classPath"
const val KEY_PAGE_PARAMS = "params"

data class SwitcherInfo(
    /**
     * 页面名
     */
    @SerializedName(KEY_PAGE_NAME)
    var mName: String,
    /**
     * 页面class
     */
    @SerializedName(KEY_PAGE_CLAZZ)
    var mClazz: String,
    /**
     * 页面参数
     */
    @SerializedName(KEY_PAGE_PARAMS)
    var mParams: String
)
