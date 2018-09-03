package com.ssf.framework.main.mvvm.lifecycle;

import com.trello.rxlifecycle2.LifecycleTransformer;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

import static com.trello.rxlifecycle2.RxLifecycle.bind;

/**
 * Created by Hzz on 2018/8/29
 */
public class RxLifecycleViewModel {
    public static <T> LifecycleTransformer<T> bindViewModel(final Observable<ViewModelEvent> lifecycle) {
        return bind(lifecycle, VIEW_LIFECYCLE);
    }

    private static final Function<ViewModelEvent, ViewModelEvent> VIEW_LIFECYCLE =
            new Function<ViewModelEvent, ViewModelEvent>() {
                @Override
                public ViewModelEvent apply(ViewModelEvent lastEvent) throws Exception {
                    switch (lastEvent) {
                        case CLEAR:
                            throw new UnsupportedOperationException("Cannot bind to Fragment lifecycle when outside of it.");

                        default:
                            throw new UnsupportedOperationException("Binding to " + lastEvent + " not yet implemented");
                    }
                }
            };
}
