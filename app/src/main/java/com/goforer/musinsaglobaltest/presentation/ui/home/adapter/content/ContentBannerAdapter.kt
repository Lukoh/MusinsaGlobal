package com.goforer.musinsaglobaltest.presentation.ui.home.adapter.content

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import coil.load
import com.goforer.base.extension.gone
import com.goforer.base.extension.setSafeOnClickListener
import com.goforer.base.extension.show
import com.goforer.base.view.holder.BaseViewHolder
import com.goforer.musinsaGlobalTest.R
import com.goforer.musinsaGlobalTest.databinding.ItemBannerBinding
import com.goforer.musinsaglobaltest.data.source.model.entity.home.response.goods.GoodsListResponse.Data.Contents.Banner

class ContentBannerAdapter(
    private val context: Context,
    private inline val onLoadContent: (linkUrl: String) -> Unit
) : ListAdapter<Banner, BaseViewHolder<Banner>>(BannerDiffUtil) {
    private var _binding: ItemBannerBinding? = null
    private val binding get() = _binding!!

    object BannerDiffUtil : DiffUtil.ItemCallback<Banner>() {
        override fun areItemsTheSame(oldItem: Banner, newItem: Banner): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Banner, newItem: Banner): Boolean {
            return oldItem.description == newItem.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Banner> {
        val contextThemeWrapper = ContextThemeWrapper(context, R.style.AppTheme)

        _binding = null
        _binding = ItemBannerBinding.inflate(
            LayoutInflater.from(contextThemeWrapper), parent, false
        )

        return BannerItemHolder(binding, this)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Banner>, position: Int) {
        getItem(position)?.let {
            holder.onBindItemHolder(holder, it, position)
        }
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<Banner>) {
        super.onViewDetachedFromWindow(holder)

        _binding = null
    }

    class BannerItemHolder(
        private val binding: ItemBannerBinding,
        private val adapter: ContentBannerAdapter
    ) : BaseViewHolder<Banner>(binding.root) {
        override fun onBindItemHolder(holder: BaseViewHolder<Banner>, item: Banner, position: Int) {
            with(binding) {
                ivGoods.load(item.thumbnailURL)
                if (item.title.isNotEmpty()) {
                    tvTitle.text = item.title
                    tvTitle.show()
                } else
                    tvTitle.gone()

                if (item.keyword.isNotEmpty()) {
                    tvShowcase.text = item.keyword
                    tvShowcase.show()
                } else
                    tvShowcase.gone()

                if (item.description.isNotEmpty()) {
                    tvDescription.text = item.description
                    tvDescription.show()
                } else
                    tvDescription.gone()

                if (position == adapter.itemCount.minus(1))
                    tvCount.text = adapter.context.getString(R.string.banner_reached_max, position.plus(1), adapter.itemCount)
                else
                    tvCount.text = adapter.context.getString(R.string.banner_count, position.plus(1), adapter.itemCount)

                root.setSafeOnClickListener {
                    adapter.onLoadContent(item.linkURL)
                }
            }
        }

        override fun onItemSelected() {}

        override fun onItemClear() {}

    }
}