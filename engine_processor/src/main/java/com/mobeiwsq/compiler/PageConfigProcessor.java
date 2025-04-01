package com.mobeiwsq.compiler;


import com.google.auto.service.AutoService;
import com.mobeiwsq.annotation.BindView;
import com.mobeiwsq.annotation.OnClick;
import com.squareup.javapoet.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


/**
 * 页面配置自动生成器
 * @author xuexiang
 */
@AutoService(Processor.class)
public class PageConfigProcessor extends AbstractProcessor {

    private Filer filer;//文件生成
    private Messager messager;//日志
    private Elements elementUtil;//工具类
    //全类名：生成复制类所需信息
    private Map<String, AnnotationInfo> annotationInfoMap = new HashMap<>();

    private void log(String msg) {
        System.out.println(msg);
    }

    /**
     * 初始化操作
     * @param processingEnv
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        elementUtil = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
    }

    /**
     * 设置版本
     *
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 返回支持的注解类型，set集合
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(BindView.class.getCanonicalName());
        set.add(OnClick.class.getCanonicalName());
        return set;
    }

    /**
     * 主要的注解解析
     * @param annotations 注解集合
     * @param roundEnv
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //获取所有被MBindView注解标记的元素
        Set<? extends Element> fieldElements = roundEnv.getElementsAnnotatedWith(BindView.class);
        Set<? extends Element> methodElements = roundEnv.getElementsAnnotatedWith(OnClick.class);

        if (fieldElements.size() < 1 && methodElements.size() < 1) {
            return true;
        }

        for (Element element : methodElements) {
            //获取当前元素父元素，即当前类
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();

            //获取全类名:com.home.test.MainActivity
            String className = typeElement.getQualifiedName().toString();
            String packageName = elementUtil.getPackageOf(typeElement).toString();

            //以类为一个对象，保存所需元素信息
            AnnotationInfo annotationInfo = annotationInfoMap.get(className);
            if (annotationInfo == null) {
                AnnotationInfo temp = new AnnotationInfo(typeElement, packageName);
                //以方法名作为key
                temp.onClickElements.put(element.getSimpleName().toString(), element);
                annotationInfoMap.put(className, temp);
            } else {
                annotationInfo.onClickElements.put(element.getSimpleName().toString(), element);
            }
        }

        //=====================================BindView 相关==========================
        for (Element element : fieldElements) {
            if (element.getKind() == ElementKind.FIELD) {
                //获取当前元素父元素，即当前类
                TypeElement typeElement = (TypeElement) element.getEnclosingElement();

                //获取全类名:com.home.test.MainActivity
                String className = typeElement.getQualifiedName().toString();
                String packageName = elementUtil.getPackageOf(typeElement).toString();

                //以类为一个对象，保存所需元素信息
                AnnotationInfo annotationInfo = annotationInfoMap.get(className);
                BindView annotation = element.getAnnotation(BindView.class);
                if (annotationInfo == null) {
                    AnnotationInfo temp = new AnnotationInfo(typeElement, packageName);
                    temp.bindViewElements.put(annotation.value(), element);
                    annotationInfoMap.put(className, temp);
                } else {
                    annotationInfo.bindViewElements.put(annotation.value(), element);
                }
            }
        }


        for (String classNameKey : annotationInfoMap.keySet()) {
            AnnotationInfo info = annotationInfoMap.get(classNameKey);
            String packageName = info.getPackageName();

            //要生成.java文件的类名：com.home.test.MainActivity_Binding
            ClassName classBindName = ClassName.get(packageName, info.getTypeElement().getSimpleName() + AnnotationInfo.TAG_NAME);
            //生成类要实现的接口:com.home.bind_core.IViewInjector
            ClassName classInterface = ClassName.get("com.mobeiwsq.engine_project", "IViewInjector");
            //className:com.home.test.MainActivity
            ClassName className = ClassName.get(info.getTypeElement());
            //泛型接口，implements IViewInjector<MainActivity>
            ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(classInterface, className);

            //生成接口的实现方法
            MethodSpec.Builder implMethod = MethodSpec.methodBuilder("initView")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addParameter(className, "activity")
                    .addParameter(Object.class, "source");

            for (int id : info.bindViewElements.keySet()) {
                //VariableElement：表示字段、 enum常量、方法或构造函数参数、局部变量、资源变量或异常参数
                VariableElement element = (VariableElement) info.bindViewElements.get(id);
                String fieldName = element.getSimpleName().toString();
                log("filedName:" + fieldName);
                //生成真正的初始化控件代码
                implMethod.beginControlFlow("if (source instanceof android.app.Activity)")
                        .addStatement("activity.$L = ((android.app.Activity) source).findViewById($L)", fieldName, id)
                        .nextControlFlow("else")
                        .addStatement("activity.$L = ((android.view.View)source).findViewById($L)", fieldName, id)
                        .endControlFlow();
            }

            //=====================================onClick 相关==========================
            //构造函数
            MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(className, "activity");

            for (String methodName:info.onClickElements.keySet()){
                Element element = info.onClickElements.get(methodName);
                //ExecutableElement:表示类或接口的方法、构造函数
                ExecutableElement executableElement = (ExecutableElement) element;
                OnClick annotation = executableElement.getAnnotation(OnClick.class);
                int[] ids = annotation.value();
                for (int idTemp: ids){
                    //匿名内部类
                    TypeSpec comparator = TypeSpec.anonymousClassBuilder("")
                            .addSuperinterface(ClassName.get("android.view","View.OnClickListener"))
                            .addMethod(MethodSpec.methodBuilder("onClick")
                                    .addAnnotation(Override.class)
                                    .addModifiers(Modifier.PUBLIC)
                                    .addStatement("activity.$L(view)",element.getSimpleName())
                                    .addParameter(ClassName.get("android.view","View"), "view")
                                    .build())
                            .build();

                    constructorBuilder.addStatement("activity.findViewById($L).setOnClickListener($L)",idTemp,comparator);
                }
            }

            //创建类
            TypeSpec typeSpec = TypeSpec.classBuilder(classBindName)
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(constructorBuilder.build())//构造器
                    .addSuperinterface(parameterizedTypeName) //实现接口
                    .addMethod(implMethod.build()) //添加类中的方法
                    .build();
            try {
                //生成 MainActivity_Binding.java文件
                JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                        .build();
                javaFile.writeTo(filer);//生成java文件
                javaFile.writeTo(System.out);//控制台输出生成的代码
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;


    }
}
