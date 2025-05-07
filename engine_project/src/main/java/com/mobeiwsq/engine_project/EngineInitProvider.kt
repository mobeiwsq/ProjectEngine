package com.mobeiwsq.engine_project

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.mobeiwsq.engine_project.utils.Utils

/**
* 自动初始化
*
* @author : mobeiwsq
* @since :  2025/5/7 14:47
*/
class EngineInitProvider:ContentProvider() {
    override fun onCreate(): Boolean {
        var application =Utils.cast(context,Application::class.java)
        if (application!=null){
            application =Utils.getApplicationByReflect()
        }
        EngineConfig.init(application)
        return true
    }

    override fun query(p0: Uri, p1: Array<out String>?, p2: String?, p3: Array<out String>?, p4: String?): Cursor? {
        return null
    }

    override fun getType(p0: Uri): String? {
        return null
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        return null
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        return 0
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        return 0
    }
}