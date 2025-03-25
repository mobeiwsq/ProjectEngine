package com.mobeiwsq.enginelibrary.logger

import android.text.TextUtils
import android.util.Log

/**
 * @ClassName : PageLogger
 * @Description : 页面跳转日志工具
 * @Author : mobeiwsq
 * @Date: 2025/3/25 11:28
 */
open class PageLogger {
        // log默认tag
    private val  DEFAULT_LOGGER_TAG = "Page_Logger"

    // 日志优先级最大，什么log都不打印
    private val MAX_LOGGER_PRIORITY = 10

    // 日志优先级最小，什么log都打印
    private val MIN_LOGGER_PRIORITY = 10

    // 默认的日志记录为Logger
    private var sILogger:ILogger = Logger()

    private var sTag = DEFAULT_LOGGER_TAG

    // 是否是调试模式
    private var sIsDebug = false

    // 日志打印优先级
    private var sLogPriority = MAX_LOGGER_PRIORITY

    /**
     * 设置日志记录者的接口
     *
     * @param iLogger
     */
    open fun setLogger(iLogger:ILogger){
        sILogger = iLogger
    }

    /**
     * 设置日志的tag
     *
     * @param tag
     */
    open fun setTag(tag:String){
        sTag = tag
    }

    /**
     * 设置是否是调试模式
     *
     * @param isDebug
     */
    open fun setDebug(isDebug:Boolean){
        sIsDebug = isDebug
    }

    /**
     * 设置打印日志的等级（只打印改等级以上的日志）
     *
     * @param priority
     */
    open fun setPriority(priority:Int){
        sLogPriority = priority
    }

    /**
     * 设置是否打开调试
     *
     * @param isDebug 是否打开调试
     */
    open fun debug(isDebug: Boolean){
        if (isDebug){
            debug(DEFAULT_LOGGER_TAG)
        } else {
            debug("")
        }
    }


    /**
     * 设置调试模式
     *
     * @param tag 日志标记
     */
    open fun debug(tag:String){
        if (!TextUtils.isEmpty(tag)){
            setDebug(true)
            setPriority(MIN_LOGGER_PRIORITY)
            setTag(tag)
        } else {
            setDebug(false)
            setPriority(MAX_LOGGER_PRIORITY)
            setTag("")
        }
    }

    /**
     * 打印任何（所有）信息
     *
     * @param msg
     */
    open fun v(msg:String){
        if (enableLog(Log.VERBOSE)){
            sILogger.log(Log.VERBOSE,sTag,msg,null)
        }
    }

    /**
     * 打印任何（所有）信息
     *
     * @param tag
     * @param msg
     */
    open fun vTag(tag: String,msg: String){
        if (enableLog(Log.VERBOSE)){
            sILogger.log(Log.VERBOSE,sTag,tag,null)
        }
    }


    /**
     * 打印调试信息
     *
     * @param msg
     */
    open fun d(msg:String){
        if (enableLog(Log.DEBUG)){
            sILogger.log(Log.DEBUG,sTag,msg,null)
        }
    }

    /**
     * 打印调试信息
     *
     * @param tag
     * @param msg
     */
    open fun dTag(tag: String,msg: String){
        if (enableLog(Log.DEBUG)){
            sILogger.log(Log.DEBUG,sTag,tag,null)
        }
    }


    /**
     * 打印提示信息
     *
     * @param msg
     */
    open fun i(msg:String){
        if (enableLog(Log.INFO)){
            sILogger.log(Log.INFO,sTag,msg,null)
        }
    }

    /**
     * 打印提示信息
     *
     * @param tag
     * @param msg
     */
    open fun iTag(tag: String,msg: String){
        if (enableLog(Log.INFO)){
            sILogger.log(Log.INFO,sTag,tag,null)
        }
    }

    /**
     * 打印警告信息
     *
     * @param msg
     */
    open fun w(msg:String){
        if (enableLog(Log.WARN)){
            sILogger.log(Log.WARN,sTag,msg,null)
        }
    }

    /**
     * 打印警告信息
     *
     * @param tag
     * @param msg
     */
    open fun wTag(tag: String,msg: String){
        if (enableLog(Log.WARN)){
            sILogger.log(Log.WARN,sTag,tag,null)
        }
    }

    /**
     * 打印报错信息
     *
     * @param msg
     */
    open fun e(msg:String){
        if (enableLog(Log.ERROR)){
            sILogger.log(Log.ERROR,sTag,msg,null)
        }
    }

    /**
     * 打印报错信息
     *
     * @param tag
     * @param msg
     */
    open fun eTag(tag: String,msg: String){
        if (enableLog(Log.ERROR)){
            sILogger.log(Log.ERROR,sTag,tag,null)
        }
    }

    /**
     * 打印出错堆栈信息
     *
     * @param msg
     */
    open fun e(t:Throwable){
        if (enableLog(Log.ERROR)){
            sILogger.log(Log.ERROR,sTag,null,t)
        }
    }

    /**
     * 打印出错堆栈信息
     *
     * @param tag
     * @param msg
     */
    open fun eTag(tag: String,t:Throwable){
        if (enableLog(Log.ERROR)){
            sILogger.log(Log.ERROR,sTag,null,t)
        }
    }

    /**
     * 打印出错堆栈信息
     *
     * @param tag
     * @param msg
     * @param t
     */
    open fun eTag(tag: String,msg:String,t:Throwable){
        if (enableLog(Log.ERROR)){
            sILogger.log(Log.ERROR,sTag,msg,t)
        }
    }

    /**
     * 打印出错堆栈信息
     *
     * @param msg
     */
    open fun wtf(msg:String){
        if (enableLog(Log.ASSERT)){
            sILogger.log(Log.ASSERT,sTag,msg,null)
        }
    }

    /**
     * 打印出错堆栈信息
     *
     * @param tag
     * @param msg
     */
    open fun wtfTag(tag: String,msg:String){
        if (enableLog(Log.ASSERT)){
            sILogger.log(Log.ASSERT,tag,msg,null)
        }
    }


    /**
     * 能否打印
     *
     * @param logPriority
     * @return
     */
    private fun enableLog(logPriority:Int):Boolean{
        return sIsDebug && logPriority>=sLogPriority
    }

    open fun debuggable():Boolean{
        return sIsDebug
    }
}
