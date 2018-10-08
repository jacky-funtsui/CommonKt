package com.ssf.framework.main.mvvm.adapter.delegate;

import android.util.SparseArray;


/**
 * 布局处理提供者委托，用来装配布局处理提供者
 */
public class ProviderDelegate {

    private SparseArray<BaseItemProvider> mItemProviders = new SparseArray<>();

    public ProviderDelegate() {
    }

    public ProviderDelegate(BaseItemProvider... providers) {
        registerProvider(providers);
    }

    public void registerProvider(BaseItemProvider... providers) {
        for (BaseItemProvider provider : providers) {
            if (provider == null) {
                throw new NullPointerException("ItemProvider can not be null");
            }

            int viewType = provider.layout();

            if (mItemProviders.get(viewType) == null) {
                mItemProviders.put(viewType, provider);
            }
        }
    }

    public SparseArray<BaseItemProvider> getItemProviders() {
        return mItemProviders;
    }

}
