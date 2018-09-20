package com.aman.instafilters.Adapters

import com.aman.instafilters.R
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.rockerhieu.emojicon.EmojiconTextView

class EmojiAdapter(private val emojiList: ArrayList<String>, private val context: Context, private val listener: EmojiSelectedListener): RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.emoji_item, parent, false)

        return EmojiViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return emojiList.size
    }

    override fun onBindViewHolder(holder: EmojiViewHolder, position: Int) {

        holder.emojiTextView!!.text = emojiList[position]

        holder.emojiTextView!!.setOnClickListener {
            listener.onEmojiSelected(emojiList[position])
        }

    }


    class EmojiViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){

        var emojiTextView: EmojiconTextView?=null

        init {
            emojiTextView= itemView!!.findViewById(R.id.emoji_text_view)

        }
    }

    interface EmojiSelectedListener {

        public fun onEmojiSelected(emoji:String)
    }
}
