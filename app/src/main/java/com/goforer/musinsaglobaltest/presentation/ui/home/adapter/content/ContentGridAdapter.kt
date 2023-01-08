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
import com.goforer.musinsaGlobalTest.databinding.ItemGridBinding
import com.goforer.musinsaglobaltest.data.source.model.entity.home.response.goods.GoodsListResponse.Data.Contents.Good

class ContentGridAdapter(
    private val context: Context,
    private inline val onLoadContent: (linkUrl: String) -> Unit
) : ListAdapter<Good, BaseViewHolder<Good>>(GridDiffUtil) {
    private var _binding: ItemGridBinding? = null
    private val binding get() = _binding!!

    object GridDiffUtil : DiffUtil.ItemCallback<Good>() {
        override fun areItemsTheSame(oldItem: Good, newItem: Good): Boolean {
            return oldItem.brandName == newItem.brandName
        }

        override fun areContentsTheSame(oldItem: Good, newItem: Good): Boolean {
            return oldItem.linkURL == newItem.linkURL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Good> {
        val contextThemeWrapper = ContextThemeWrapper(context, R.style.AppTheme)

        _binding = null
        _binding = ItemGridBinding.inflate(
            LayoutInflater.from(contextThemeWrapper), parent, false
        )

        return GridItemHolder(binding, this)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Good>, position: Int) {
        getItem(position)?.let {
            holder.onBindItemHolder(holder, it, position)
        }
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<Good>) {
        super.onViewDetachedFromWindow(holder)

        _binding = null
    }

    class GridItemHolder(
        private val binding: ItemGridBinding,
        private val adapter: ContentGridAdapter
    ) : BaseViewHolder<Good>(binding.root) {
        override fun onBindItemHolder(holder: BaseViewHolder<Good>, item: Good, position: Int) {
            with(binding.item) {
                ivGoods.load(item.thumbnailURL)
                tvBrandName.text = item.brandName
                if (item.hasCoupon)
                    tvCoupon.show()
                else
                    tvCoupon.gone()

                tvPrice.text = adapter.context.getString(R.string.scroll_price, item.price.toString())
                tvSaleRate.text = adapter.context.getString(R.string.scroll_sale_rate, item.saleRate.toString())

                root.setSafeOnClickListener(600L) {
                    adapter.onLoadContent(item.linkURL)
                }
            }
        }

        override fun onItemSelected() {
            TODO("Not yet implemented")
        }

        override fun onItemClear() {
            TODO("Not yet implemented")
        }

    }
}