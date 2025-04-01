package com.mobeiwsq.engine_project;

/**
 * 创建接口，通过代理类实现具体
 */
public interface IViewInjector<T> {
    void initView(T target,Object source);
}
