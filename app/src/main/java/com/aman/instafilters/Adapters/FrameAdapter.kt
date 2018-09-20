package com.aman.instafilters.Adapters

import com.aman.instafilters.R
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class FrameAdapter(private val context: Context, private val listener: FrameSelectedListener): RecyclerView.Adapter<FrameAdapter.FrameViewHolder>() {


     var frameList:ArrayList<Int>
    private var selectedFramePosition:Int?=null

    init {
        frameList=getFrameAList()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrameViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.frame_item, parent, false)

        return FrameViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return frameList.size
    }

    override fun onBindViewHolder(holder: FrameViewHolder, position: Int) {

        if(position==selectedFramePosition)

            holder.img_check.visibility=View.VISIBLE
        else
            holder.img_check.visibility=View.GONE

        holder.img_frame.setImageResource(frameList.get(position))

        holder.img_frame.setOnClickListener{
            listener.onFrameSelected(frameList.get(position))
            selectedFramePosition=position
            notifyDataSetChanged()

        }

    }


    class FrameViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){

         var img_check:ImageView
        var img_frame:ImageView

        init {
            img_check=itemView!!.findViewById(R.id.img_check)
            img_frame=itemView!!.findViewById(R.id.img_frame)


        }
    }

    interface FrameSelectedListener {
        fun onFrameSelected(Frame:Int)
    }



    fun getFrameAList(): ArrayList<Int> {

        var list=ArrayList<Int>()

        list.add(R.drawable.card_1_resize)
        list.add(R.drawable.card_2_resize)
        list.add(R.drawable.card_3_resize)
        list.add(R.drawable.card_4_resize)
        list.add(R.drawable.card_5_resize)
        list.add(R.drawable.card_6_resize)
        list.add(R.drawable.card_7_resize)
        list.add(R.drawable.card_8_resize)
        list.add(R.drawable.card_9_resize)
        list.add(R.drawable.card_10_resize)

        return list
    }
}