package com.mobeiwsq.compiler;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.HashMap;
import java.util.Map;

public class AnnotationInfo {
    public static final String TAG_NAME = "_ViewBinding";
    private TypeElement typeElement;
    private String packageName;
    public Map<Integer, Element> bindViewElements = new HashMap<>();
    public Map<String,Element> onClickElements = new HashMap<>();

    public AnnotationInfo(TypeElement typeElement, String packageName){
        this.typeElement = typeElement;
        this.packageName=packageName;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public String getPackageName() {
        return packageName;
    }
}
