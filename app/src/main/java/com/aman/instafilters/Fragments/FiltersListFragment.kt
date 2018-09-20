package com.aman.instafilters.Fragments

import com.aman.instafilters.Adapters.ThumbnailsAdapter
import com.aman.instafilters.MainActivity

import com.aman.instafilters.R
import com.aman.instafilters.Utils.BitmapUtils


import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment

import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.zomato.photofilters.FilterPack
import com.zomato.photofilters.imageprocessors.Filter
import com.zomato.photofilters.utils.ThumbnailItem
import com.zomato.photofilters.utils.ThumbnailsManager

import java.util.ArrayList


class FiltersListFragment : BottomSheetDialogFragment(), ThumbnailsAdapter.ThumbnailsAdapterListener {


    lateinit var recyclerView: RecyclerView

    lateinit var mAdapter: ThumbnailsAdapter

    lateinit var thumbnailItemList: ArrayList<ThumbnailItem>

    internal var listener: FiltersListFragmentListener? = null

    companion object {

        internal var instance: FiltersListFragment? =null

        fun getInstance() : FiltersListFragment {

            if(instance ==null) instance = FiltersListFragment()

            return instance!!


        }

    }



    fun setListener(listener: FiltersListFragmentListener) {
        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_filters_list, container, false)

        recyclerView=view.findViewById(R.id.recycler_view) as RecyclerView

        thumbnailItemList = ArrayList()
        mAdapter = ThumbnailsAdapter(activity!!, thumbnailItemList, this)

        val mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()



        recyclerView!!.adapter = mAdapter

        prepareThumbnail(MainActivity.filteredImage)

        return view
    }

    /**
     * Renders thumbnails in horizontal list
     * loads default image from Assets if passed param is null
     *
     * @param bitmap
     */
    fun prepareThumbnail(bitmap: Bitmap?) {
        val r = Runnable {
            val thumbImage: Bitmap?

            if (bitmap == null) {
                thumbImage = BitmapUtils.getBitmapFromAssets(activity!!, MainActivity.IMAGE_NAME, 100, 100)

            } else {
                thumbImage = Bitmap.createScaledBitmap(bitmap, 100, 100, false)
            }

            if (thumbImage == null)
                return@Runnable

            ThumbnailsManager.clearThumbs()
            thumbnailItemList.clear()

            // add normal bitmap first
            val thumbnailItem = ThumbnailItem()
            thumbnailItem.image = thumbImage
            thumbnailItem.filterName = getString(R.string.filter_normal)
            ThumbnailsManager.addThumb(thumbnailItem)

            val filters = FilterPack.getFilterPack(activity!!)

            for (filter in filters) {
                val tI = ThumbnailItem()
                tI.image = thumbImage
                tI.filter = filter
                tI.filterName = filter.name
                ThumbnailsManager.addThumb(tI)
            }

            thumbnailItemList.addAll(ThumbnailsManager.processThumbs(activity))

            activity!!.runOnUiThread { mAdapter.notifyDataSetChanged() }


        }

        Thread(r).start()
    }

    override fun onFilterSelected(filter: Filter) {
        if (listener != null)
            listener!!.onFilterSelected(filter)
    }

    interface FiltersListFragmentListener {
        fun onFilterSelected(filter: Filter)
    }
}// Required empty public constructor