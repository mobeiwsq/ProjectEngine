package com.mobeiwsq.projectengine.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import com.mobeiwsq.engine_project.view.dialog.materialdialog.MaterialDialog;
import com.mobeiwsq.projectengine.R;
import com.mobeiwsq.projectengine.databinding.DialogAddInspectionBinding;

import static com.mobeiwsq.engine_project.CommonUtilsKt.showCustomDialog;
import static com.mobeiwsq.projectengine.utils.SimpleUtilsKt.getDisplayMetrics;

/**
 * 新增巡查前置dialog
 *
 * @author : swn
 * @since :  2025/6/5 15:34
 */
public class AddInspectionDialog {

    private volatile static AddInspectionDialog instance = null;
    private static Context mContext;
    private DialogAddInspectionBinding binding; // 替换原 customView
    private MaterialDialog showDialog;
    private ClickInterListener mClickInterListener;

    public AddInspectionDialog setClickInterListener(ClickInterListener clickInterListener) {
        mClickInterListener = clickInterListener;
        return instance; // 修正单例返回
    }

    public interface ClickInterListener {
        void projectClick();

        void programClick();

        void typeClick();

        void submit();

        void cancel();
    }

    private AddInspectionDialog() {

    }

    public static AddInspectionDialog getInstance(Context context) {
        mContext = context; // 使用 Application Context 避免内存泄漏
        if (instance == null) {
            synchronized (AddInspectionDialog.class) {
                if (instance == null) {
                    instance = new AddInspectionDialog();
                }
            }
        }
        return instance;
    }

    public void createDialog() {
        // 1. 通过 DataBinding  inflate 布局
        binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.dialog_add_inspection, null, false);


        // 4. 创建 Dialog 并设置内容为 Binding 的根视图
        showDialog = showCustomDialog(mContext, binding.getRoot(), "保存", "取消",
                (dialog, which) -> {
                    if (mClickInterListener != null) {
                        mClickInterListener.submit();
                    }
                },
                (dialog, which) -> {
                    if (mClickInterListener != null) {
                        mClickInterListener.cancel();
                    }
                });

        // 项目名称点击事件
        binding.projectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mClickInterListener) {
                    mClickInterListener.programClick();
                }
            }
        });

        // 项目标段点击事件
        binding.programName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mClickInterListener) {
                    mClickInterListener.projectClick();
                }
            }
        });

        // 巡查类型点击事件
        binding.inspectionType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mClickInterListener) {
                    mClickInterListener.typeClick();
                }
            }
        });

        // 5. 配置 Dialog 尺寸（保留原逻辑）
        showDialog.getWindow().getAttributes().width = (int) (getDisplayMetrics(mContext).widthPixels * 0.83);
        showDialog.setCancelable(false);
        showDialog.setCanceledOnTouchOutside(false);
        showDialog.show();
    }

}
