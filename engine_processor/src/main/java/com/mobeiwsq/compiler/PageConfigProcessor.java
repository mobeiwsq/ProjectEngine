package com.mobeiwsq.compiler;


import com.google.auto.service.AutoService;
import com.mobeiwsq.annotation.OnClick;
import com.mobeiwsq.annotation.Page;
import com.mobeiwsq.annotation.enums.CoreAnim;
import com.mobeiwsq.annotation.model.PageInfo;
import com.mobeiwsq.compiler.util.Consts;
import com.mobeiwsq.compiler.util.Logger;
import com.squareup.javapoet.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.mobeiwsq.compiler.util.Consts.KEY_MODULE_NAME;


/**
 * 页面配置自动生成器
 *
 * @author xuexiang
 */
@AutoService(Processor.class)
public class PageConfigProcessor extends AbstractProcessor {

    /**
     * 文件相关的辅助类
     */
    private Filer mFiler;
    private Types mTypes;
    private Elements mElements;


    //全类名：生成复制类所需信息
    private Map<String, AnnotationInfo> annotationInfoMap = new HashMap<>();


    /**
     * 日志相关的辅助类
     */
    private Logger mLogger;

    /**
     * Module name, maybe its 'app' or others
     */
    private String moduleName = null;
    /**
     * 页面配置所在的包名
     */
    private static final String PAGE_CONFIG_PACKAGE_NAME = "com.mobeiwsq.page.config";

    private static final String PAGE_CONFIG_CLASS_NAME_SUFFIX = "PageConfig";

    private TypeMirror mFragment = null;


    private void log(String msg) {
        System.out.println(msg);
    }

    /**
     * 初始化操作
     *
     * @param processingEnv
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mTypes = processingEnv.getTypeUtils();
        mElements = processingEnv.getElementUtils();
        mLogger = new Logger(processingEnv.getMessager());

        // Attempt to get user configuration [moduleName]
        Map<String, String> options = processingEnv.getOptions();
        if (MapUtils.isNotEmpty(options)) {
            moduleName = options.get(KEY_MODULE_NAME);
        }

        if (StringUtils.isNotEmpty(moduleName)) {
            moduleName = moduleName.replaceAll("[^0-9a-zA-Z_]+", "");

            mLogger.info("The user has configuration the module name, it was [" + moduleName + "]");
        } else {
            mLogger.info("These no module name, at 'build.gradle', like :\n" +
                    "javaCompileOptions {\n" +
                    "    annotationProcessorOptions {\n" +
                    "        arguments = [ moduleName : project.getName() ]\n" +
                    "    }\n" +
                    "}\n");
            //默认是app
            moduleName = "app";
//            throw new RuntimeException("XPage::Compiler >>> No module name, for more information, look at gradle log.");
        }

        mFragment = mElements.getTypeElement(Consts.FRAGMENT).asType();

        mLogger.info(">>> PageConfigProcessor init. <<<");
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
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(Page.class.getCanonicalName());
        set.add(OnClick.class.getCanonicalName());
        return set;
    }

    private void parsePages(Set<? extends Element> pageElements) throws IOException {
        if (CollectionUtils.isNotEmpty(pageElements)) {
            mLogger.info(">>> Found Pages, size is " + pageElements.size() + " <<<");

            ClassName pageConfigClassName = ClassName.get(PAGE_CONFIG_PACKAGE_NAME, upperFirstLetter(moduleName) + PAGE_CONFIG_CLASS_NAME_SUFFIX);
            TypeSpec.Builder pageConfigBuilder = TypeSpec.classBuilder(pageConfigClassName);

             /*
               private static PageConfig sInstance;
             */
            FieldSpec instanceField = FieldSpec.builder(pageConfigClassName, "sInstance")
                    .addModifiers(Modifier.PRIVATE)
                    .addModifiers(Modifier.STATIC)
                    .build();

              /*

              ``List<PageInfo>```
             */
            ParameterizedTypeName inputListTypeOfPage = ParameterizedTypeName.get(
                    ClassName.get(List.class),
                    ClassName.get(PageInfo.class)
            );

            /*
               private List<PageInfo> mPages = new ArrayList<>();
             */
            FieldSpec pagesField = FieldSpec.builder(inputListTypeOfPage, "mPages")
                    .addModifiers(Modifier.PRIVATE)
                    .build();

            //构造函数
            MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PRIVATE)
                    .addStatement("mPages = new $T<>()", ClassName.get(ArrayList.class));

            TypeMirror tm;
            String name;
            for (Element element : pageElements) {
                tm = element.asType();
                // Fragment
                // 只有是Fragment的子类才会添加进来，不会添加activity
                if (mTypes.isSubtype(tm, mFragment)) {
                    mLogger.info(">>> Found Fragment Page: " + tm.toString() + " <<<");

                    Page page = element.getAnnotation(Page.class);
                    name = StringUtils.isEmpty(page.name()) ? element.getSimpleName().toString() : page.name();

                    constructorBuilder.addStatement("mPages.add(new $T($S, $S, $S, $T.$L, $L))",
                            PageInfo.class,
                            name,
                            tm.toString(),
                            PageInfo.getParams(page.params()),
                            ClassName.get(CoreAnim.class),
                            page.anim(),
                            page.extra());
                }
            }

            MethodSpec constructorMethod = constructorBuilder.build();

            MethodSpec instanceMethod = MethodSpec.methodBuilder("getInstance")
                    .addModifiers(Modifier.PUBLIC)
                    .addModifiers(Modifier.STATIC)
                    .returns(pageConfigClassName)
                    .addCode("if (sInstance == null) {\n" +
                            "    synchronized ($T.class) {\n" +
                            "        if (sInstance == null) {\n" +
                            "            sInstance = new $T();\n" +
                            "        }\n" +
                            "    }\n" +
                            "}\n", pageConfigClassName, pageConfigClassName)
                    .addStatement("return sInstance")
                    .build();

            MethodSpec getPagesMethod = MethodSpec.methodBuilder("getPages")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(inputListTypeOfPage)
                    .addStatement("return mPages")
                    .build();

            CodeBlock javaDoc = CodeBlock.builder()
                    .add("<p>这是PageConfigProcessor自动生成的类，用以自动进行页面的注册。</p>\n")
                    .add("\n")
                    .add("@author mobeiwsq \n")
                    .add("@date ").add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).add("\n")
                    .build();

            pageConfigBuilder
                    .addJavadoc(javaDoc)
                    .addModifiers(Modifier.PUBLIC)
                    .addField(instanceField)
                    .addField(pagesField)
                    .addMethod(constructorMethod)
                    .addMethod(instanceMethod)
                    .addMethod(getPagesMethod);
            JavaFile javaFile = JavaFile.builder(PAGE_CONFIG_PACKAGE_NAME, pageConfigBuilder.build()).build();

            //生成java文件
            javaFile.writeTo(mFiler);
            //控制台输出生成的代码
            javaFile.writeTo(System.out);
        }
    }

    /**
     * 主要的注解解析
     *
     * @param annotations 注解集合
     * @param roundEnv
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (CollectionUtils.isNotEmpty(annotations)) {
            Set<? extends Element> pageElements = roundEnv.getElementsAnnotatedWith(Page.class);
            try {
                mLogger.info(">>> Found Pages, start... <<<");
                parsePages(pageElements);

            } catch (Exception e) {
                mLogger.error(e);
            }
            return true;
        }
        return false;


    }

    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(final String s) {
        if (StringUtils.isEmpty(s) || !Character.isLowerCase(s.charAt(0))) {
            return s;
        }
        return (char) (s.charAt(0) - 32) + s.substring(1);
    }

}
