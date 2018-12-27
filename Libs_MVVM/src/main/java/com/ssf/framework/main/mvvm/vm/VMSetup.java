package com.ssf.framework.main.mvvm.vm;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * VM全局配置
 */
public class VMSetup {
    private static VMSetup INSTANCE = new VMSetup();

    private VMSetup() {
    }

    public static VMSetup getInstance() {
        return INSTANCE;
    }

    @Nullable
    private IObserverProvider defaultObserverProvider;

    public void setDefaultObserverProvider(@NotNull IObserverProvider provider) {
        this.defaultObserverProvider = provider;
    }

    @Nullable
    public IObserverProvider getDefaultObserverProvider() {
        return defaultObserverProvider;
    }
}
