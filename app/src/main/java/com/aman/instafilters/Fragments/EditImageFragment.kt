package com.aman.instafilters.Fragments

import com.aman.instafilters.R
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar



class EditImageFragment : BottomSheetDialogFragment(), SeekBar.OnSeekBarChangeListener {

    private var listener: EditImageFragmentListener? = null

    var seekbar_brightness: SeekBar?=null
    var seekbar_contrast: SeekBar?=null
    var seekbar_saturation: SeekBar?=null


    companion object {

        internal var instance: EditImageFragment? =null

        fun getInstance() : EditImageFragment {

            if(instance ==null) instance = EditImageFragment()

            return instance!!


        }

    }



    fun setListener(listener: EditImageFragmentListener) {
        this.listener = listener
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_edit_image, container, false)
        seekbar_brightness=view.findViewById(R.id.seekbar_brightness)as SeekBar
        seekbar_contrast=view.findViewById(R.id.seekbar_contrast)as SeekBar
        seekbar_saturation=view.findViewById(R.id.seekbar_saturation)as SeekBar


        // keeping brightness value b/w -100 / +100
        seekbar_brightness!!.max = 200
        seekbar_brightness!!.progress = 100

        // keeping contrast value b/w 1.0 - 3.0
        seekbar_contrast!!.max = 20
        seekbar_contrast!!.progress = 0

        // keeping saturation value b/w 0.0 - 3.0
        seekbar_saturation!!.max = 30
        seekbar_saturation!!.progress = 10

        seekbar_brightness!!.setOnSeekBarChangeListener(this)
        seekbar_contrast!!.setOnSeekBarChangeListener(this)
        seekbar_saturation!!.setOnSeekBarChangeListener(this)

        return view
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
        var progress = progress
        if (listener != null) {

            if (seekBar.id == R.id.seekbar_brightness) {
                // brightness values are b/w -100 to +100
                listener!!.onBrightnessChanged(progress - 100)
            }

            if (seekBar.id == R.id.seekbar_contrast) {
                // converting int value to float
                // contrast values are b/w 1.0f - 3.0f
                // progress = progress > 10 ? progress : 10;
                progress += 10
                val floatVal = .10f * progress
                listener!!.onContrastChanged(floatVal)
            }

            if (seekBar.id == R.id.seekbar_saturation) {
                // converting int value to float
                // saturation values are b/w 0.0f - 3.0f
                val floatVal = .10f * progress
                listener!!.onSaturationChanged(floatVal)
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        if (listener != null)
            listener!!.onEditStarted()
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        if (listener != null)
            listener!!.onEditCompleted()
    }

    fun resetControls() {
        if(seekbar_brightness!=null && seekbar_contrast!=null &&  seekbar_saturation!=null) {
            seekbar_brightness!!.progress = 100
            seekbar_contrast!!.progress = 0
            seekbar_saturation!!.progress = 10

        }
    }

    interface EditImageFragmentListener {
        fun onBrightnessChanged(brightness: Int)

        fun onSaturationChanged(saturation: Float)

        fun onContrastChanged(contrast: Float)

        fun onEditStarted()

        fun onEditCompleted()
    }
}// Required empty public constructor