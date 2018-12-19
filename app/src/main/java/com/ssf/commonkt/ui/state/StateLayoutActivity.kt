package com.ssf.commonkt.ui.state

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ssf.commonkt.R
import com.ssf.framework.widget.state.IStateLayout
import com.ssf.framework.widget.state.StateFrameLayout

class StateLayoutActivity : AppCompatActivity(), View.OnClickListener {


    private val viewModel by lazy {
        ViewModelProviders.of(this).get(StateViewModel::class.java)
    }

    private val stateLayout by lazy{
        findViewById<StateFrameLayout>(R.id.state_layout)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state_layout)
        findViewById<View>(R.id.normal).setOnClickListener(this)
        findViewById<View>(R.id.error).setOnClickListener(this)
        findViewById<View>(R.id.loading).setOnClickListener(this)
        findViewById<View>(R.id.empty).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.normal ->
                stateLayout.stateLayout = IStateLayout.NORMAL
            R.id.error ->
                stateLayout.stateLayout = (IStateLayout.REFRESH)
            R.id.loading ->
                stateLayout.stateLayout = (IStateLayout.LOADING)
            R.id.empty ->
                stateLayout.stateLayout = (IStateLayout.EMPTY)
            else -> {

            }
        }
    }
}
