package com.mobeiwsq.engine_project.file

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mobeiwsq.engine_project.R
import java.io.File

/**
 * 文件选择adapter
 *
 * @author : mobeiwsq
 * @since :  2025/7/31 15:24
 */

class FileSelectAdapter(
    private val fileList: List<FileSelectData>,
    private val onItemClick: (FileSelectData) -> Unit
) : RecyclerView.Adapter<FileSelectAdapter.ViewHolder>() {

    // 图片文件扩展名集合
    private val imageExtensions = setOf("jpg", "jpeg", "png", "gif", "bmp", "webp")

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivIcon: ImageView = itemView.findViewById(R.id.iv_icon)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvSize: TextView = itemView.findViewById(R.id.tv_size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_file_select, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return fileList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fileItem = fileList[position]

        // 根据文件类型处理图标显示
        if (fileItem.isDirectory) {
            // 目录显示文件夹图标
            holder.ivIcon.setImageResource(R.mipmap.icon_folder)
        } else {
            val fileType = fileItem.fileType.lowercase()
            if (imageExtensions.contains(fileType)) {
                // 图片文件显示实际缩略图
                loadImageThumbnail(holder.itemView.context, fileItem.filePath, holder.ivIcon)
            } else {
                // 非图片文件显示对应类型图标
                holder.ivIcon.setImageResource(getFileIconRes(fileType))
            }
        }

        // 设置文件名
        holder.tvName.text = fileItem.fileName

        // 设置文件大小（仅文件显示）
        holder.tvSize.text = fileItem.fileSize

        // 设置点击事件
        holder.itemView.setOnClickListener {
            onItemClick(fileItem)
        }
    }

    /**
     * 根据文件扩展名获取对应图标资源
     */
    private fun getFileIconRes(fileType: String): Int {
        return when (fileType) {
            "doc", "docx" -> R.mipmap.icon_doc
            "pdf" -> R.mipmap.icon_pdf
            "txt" -> R.mipmap.icon_txt
            "xls", "xlsx" -> R.mipmap.icon_xlsx
            "ppt", "pptx" -> R.mipmap.icon_ppt
            else -> R.mipmap.icon_other // 默认文件图标
        }
    }

    /**
     * 加载图片文件缩略图
     * 使用Glide库高效加载本地图片缩略图，避免OOM
     */
    private fun loadImageThumbnail(context: Context, filePath: String, imageView: ImageView) {
        val file = File(filePath)
        if (file.exists() && file.isFile) {
            Glide.with(context)
                .load(file)
                .thumbnail(0.1f) // 加载缩略图（原图的1/10尺寸）
                .diskCacheStrategy(DiskCacheStrategy.ALL) // 缓存缩略图
                .error(R.mipmap.icon_jpg) // 加载失败时显示的错误图标
                .into(imageView)
        } else {
            // 文件不存在时显示默认图片图标
            imageView.setImageResource(R.mipmap.icon_jpg)
        }
    }
}