package ru.itd.sarafan.utils

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import ru.itd.sarafan.R

/**
 * Created by macbook on 20.10.17.
 */
class FragmentUtils {
    companion object {
        val POST_FRAGMENT_TAG = "POST_FRAGMENT"


        fun replaceFragment(fragmentManager: FragmentManager, fragment: Fragment, tag: String) {
            if (fragmentManager.findFragmentByTag(tag) == null) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment, tag).commit()
            }
        }

    }
}