package com.mobeiwsq.enginelibrary.logger

import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter

/**
 * @ClassName : Logger
 * @Description : log工具类
 * @Author : mobeiwsq
 * @Date: 2025/3/25  10:00
 */
class Logger :ILogger{

    // 日志最大长度
    private val MAX_LOG_LENGTH = 4000


    override fun log(priority: Int,tag: String, message: String?, t: Throwable?) {
        var newMessage =message
        if (message != null && message.isEmpty()){
            newMessage =null
        }

        if (newMessage == null){
            if(t == null){
              return
            }
            newMessage = getStackTraceString(t)
        }  else {
            if (t!=null){
                newMessage += "\n" +  getStackTraceString(t)
            }
        }
        log(priority,tag,newMessage)
    }

    private fun getStackTraceString(t:Throwable):String{
        val sw = StringWriter(256)
        val pw = PrintWriter(sw ,false)
        t.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }

    /**
     * 使用LogCat输出日志，字符长度超过4000则自动换行.
     *
     * @param priority 优先级
     * @param tag      标签
     * @param message  信息
     */
    private fun log( priority:Int,tag:String,message:String){
        val subNum = message.length/MAX_LOG_LENGTH
        if (subNum>0){
            var index = 0
            for (i in 0 until subNum) {
                val lastIndex = index + MAX_LOG_LENGTH
                val sub = message.substring(index,lastIndex)
                logSub(priority,tag,sub)
                index = lastIndex
            }
        } else {
            logSub(priority,tag,message)
        }
    }


    /**
     * 使用LogCat输出日志.
     *
     * @param priority 优先级
     * @param tag      标签
     * @param sub      信息
     */
    private fun logSub( priority:Int,tag:String,sub:String){
        when (priority){
            Log.VERBOSE ->{
                Log.v(tag, sub)
            }

            Log.DEBUG ->{
                Log.d(tag, sub)
            }

            Log.INFO ->{
                Log.i(tag, sub)
            }

            Log.WARN ->{
                Log.w(tag, sub)
            }

            Log.ERROR ->{
                Log.e(tag, sub)
            }

            Log.ASSERT ->{
                Log.w(tag, sub)
            }
            else ->{
                Log.v(tag, sub)
            }
        }
    }
}