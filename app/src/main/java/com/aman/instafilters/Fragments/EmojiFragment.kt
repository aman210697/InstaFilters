package com.aman.instafilters.Fragments


import com.aman.instafilters.Adapters.EmojiAdapter
import com.aman.instafilters.R
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ja.burhanrashid52.photoeditor.PhotoEditor


class EmojiFragment : BottomSheetDialogFragment(), EmojiAdapter.EmojiSelectedListener {


    lateinit var emojiRecyclerView: RecyclerView
    lateinit var listener: EmojiFragmentListener
    lateinit var emojiAdapter: EmojiAdapter


    companion object {

        internal var instance: EmojiFragment? =null

        fun getInstance() : EmojiFragment {

            if(instance ==null) instance = EmojiFragment()

            return instance!!


        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_emoji, container, false)

        emojiRecyclerView=view.findViewById(R.id.emoji_recyclerView)
        emojiRecyclerView.setHasFixedSize(true)
        emojiRecyclerView.layoutManager=GridLayoutManager(activity,5)

        emojiAdapter= EmojiAdapter(PhotoEditor.getEmojis(activity), activity!!, this)
        emojiRecyclerView.adapter=emojiAdapter


        return view
    }

    override fun onEmojiSelected(emoji: String) {

        listener.onSelectedEmoji(emoji)
    }

    fun setEmojiListener(l: EmojiFragmentListener){

        listener=l
    }


    interface EmojiFragmentListener{

        fun onSelectedEmoji(emoji:String)
    }
}
