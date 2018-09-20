package com.aman.instafilters.Fragments


import com.aman.instafilters.Adapters.FrameAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.aman.instafilters.R
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button


class FrameFragment : BottomSheetDialogFragment(), FrameAdapter.FrameSelectedListener {


    lateinit var frameRecyclerView: RecyclerView
    lateinit var frameAdapter: FrameAdapter
    lateinit var listener: FrameFragmentListener
    lateinit var btn_add_frame: Button
    var frameSelected = 0


    companion object {

        internal var instance: FrameFragment? = null

        fun getInstance(): FrameFragment {

            if (instance == null) instance = FrameFragment()

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
        var view = inflater.inflate(R.layout.fragment_frame, container, false)

        btn_add_frame = view.findViewById(R.id.btn_add_frame)
        btn_add_frame.setOnClickListener {

            listener.onFrameSelected(frameSelected)
            dismiss()
        }


        frameAdapter = FrameAdapter(activity!!, this)
        frameRecyclerView = view.findViewById(R.id.frame_recyclerview)
        frameRecyclerView.setHasFixedSize(true)
        frameRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        frameRecyclerView.adapter = frameAdapter
        return view
    }

    override fun onFrameSelected(frame: Int) {

        frameSelected = frame

    }

    fun setFrameListener(l: FrameFragmentListener) {

        listener = l
    }


    interface FrameFragmentListener {

        fun onFrameSelected(frame: Int)

    }


}
