package com.mobeiwsq.engine_project.file

import android.content.Intent
import android.os.Environment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobeiwsq.engine_project.file.FileSelectManager.FILE_SELECT_REQUEST_CODE
import com.mobeiwsq.engine_project.R
import com.mobeiwsq.engine_project.databinding.ActivityFileSelectBinding
import com.mobeiwsq.engine_project.easy.view.EasyToolbarActivity
import com.mobeiwsq.engine_project.view.toast.toast
import java.io.File
import java.util.*


class FileSelectActivity : EasyToolbarActivity<ActivityFileSelectBinding>(R.layout.activity_file_select) {
    companion object {
        const val TAG = "FileSelectActivity_TAG"
        const val EXTRA_ALLOWED_FILE_TYPES = "allowed_file_types" // 文件类型参数键
    }

    private lateinit var currentPath: String
    private lateinit var fileAdapter: FileSelectAdapter
    private val fileList = mutableListOf<FileSelectData>()
    private var allowedFileTypes: List<String>? = null // 允许的文件类型列表


    override fun initView() {
        title = "文件选择"
        // 初始化RecyclerView
        initRecyclerView()
    }

    override fun initData() {
        // 获取外部传入的允许文件类型列表
        allowedFileTypes = intent.getStringArrayListExtra(EXTRA_ALLOWED_FILE_TYPES)

        // 加载根目录文件
        val rootPath = Environment.getExternalStorageDirectory().absolutePath
        loadFiles(rootPath)
    }

    override fun initListeners() {
    }

    // 初始化RecyclerView
    private fun initRecyclerView() {
        fileAdapter = FileSelectAdapter(fileList) { fileItem ->
            if (fileItem.isDirectory) {
                // 如果是目录，进入该目录
                loadFiles(fileItem.filePath)
            } else {
                // 如果是文件，返回该文件路径
                val intent = Intent()
                intent.putExtra("file_path", fileItem.filePath)
                FileSelectManager.onActivityResult(FILE_SELECT_REQUEST_CODE, RESULT_OK, intent)
                finish()
            }
        }

        binding.rvFiles.apply {
            layoutManager = LinearLayoutManager(this@FileSelectActivity)
            adapter = fileAdapter
        }
    }

    // 加载指定路径的文件
    private fun loadFiles(path: String) {
        currentPath = path

        val file = File(path)
        val files = file.listFiles() ?: run {
            toast("无法访问该目录")
            return
        }

        fileList.clear()

        val filteredFiles = files.filter { file ->
            file.isDirectory || // 目录始终保留
                    (allowedFileTypes?.let { types ->
                        types.any { ext ->
                            getFileExtension(file.name).equals(ext, ignoreCase = true)
                        }
                    } ?: true) // 若未指定类型，则保留所有文件
        }

        // 优化点2：排序逻辑（目录优先，按名称排序）
        val sortedFiles = filteredFiles.sortedWith(
            compareBy(
                { !it.isDirectory },
                { it.name.lowercase(Locale.getDefault()) }
            )
        )

        // 转换为FileItem并添加到列表
        sortedFiles.forEach {
            val fileType = getFileExtension(it.name)
            fileList.add(
                FileSelectData(
                    fileName = it.name ?: "",
                    filePath = it.absolutePath,
                    isDirectory = it.isDirectory,
                    fileSize = if (it.isFile) formatFileSize(it.length()) else "",
                    fileType = fileType
                )
            )
        }

        // 添加"返回上一级"选项（如果不是根目录）
        if (path != Environment.getExternalStorageDirectory().absolutePath) {
            fileList.add(
                0, FileSelectData(
                    fileName = "..",
                    filePath = File(path).parent ?: "",
                    isDirectory = true,
                    fileSize = "",
                    fileType = ""
                )
            )
        }

        fileAdapter.notifyDataSetChanged()
    }

    // 格式化文件大小
    private fun formatFileSize(size: Long): String {
        return when {
            size < 1024 -> "$size B"
            size < 1024 * 1024 -> String.format("%.1f KB", size / 1024f)
            size < 1024 * 1024 * 1024 -> String.format("%.1f MB", size / (1024f * 1024f))
            else -> String.format("%.1f GB", size / (1024f * 1024f * 1024f))
        }
    }

    /**
     * 从文件名中提取扩展名
     */
    private fun getFileExtension(fileName: String?): String {
        if (fileName.isNullOrEmpty()) return ""
        val lastDotIndex = fileName.lastIndexOf('.')
        return if (lastDotIndex > 0 && lastDotIndex < fileName.length - 1) {
            fileName.substring(lastDotIndex + 1)
        } else {
            ""  // 没有扩展名或扩展名在开头
        }
    }

    private fun handleBackAction() {
        // 统一处理返回逻辑
        if (currentPath != Environment.getExternalStorageDirectory().absolutePath) {
            // 返回上一级目录
            loadFiles(File(currentPath).parent ?: Environment.getExternalStorageDirectory().absolutePath)
        } else {
            // 已是根目录则关闭Activity
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        handleBackAction() // 复用相同的返回逻辑
    }

}