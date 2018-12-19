package com.ssf.commonkt.ui.state

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.ssf.framework.widget.state.IStateLayout

class StateViewModel(app: Application) : AndroidViewModel(app) {

    val state = ObservableField<IStateLayout>(IStateLayout.LOADING)
}