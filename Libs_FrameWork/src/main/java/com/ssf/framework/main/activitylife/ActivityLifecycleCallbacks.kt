package com.ssf.framework.main.activitylife

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.util.*

class ActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks, ActivityState {

    private val activityList = ArrayList<Activity>()
    private val resumeActivity = ArrayList<Activity>()

    override fun count(): Int {
        return activityList.size
    }

    override fun isFront(): Boolean {
        return resumeActivity.size > 0
    }

    override fun current(): Activity? {
        return if (activityList.size > 0) activityList[0] else null
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activityList.add(0, activity)
    }

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {
        if (!resumeActivity.contains(activity)) {
            resumeActivity.add(activity)
        }
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {
        resumeActivity.remove(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}

    override fun onActivityDestroyed(activity: Activity) {
        activityList.remove(activity)
    }

}