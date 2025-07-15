package com.mobeiwsq.engine_project.utils.fileselect;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;

public class FileUtils {
    /**
     * 文件选择器
     */
    public static void fileSelect(Activity activity, View view, Window window, LayoutInflater layoutInflater,
                                  ArrayList<String> types, ChooseFile.onChooseFileBack callback) {
        if (view == null) {
            return;
        }
        ChooseFile chooseFile = new ChooseFile();
        chooseFile.setOnChooseFileBack(callback);
        chooseFile.setAllowedFileTypes(types);
        chooseFile.popupChoose(activity, view, window, layoutInflater, true);

    }
}
