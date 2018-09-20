package com.aman.instafilters.Adapters

import com.aman.instafilters.R
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.zomato.photofilters.imageprocessors.Filter
import com.zomato.photofilters.utils.ThumbnailItem



/**
 * Created by ravi on 23/10/17.
 */

class ThumbnailsAdapter(private val mContext: Context, private val thumbnailItemList: List<ThumbnailItem>, private val listener: ThumbnailsAdapterListener) : RecyclerView.Adapter<ThumbnailsAdapter.MyViewHolder>() {
    private var selectedIndex = 0

    inner class MyViewHolder(view: View):RecyclerView.ViewHolder(view){

        internal var thumbnail: ImageView? = null


        internal var filterName: TextView? = null

        init {

            thumbnail=view.findViewById(R.id.thumbnail)
            filterName=view.findViewById(R.id.filter_name)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.thumbnail_list_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val thumbnailItem = thumbnailItemList[position]

        holder.thumbnail!!.setImageBitmap(thumbnailItem.image)

        holder.thumbnail!!.setOnClickListener {
            listener.onFilterSelected(thumbnailItem.filter)
            selectedIndex = position
            notifyDataSetChanged()
        }

        holder.filterName!!.text = thumbnailItem.filterName

        if (selectedIndex == position) {
            holder.filterName!!.setTextColor(ContextCompat.getColor(mContext, R.color.filter_color_select))
        } else {
            holder.filterName!!.setTextColor(ContextCompat.getColor(mContext, R.color.filter_color_normal))
        }
    }

    override fun getItemCount(): Int {
        return thumbnailItemList.size
    }

    interface ThumbnailsAdapterListener {
        fun onFilterSelected(filter: Filter)
    }
}