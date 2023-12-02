package com.iaspo.equisafe.core.adapter

import android.view.View
import android.view.ViewGroup
import com.iaspo.equisafe.R
import com.iaspo.equisafe.databinding.ItemSlideModeBinding
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder

class ViewBindingSampleAdapter : BaseBannerAdapter<Int>() {

    override fun createViewHolder(
        parent: ViewGroup,
        itemView: View,
        viewType: Int
    ): BaseViewHolder<Int> {
        return ViewBindingViewHolder(ItemSlideModeBinding.bind(itemView))
    }

    override fun bindData(holder: BaseViewHolder<Int>, data: Int, position: Int, pageSize: Int) {
        if (holder is ViewBindingViewHolder) {
            holder.viewBinding.bannerImage.setImageResource(data)
        }
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_slide_mode
    }
}

internal class ViewBindingViewHolder(var viewBinding: ItemSlideModeBinding) :
    BaseViewHolder<Int>(viewBinding.root)