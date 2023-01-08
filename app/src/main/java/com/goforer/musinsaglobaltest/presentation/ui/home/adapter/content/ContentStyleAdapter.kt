package com.goforer.musinsaglobaltest.presentation.ui.home.adapter.content

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import coil.load
import com.goforer.base.extension.setSafeOnClickListener
import com.goforer.base.view.holder.BaseViewHolder
import com.goforer.musinsaGlobalTest.R
import com.goforer.musinsaGlobalTest.databinding.ItemStyleBinding
import com.goforer.musinsaglobaltest.data.source.model.entity.home.response.goods.GoodsListResponse.Data.Contents.Style
import timber.log.Timber

class ContentStyleAdapter(
    private val context: Context,
    private inline val onLoadContent: (linkUrl: String) -> Unit
) : ListAdapter<Style, BaseViewHolder<Style>>(StyleDiffUtil) {
    private var _binding: ItemStyleBinding? = null
    private val binding get() = _binding!!

    object StyleDiffUtil : DiffUtil.ItemCallback<Style>() {
        override fun areItemsTheSame(oldItem: Style, newItem: Style): Boolean {
            return oldItem.linkURL == newItem.linkURL
        }

        override fun areContentsTheSame(oldItem: Style, newItem: Style): Boolean {
            return oldItem.thumbnailURL == newItem.thumbnailURL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Style> {
        val contextThemeWrapper = ContextThemeWrapper(context, R.style.AppTheme)

        _binding = null
        _binding = ItemStyleBinding.inflate(
            LayoutInflater.from(contextThemeWrapper), parent, false
        )

        return StyleItemHolder(binding, this)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Style>, position: Int) {
        getItem(position)?.let {
            holder.onBindItemHolder(holder, it, position)
        }
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<Style>) {
        super.onViewDetachedFromWindow(holder)

        _binding = null
    }

    override fun onCurrentListChanged(previousList: MutableList<Style>, currentList: MutableList<Style>) {
        super.onCurrentListChanged(previousList, currentList)

        Timber.d("Current Style List has been changed.....")
    }

    class StyleItemHolder(
        private val binding: ItemStyleBinding,
        private val adapter: ContentStyleAdapter
    ) : BaseViewHolder<Style>(binding.root) {
        override fun onBindItemHolder(holder: BaseViewHolder<Style>, item: Style, position: Int) {
           with(binding) {
               ivStyle.load(item.thumbnailURL)

               root.setSafeOnClickListener {
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