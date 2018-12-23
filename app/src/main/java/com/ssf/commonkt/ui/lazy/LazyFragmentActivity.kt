package com.ssf.commonkt.ui.lazy

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.util.SparseArray
import com.ssf.commonkt.R
import com.ssf.commonkt.databinding.ActivityLazyBinding
import com.ssf.framework.main.mvvm.activity.MVVMActivity

class LazyFragmentActivity : MVVMActivity<ActivityLazyBinding>(R.layout.activity_lazy) {
    override fun init() {
        //选择注释下面的方式来查看两种请看下的加载生命周期
        //ViewPager模式
        initPageModel()
        //normal模式
//        initNoramlModel()

    }

    override fun initStatusBar() {
    }

    private fun initPageModel(){
        val viewPager = binding.viewPager

        val titles = arrayOf("page1", "page2", "page3", "page4")

        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            val fragments by lazy { SparseArray<Fragment>() }

            override fun getItem(position: Int): Fragment {
                var fragment = fragments[position]
                if (fragment == null) {
                    fragment = SampleLazyLogFragment.newInstance(getPageTitle(position)!!.toString())
                    fragments.put(position, fragment)
                }
                return fragment
            }

            override fun getCount(): Int {
                return titles.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return titles[position]
            }
        }

        binding.tabLayout.setupWithViewPager(viewPager)
    }

    private fun initNoramlModel(){
        supportFragmentManager.beginTransaction().replace(android.R.id.content, SampleLazyLogFragment.newInstance("page1"))
                .commit()
    }
}
