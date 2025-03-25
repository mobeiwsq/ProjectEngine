package com.mobeiwsq.enginelibrary.logger

/**
 * @ClassName : ILogger
 * @Description : 日志接口
 * @Author : mobeiwsq
 * @Date: 2025/3/25  10:04
 */
interface ILogger {
    fun log(priority:Int,tag: String,message:String?,t:Throwable?)
}