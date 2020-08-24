package com.zzh.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * 车主邦
 * ---------------------------
 * <p>
 * Created by zhaozh on 2020/8/20.
 */
public class ProxyInfo {

    public static final String PROXY_NAME = "_ViewBinding";

    /**
     * 注解变量的集合
     */
    public Map<Integer, VariableElement> variableElementMap = new HashMap<>();

    /**
     * 生成的代理类名称
     */
    public String proxyClassName;

    /**
     * 生成的代理类的包名
     */
    public String packageName;

    /**
     * 注解的所在类的元素标识
     */
    private TypeElement typeElement;

    public ProxyInfo(TypeElement typeElement, String packageName) {
        this.packageName = packageName;
        this.typeElement = typeElement;
        String className = getClassName(typeElement, packageName);
        this.proxyClassName = className + PROXY_NAME;
    }

    /**
     * 获取生成的代理类的类名
     * 之所以用字符串截取、替换而没用class.getSimpleName() 的原因是内部类注解情况，比如adapter.ViewHolder
     * 内部类反射之后的类名: 例如MyAdapter$ContentViewHolder，中间是“￥” ，而不是 “.”。
     *
     * @param typeElement
     * @param packageName
     * @return
     */
    private String getClassName(TypeElement typeElement, String packageName) {
        int packageLen = packageName.length() + 1;
        return typeElement.getQualifiedName().toString().substring(packageLen)
                .replace('.', '$');
    }

    /**
     * 通过 java poet API生成代理类
     *
     * @return
     */
    public TypeSpec generateProxyClass() {
        //代理类实现的接口
        ClassName viewInjector = ClassName.get("com.zzh.api", "IViewInjector");

        //类
        ClassName className = ClassName.get(typeElement);
        //泛型接口 ，implements IViewInjector<MainActivity>
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(viewInjector, className);

        //生成构造方法
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(className, "target")
                .addStatement("this.target = target");

        //生成接口的实现方法inject()
        MethodSpec.Builder bindBuilder = MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addAnnotation(Override.class)  //添加方法注解
                .addParameter(className, "target")
                .addParameter(Object.class, "source");

        for (int id : variableElementMap.keySet()) {
            VariableElement variableElement = variableElementMap.get(id);
            String fieldName = variableElement.getSimpleName().toString();
//            bindBuilder.addStatement(
//                    " if (source instanceof android.app.Activity){target.$L = ((android.app.Activity) source).findViewById($L);}" +
//                            "else {target.$L = ((android.view.View) source).findViewById($L);}",
//                    fieldName, id, fieldName, id);
            bindBuilder.addStatement(
                    " if(source instanceof android.app.Activity){target.$L = ((android.app.Activity) source).findViewById( $L);}" +
                            "else{target.$L = ((android.view.View)source).findViewById($L);}",
                    fieldName, id, fieldName, id);
        }

        MethodSpec bindMethodSpec = bindBuilder.build();

        //创建类
        TypeSpec typeSpec = TypeSpec.classBuilder(proxyClassName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(parameterizedTypeName)   //实现接口
                .addMethod(bindMethodSpec)  //添加类中的方法
                .build();

        return typeSpec;

    }
}
