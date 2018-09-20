package com.aman.instafilters.Fragments



import com.aman.instafilters.R
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.fragment_add_text.*
import top.defaults.colorpicker.ColorPickerPopup


class AddTextFragment : BottomSheetDialogFragment() {

    lateinit var editText: EditText
    // lateinit var colorRecyclerView: RecyclerView
    lateinit var btn_addText: Button
    lateinit var btn_changeColor:Button
    // lateinit var colorAdapter: ColorAdapter
    var textColor = Color.parseColor("#000000")


    lateinit var listener: AddTextFragmentListener

    companion object {

        internal var instance: AddTextFragment? = null

        fun getInstance(): AddTextFragment {
            if (instance == null) instance = AddTextFragment()
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
        var view = inflater.inflate(R.layout.fragment_add_text, container, false)

        editText = view.findViewById(R.id.et_add_text)

        btn_addText = view.findViewById(R.id.btn_add_text)
        btn_changeColor=view.findViewById(R.id.btn_text_color)
        btn_changeColor.setBackgroundColor(textColor)
//        colorRecyclerView=view.findViewById(R.id.text_color_recyclerview)
//        colorRecyclerView.setHasFixedSize(true)
//        colorRecyclerView.layoutManager= LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false)
//
//        colorAdapter= ColorAdapter(activity!!, this)
//
//        colorRecyclerView.adapter=colorAdapter


        btn_addText.setOnClickListener {
            dismiss()
            listener.onAddTextListener(editText.text.toString(), textColor)
        }

        btn_changeColor.setOnClickListener {

            ColorPickerPopup.Builder(activity)
                    .initialColor(textColor) // Set initial color
                    .enableAlpha(true) // Enable alpha slider or not
                    .okTitle("Ok")
                    .cancelTitle("Cancel")
                    .showIndicator(true)
                    .showValue(true)
                    .build()
                    .show(view, object : ColorPickerPopup.ColorPickerObserver {
                        override fun onColorPicked(color: Int) {
                            textColor = color
                            btn_text_color.setBackgroundColor(color)

                        }

                        override fun onColor(color: Int, fromUser: Boolean) {

                        }
                    })


        }


        return view
    }


    fun setAddTextListener(l: AddTextFragmentListener) {

        listener = l
    }


    interface AddTextFragmentListener {

        fun onAddTextListener(text: String, color: Int)
    }


}
