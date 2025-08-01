package com.mobeiwsq.engine_project.file

/**
 * 文件选择实体类
 *
 * @author : mobeiwsq
 * @since :  2025/7/31 15:25
 */

data class FileSelectData(
    // 文件名称
    val fileName: String,
    // 文件路径
    val filePath: String,
    // 是否是文件夹
    val isDirectory: Boolean,
    // 文件大小
    val fileSize: String,

    // 文件类型
    val fileType: String,
)
