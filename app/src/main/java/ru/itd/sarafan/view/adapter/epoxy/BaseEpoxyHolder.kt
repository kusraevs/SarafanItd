package ru.itd.sarafan.view.adapter.epoxy

import android.support.annotation.CallSuper
import android.view.View
import butterknife.ButterKnife
import com.airbnb.epoxy.EpoxyHolder

/**
 * Created by macbook on 19.10.17.
 */
abstract class BaseEpoxyHolder : EpoxyHolder() {
    @CallSuper
    override fun bindView(itemView: View) {
        ButterKnife.bind(this, itemView)
    }
}