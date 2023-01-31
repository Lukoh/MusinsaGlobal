package com.goforer.musinsaglobaltest.presentation.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.*
import coil.load
import com.goforer.base.extension.gone
import com.goforer.base.extension.isNull
import com.goforer.base.extension.setSafeOnClickListener
import com.goforer.base.extension.show
import com.goforer.base.view.holder.BaseViewHolder
import com.goforer.musinsaGlobalTest.R
import com.goforer.musinsaGlobalTest.databinding.ItemDataBinding
import com.goforer.musinsaglobaltest.data.source.model.entity.home.response.goods.GoodsListResponse.Data
import com.goforer.musinsaglobaltest.data.source.model.entity.home.response.goods.GoodsListResponse.Data.Contents
import com.goforer.musinsaglobaltest.data.source.model.entity.home.response.goods.GoodsListResponse.Data.Contents.Style
import com.goforer.musinsaglobaltest.presentation.ui.BaseFragment.Companion.RECYCLER_VIEW_CACHE_SIZE
import com.goforer.musinsaglobaltest.presentation.ui.MainActivity
import com.goforer.musinsaglobaltest.presentation.ui.home.adapter.content.ContentBannerAdapter
import com.goforer.musinsaglobaltest.presentation.ui.home.adapter.content.ContentGridAdapter
import com.goforer.musinsaglobaltest.presentation.ui.home.adapter.content.ContentScrollAdapter
import com.goforer.musinsaglobaltest.presentation.ui.home.adapter.content.ContentStyleAdapter
import timber.log.Timber

class DataAdapter(
    private val context: Context,
    private inline val onLoadContent: (linkUrl: String) -> Unit,
    private inline val onShowStyleList: (styles: List<Style>) -> Unit
) : ListAdapter<Data, BaseViewHolder<Data>>(DataDiffUtil) {
    private var _binding: ItemDataBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val FOOTER_REFRESH = "REFRESH"
        private const val FOOTER_MORE = "MORE"

        private const val CONTENT_GRID = "GRID"
        private const val CONTENT_SCROLL = "SCROLL"
        private const val CONTENT_BANNER = "BANNER"
        private const val CONTENT_STYLE = "STYLE"
    }

    object DataDiffUtil : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.contents == newItem.contents
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.header == newItem.header
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Data> {
        val contextThemeWrapper = ContextThemeWrapper(context, R.style.AppTheme)

        _binding = null
        _binding = ItemDataBinding.inflate(
            LayoutInflater.from(contextThemeWrapper), parent, false
        )

        return DataItemHolder(binding, this)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Data>, position: Int) {
        getItem(position)?.let {
            holder.onBindItemHolder(holder, it, position)
        }
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<Data>) {
        super.onViewDetachedFromWindow(holder)


        _binding = null
    }

   class DataItemHolder(
        private val binding: ItemDataBinding,
        private val adapter: DataAdapter
    ) : BaseViewHolder<Data>(binding.root) {
       private lateinit var gridAdapter: ContentGridAdapter
       private lateinit var scrollAdapter: ContentScrollAdapter
       private lateinit var bannerAdapter: ContentBannerAdapter
       private lateinit var styleAdapter: ContentStyleAdapter

       private var lastIndex = 0

        override fun onBindItemHolder(holder: BaseViewHolder<Data>, item: Data, position: Int) {
            with(binding) {
                item.header.isNull({
                    itemHeader.root.gone()
                    vDivider1.gone()
                    vDivider2.gone()
                }, {
                    with(itemHeader) {
                        root.show()
                        it.iconURL.isNull({
                            ivIcon.gone()
                        }, {
                            ivIcon.load(it)
                            ivIcon.show()
                        })

                        tvTitle.text = it.title
                        it.linkURL.isNull({
                            tvAll.gone()
                        }, {
                            tvAll.show()
                            tvAll.text = adapter.context.getString(R.string.header_all)
                        })
                    }

                })

                item.footer.isNull({
                    itemFooter.root.gone()
                }, {
                    with(itemFooter) {
                        root.show()
                        when(it.type) {
                            FOOTER_REFRESH -> {
                                ivIcon.show()
                                ivIcon.load(it.iconURL)
                                if (item.contents.type == CONTENT_STYLE) {
                                    tvAll.show()
                                } else
                                    tvAll.gone()
                            }

                            FOOTER_MORE -> {
                                ivIcon.gone()
                                if (item.contents.type == CONTENT_STYLE) {
                                    tvAll.show()
                                } else
                                    tvAll.gone()
                            }
                        }

                        tvState.text = it.title
                    }
                })

                setContents(item.contents, item.footer?.type)
            }
        }

        override fun onItemSelected() {
            TODO("Not yet implemented")
        }

        override fun onItemClear() {
            TODO("Not yet implemented")
        }

        private fun setContents(content: Contents, footerType: String?) {
            when(content.type) {
                CONTENT_GRID -> {
                    gridAdapter = ContentGridAdapter(adapter.context) {
                        adapter.onLoadContent(it)
                    }
                }

                CONTENT_SCROLL -> {
                    scrollAdapter = ContentScrollAdapter(adapter.context) {
                        adapter.onLoadContent(it)
                    }
                }

                CONTENT_BANNER -> {
                    bannerAdapter = ContentBannerAdapter(adapter.context) {
                        adapter.onLoadContent(it)
                    }
                }

                CONTENT_STYLE -> {
                    styleAdapter = ContentStyleAdapter(adapter.context) {
                        adapter.onLoadContent(it)
                    }
                }
            }

            submitContents(adapter.context, content, footerType)
        }

       private fun submitContents(context: Context, contents: Contents, footerType: String?) {
           with(binding) {
               when(contents.type) {
                   CONTENT_GRID -> {
                       rvContents.apply {
                           val gridLayoutManager =
                               GridLayoutManager(context as MainActivity, 3)

                           gridLayoutManager.orientation = GridLayoutManager.VERTICAL
                           if (::gridAdapter.isInitialized) {
                               gridAdapter.stateRestorationPolicy =
                                   StateRestorationPolicy.PREVENT_WHEN_EMPTY
                               itemAnimator?.changeDuration = 0
                               (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
                               setHasFixedSize(false)
                               setItemViewCacheSize(RECYCLER_VIEW_CACHE_SIZE)
                               layoutManager = gridLayoutManager
                               adapter = gridAdapter
                               lastIndex = if (contents.goods.size < 6)
                                   contents.goods.size.minus(1)
                               else
                                   lastIndex.plus(6)

                               gridAdapter.submitList(contents.goods.subList(0, lastIndex))
                           }
                       }

                       itemFooter.layoutFooter.setSafeOnClickListener {
                           footerType?.let {
                               when(it) {
                                   FOOTER_REFRESH -> {
                                       contents.styles.shuffle()
                                       styleAdapter.submitList(null)
                                       styleAdapter.submitList(contents.styles.subList(lastIndex, lastIndex.plus(6)))
                                   }

                                   FOOTER_MORE -> {
                                       if (contents.goods.size - lastIndex >= 6) {
                                           lastIndex = lastIndex.plus(6)
                                           gridAdapter.submitList(contents.goods.subList(0, lastIndex))
                                       } else {
                                           gridAdapter.submitList(contents.goods.subList(0, contents.goods.size))
                                           itemFooter.root.gone()
                                       }
                                   }

                                   else -> {}
                               }
                           }
                       }
                   }

                   CONTENT_SCROLL -> {
                       rvContents.apply {
                           val linearLayoutManager =
                               LinearLayoutManager(context as MainActivity, RecyclerView.HORIZONTAL, false)

                           rvContents.show()
                           linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                           if (::scrollAdapter.isInitialized) {
                               scrollAdapter.stateRestorationPolicy =
                                   StateRestorationPolicy.PREVENT_WHEN_EMPTY
                               itemAnimator?.changeDuration = 0
                               (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
                               setHasFixedSize(false)
                               setItemViewCacheSize(RECYCLER_VIEW_CACHE_SIZE)
                               layoutManager = linearLayoutManager
                               adapter = scrollAdapter
                               scrollAdapter.submitList(contents.goods)
                           }
                       }

                       itemFooter.layoutFooter.setSafeOnClickListener(600L) {
                           footerType?.let {
                               when(it) {
                                   FOOTER_REFRESH -> {
                                       contents.goods.shuffle()
                                       scrollAdapter.submitList(null)
                                       scrollAdapter.submitList(contents.goods)
                                   }

                                   else -> {}
                               }
                           }
                       }
                   }

                   CONTENT_BANNER -> {
                       rvContents.apply {
                           val linearLayoutManager =
                               LinearLayoutManager(context as MainActivity, RecyclerView.HORIZONTAL, false)
                           val snapHelper = PagerSnapHelper()

                           rvContents.show()
                           snapHelper.attachToRecyclerView(this)
                           linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                           if (::bannerAdapter.isInitialized) {
                               bannerAdapter.stateRestorationPolicy =
                                   StateRestorationPolicy.PREVENT_WHEN_EMPTY
                               itemAnimator?.changeDuration = 0
                               (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
                               setHasFixedSize(false)
                               setItemViewCacheSize(RECYCLER_VIEW_CACHE_SIZE)
                               layoutManager = linearLayoutManager
                               adapter = bannerAdapter
                               bannerAdapter.submitList(contents.banners)
                           }
                       }
                   }

                   CONTENT_STYLE -> {
                       rvContents.apply {
                           val gridLayoutManager =
                               GridLayoutManager(context as MainActivity, 2)

                           gridLayoutManager.orientation = GridLayoutManager.VERTICAL
                           if (::styleAdapter.isInitialized) {
                               styleAdapter.stateRestorationPolicy =
                                   StateRestorationPolicy.PREVENT_WHEN_EMPTY
                               itemAnimator?.changeDuration = 0
                               (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
                               setHasFixedSize(false)
                               setItemViewCacheSize(RECYCLER_VIEW_CACHE_SIZE)
                               layoutManager = gridLayoutManager
                               adapter = styleAdapter
                               lastIndex = lastIndex.plus(4)
                               styleAdapter.submitList(contents.styles.subList(0, lastIndex))
                           }
                       }

                       itemFooter.layoutFooter.setSafeOnClickListener {
                           footerType?.let {
                               when(it) {
                                   FOOTER_REFRESH -> {
                                       contents.styles.shuffle()
                                       styleAdapter.submitList(null)
                                       styleAdapter.submitList(contents.styles.subList(lastIndex, lastIndex.plus(4)))
                                   }

                                   FOOTER_MORE -> {
                                       if (contents.styles.size - lastIndex >= 4) {
                                           lastIndex = lastIndex.plus(4)
                                           styleAdapter.submitList(contents.styles.subList(0, lastIndex))
                                       } else {
                                           styleAdapter.submitList(contents.styles.subList(0, contents.styles.size))
                                           itemFooter.root.gone()
                                       }
                                   }

                                   else -> {}
                               }
                           }
                       }

                       itemFooter.tvAll.setSafeOnClickListener {
                           adapter.onShowStyleList(contents.styles)
                       }
                   }
                   else -> {
                       Timber.d("else.......")
                   }
               }
           }
       }
    }
}