package com.aman.instafilters.Fragments


import com.aman.instafilters.R
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.ToggleButton
import top.defaults.colorpicker.ColorPickerPopup


class BrushFragment : BottomSheetDialogFragment(){

    lateinit var seekbar_brushSize: SeekBar
    lateinit var seekbar_opacity: SeekBar
    lateinit var seekbar_eraser: SeekBar
//    lateinit var color_recyclerview: RecyclerView
    lateinit var btn_brush_color: Button
    lateinit var btn_brush_state: ToggleButton

    lateinit var brushEditLayout:LinearLayout
    lateinit var eraserEditLayout:LinearLayout
//    lateinit var colorAdapter: ColorAdapter
    lateinit var listener: BrushFragmentListener
    private var brushColor = Color.parseColor("#000000")
    var isEraserSelected=false


    companion object {

        internal var instance: BrushFragment? = null

        fun getInstance(): BrushFragment {

            if (instance == null) instance = BrushFragment()

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
        var view = inflater.inflate(R.layout.fragment_brush, container, false)
        seekbar_brushSize = view.findViewById(R.id.seekbar_brushSize)
        seekbar_brushSize.setProgress(20)
        seekbar_opacity = view.findViewById(R.id.seekbar_opacity)
        seekbar_opacity.setProgress(100)

        seekbar_eraser=view.findViewById(R.id.seekbar_eraserSize)
        seekbar_eraser.setProgress(10)

        brushEditLayout=view.findViewById(R.id.brush_layout)
        eraserEditLayout=view.findViewById(R.id.eraser_layout)

        btn_brush_state = view.findViewById(R.id.btn_brush_state)
        btn_brush_state.isChecked = false
        btn_brush_color = view.findViewById(R.id.btn_brush_color)
        btn_brush_color.setBackgroundColor(brushColor)



        seekbar_brushSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {


            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                if(!isEraserSelected)
                listener.onBrushSizedChanged(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        seekbar_opacity.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(!isEraserSelected)
                listener.onBrushOpacityChanged(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        seekbar_eraser.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                listener.onEraserSizeChanged(progress.toFloat())

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        btn_brush_state.setOnCheckedChangeListener { buttonView, isChecked ->
            isEraserSelected=isChecked
            if(isChecked){

                brushEditLayout.visibility=View.GONE


            }else{
                brushEditLayout.visibility=View.VISIBLE
                eraserEditLayout.visibility=View.GONE
            }

            listener.onBrushStateChanged(isChecked)

        }

        btn_brush_color.setOnClickListener {

            if(!isEraserSelected) {
                ColorPickerPopup.Builder(activity)
                        .initialColor(brushColor) // Set initial color
                        .enableAlpha(true) // Enable alpha slider or not
                        .okTitle("Ok")
                        .cancelTitle("Cancel")
                        .showIndicator(true)
                        .showValue(true)
                        .build()
                        .show(view, object : ColorPickerPopup.ColorPickerObserver {
                            override fun onColorPicked(color: Int) {
                                brushColor = color
                                btn_brush_color.setBackgroundColor(color)
                                listener.onBrushColorChanged(color)

                            }

                            override fun onColor(color: Int, fromUser: Boolean) {

                            }
                        })

            }


        }

        return view
    }

    fun setBrushListener(listener: BrushFragmentListener) {
        this.listener = listener
    }



    interface BrushFragmentListener {
        fun onBrushSizedChanged(size: Float)
        fun onBrushOpacityChanged(value: Int)
        fun onBrushColorChanged(color: Int)
        fun onBrushStateChanged(isEraser: Boolean)
        fun onEraserSizeChanged(size: Float)

    }

}
