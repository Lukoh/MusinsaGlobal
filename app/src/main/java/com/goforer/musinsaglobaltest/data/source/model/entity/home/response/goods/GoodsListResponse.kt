package com.goforer.musinsaglobaltest.data.source.model.entity.home.response.goods

import android.os.Parcelable
import com.goforer.musinsaglobaltest.data.source.model.entity.home.BaseEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class GoodsListResponse(
    var data: MutableList<Data>
) : BaseEntity(), Parcelable {
    @Parcelize
    data class Data(
        var contents: Contents,
        val footer: Footer? = null,
        val header: Header? = null
    ) : BaseEntity(), Parcelable {
        @Parcelize
        data class Contents(
            var banners: MutableList<Banner>,
            var goods: MutableList<Good>,
            var styles: MutableList<Style>,
            val type: String
        ) : BaseEntity(), Parcelable {
            @Parcelize
            data class Banner(
                var description: String,
                var keyword: String,
                var linkURL: String,
                var thumbnailURL: String,
                var title: String
            ) : BaseEntity(), Parcelable

            @Parcelize
            data class Good(
                var brandName: String,
                var hasCoupon: Boolean,
                var linkURL: String,
                var price: Int,
                var saleRate: Int,
                var thumbnailURL: String
            ) : BaseEntity(), Parcelable

            @Parcelize
            data class Style(
                var linkURL: String,
                var thumbnailURL: String
            ) : BaseEntity(), Parcelable
        }

        @Parcelize
        data class Header(
            val iconURL: String,
            val linkURL: String? = null,
            val title: String
        ) : BaseEntity(), Parcelable

        @Parcelize
        data class Footer(
            val iconURL: String? = null,
            val title: String,
            val type: String
        ) : BaseEntity(), Parcelable
    }
}