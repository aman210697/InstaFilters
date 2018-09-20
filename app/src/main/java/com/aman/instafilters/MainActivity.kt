package com.aman.instafilters

import com.aman.instafilters.R
import com.aman.instafilters.Fragments.*
import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle

import android.support.design.widget.Snackbar

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.zomato.photofilters.imageprocessors.Filter
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter

import com.aman.instafilters.Utils.BitmapUtils
import android.app.Activity
import android.content.ActivityNotFoundException

import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.os.Environment
import android.support.v4.content.FileProvider
import com.aman.instafilters.Fragments.*
import com.yalantis.ucrop.UCrop
import ja.burhanrashid52.photoeditor.OnSaveBitmap
import ja.burhanrashid52.photoeditor.PhotoEditor
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.util.*


@Suppress("UNREACHABLE_CODE")
class MainActivity : AppCompatActivity(), FiltersListFragment.FiltersListFragmentListener, EditImageFragment.EditImageFragmentListener, BrushFragment.BrushFragmentListener, EmojiFragment.EmojiFragmentListener, AddTextFragment.AddTextFragmentListener, FrameFragment.FrameFragmentListener {



    var originalImage: Bitmap? = null

    // to backup image with filter applied

    //  lateinit var filteredImage: Bitmap

    // the final image after applying
    // brightness, saturation, contrast
    lateinit var finalImage: Bitmap
    var imageSelectedUri = Uri.fromFile(File("//android_asset/dog.jpg"))

    lateinit var filtersListFragment: FiltersListFragment
    lateinit var editImageFragment: EditImageFragment
    lateinit var brushFragment: BrushFragment
    lateinit var emojiFragment: EmojiFragment
    lateinit var addTextFragment: AddTextFragment
    lateinit var addFramFragment: FrameFragment


    // modified image values
    var brightnessFinal = 0
    var saturationFinal = 1.0f
    var contrastFinal = 1.0f

    lateinit var photoEditor: PhotoEditor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        photoEditor = PhotoEditor.Builder(this, image_preview)
                .setPinchTextScalable(true)
                .setDefaultEmojiTypeface(Typeface.createFromAsset(assets, "emojione.ttf"))
                .build()
        setSupportActionBar(toolbar)

        supportActionBar!!.title = getString(R.string.app_name)

        loadImage()

        filtersListFragment = FiltersListFragment.getInstance()
        editImageFragment = EditImageFragment.getInstance()
        brushFragment = BrushFragment.getInstance()
        emojiFragment = EmojiFragment.getInstance()
        addTextFragment = AddTextFragment.getInstance()
        addFramFragment = FrameFragment.getInstance()

        btn_filter.setOnClickListener {


            if (filtersListFragment != null) {
                filtersListFragment.setListener(this)
                filtersListFragment.show(supportFragmentManager, filtersListFragment.tag)

            }
        }

        btn_editfilter.setOnClickListener {
            if (editImageFragment != null) {
                editImageFragment.setListener(this)
                editImageFragment.show(supportFragmentManager, editImageFragment!!.tag)

            }
        }

        btn_brush.setOnClickListener {
            if (brushFragment != null) {
                photoEditor.setBrushDrawingMode(true)
                brushFragment.setBrushListener(this)
                brushFragment.show(supportFragmentManager, brushFragment.tag)
            }
        }

        btn_emoji.setOnClickListener {
            if (emojiFragment != null) {
                emojiFragment.setEmojiListener(this)
                emojiFragment.show(supportFragmentManager, emojiFragment.tag)
            }
        }

        btn_text.setOnClickListener {

            if (addTextFragment != null) {
                addTextFragment.setAddTextListener(this)
                addTextFragment.show(supportFragmentManager, addTextFragment.tag)
            }
        }

        btn_frame.setOnClickListener {
            if (addFramFragment != null) {
                addFramFragment.setFrameListener(this)
                addFramFragment.show(supportFragmentManager, addFramFragment.tag)
            }
        }


        btn_crop.setOnClickListener {
            try {
                startCrop(imageSelectedUri)
            } catch (e: Exception) {

                Toast.makeText(this, "No Image Open", Toast.LENGTH_SHORT).show()

            }

        }
    }

    private fun startCrop(uri: Uri) {
        var destinatioUri = StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString()

        var ucCrop = UCrop.of(uri, Uri.fromFile(File(cacheDir, destinatioUri)))
        ucCrop.start(this)

    }


    /*
    * Filter Fragment Listener
    * */
    override fun onFilterSelected(filter: Filter) {
        // reset image controls
        resetControls()

        // applying the selected filter
        filteredImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)
        // preview filtered image
        image_preview!!.source.setImageBitmap(filter.processFilter(filteredImage))

        finalImage = filteredImage.copy(Bitmap.Config.ARGB_8888, true)
    }

    /*
   * Filter Fragment Listener
   *
   */


    override fun onBrightnessChanged(brightness: Int) {
        brightnessFinal = brightness
        val myFilter = Filter()
        myFilter.addSubFilter(BrightnessSubFilter(brightness))
        image_preview!!.source.setImageBitmap(myFilter.processFilter(finalImage.copy(Bitmap.Config.ARGB_8888, true)))
    }

    override fun onSaturationChanged(saturation: Float) {
        saturationFinal = saturation
        val myFilter = Filter()
        myFilter.addSubFilter(SaturationSubfilter(saturation))
        image_preview!!.source.setImageBitmap(myFilter.processFilter(finalImage.copy(Bitmap.Config.ARGB_8888, true)))
    }

    override fun onContrastChanged(contrast: Float) {
        contrastFinal = contrast
        val myFilter = Filter()
        myFilter.addSubFilter(ContrastSubFilter(contrast))
        image_preview!!.source.setImageBitmap(myFilter.processFilter(finalImage.copy(Bitmap.Config.ARGB_8888, true)))
    }

    override fun onEditStarted() {

    }

    override fun onEditCompleted() {
        // once the editing is done i.e seekbar is drag is completed,
        // apply the values on to filtered image
        val bitmap = filteredImage.copy(Bitmap.Config.ARGB_8888, true)

        val myFilter = Filter()
        myFilter.addSubFilter(BrightnessSubFilter(brightnessFinal))
        myFilter.addSubFilter(ContrastSubFilter(contrastFinal))
        myFilter.addSubFilter(SaturationSubfilter(saturationFinal))
        finalImage = myFilter.processFilter(bitmap)
    }

    /*
    * Brush Fragment Listeners*/
    override fun onBrushSizedChanged(size: Float) {

        photoEditor.brushSize = size


    }
    override fun onEraserSizeChanged(size: Float) {
       photoEditor.setBrushEraserSize(size)

    }


    override fun onBrushOpacityChanged(value: Int) {

        photoEditor.setOpacity(value)

    }

    override fun onBrushColorChanged(color: Int) {
        photoEditor.brushColor = color
    }


    override fun onBrushStateChanged(isEraser: Boolean) {
        if (isEraser)
            photoEditor.brushEraser()
        else
            photoEditor.setBrushDrawingMode(true)

    }

    /*
    * Emoji Fragment listeners
    * */
    override fun onSelectedEmoji(emoji: String) {
        photoEditor.addEmoji(emoji)
    }

    /*
    * AddText Fragment Listener
    * */
    override fun onAddTextListener(text: String, color: Int) {

        photoEditor.addText(text, color)

    }

    /*
   * AddFrame Fragment Listener
   * */

    override fun onFrameSelected(frame: Int) {
        var bitmap = BitmapFactory.decodeResource(resources, frame)

        photoEditor.addImage(bitmap)
    }

    /**
     * Resets image edit controls to normal when new filter
     * is selected
     */
    private fun resetControls() {
        if (editImageFragment != null) {
            editImageFragment!!.resetControls()
        }
        brightnessFinal = 0
        saturationFinal = 1.0f
        contrastFinal = 1.0f
    }


    // load the default image from assets on app launch
    private fun loadImage() {
        originalImage = BitmapUtils.getBitmapFromAssets(this, IMAGE_NAME, 300, 300)
        filteredImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)
        finalImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)
        image_preview!!.source.setImageBitmap(originalImage)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_open) {
            openImageFromGallery()
            return true
        }

        if (id == R.id.action_save) {
            saveImageToGallery()
            return true
        }

        if (id == R.id.action_share) {
            Toast.makeText(this, "You're awesome! :D", Toast.LENGTH_SHORT).show()
            val value = "http://play.google.com/store/apps/details?id=" + applicationContext.packageName + " \n\n  Try out this new photo editing app"
            val shareIntent = Intent(android.content.Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, value)
            startActivity(Intent.createChooser(shareIntent, "Share via..."))

            return true
        }

        if (id == R.id.action_rate) {
            Toast.makeText(this, "Thanks", Toast.LENGTH_SHORT).show()
            val goToMarket = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + applicationContext.packageName))

            try {
                startActivity(goToMarket)

            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + applicationContext.packageName)))
            }

            return true
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == SELECT_GALLERY_IMAGE) {

                if (data != null) {
                    imageSelectedUri = data.data
                }

                val bitmap = BitmapUtils.getBitmapFromGallery(this, data?.data!!, 800, 800)

                // clear bitmap memory
                originalImage!!.recycle()
                finalImage.recycle()
                finalImage.recycle()

                originalImage = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                filteredImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)
                finalImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)
                image_preview!!.source.setImageBitmap(originalImage)
                bitmap.recycle()

                // render selected image thumbnails
//            if(filtersListFragment!=null)
//            filtersListFragment.prepareThumbnail(originalImage)
            } else if (requestCode == UCrop.REQUEST_CROP) {

                handleCropResult(data)

            }

        } else if (requestCode == UCrop.RESULT_ERROR) {
            handleCropError(data)

        }
    }

    private fun handleCropError(data: Intent?) {
        var cropError = UCrop.getError(data!!)

        if (cropError != null) {
            Toast.makeText(this, cropError.message.toString(), Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(this, "Unexpected Error", Toast.LENGTH_SHORT).show()

        }
    }

    private fun handleCropResult(data: Intent?) {

        val resultUri = UCrop.getOutput(data!!)
        if (resultUri != null) {
            image_preview.source.setImageURI(resultUri)

        } else {
            Toast.makeText(this, "Can not retrieve crop image", Toast.LENGTH_SHORT).show()

        }
    }

    private fun openImageFromGallery() {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            val intent = Intent(Intent.ACTION_PICK)
                            intent.type = "image/*"
                            startActivityForResult(intent, SELECT_GALLERY_IMAGE)
                        } else {
                            Toast.makeText(applicationContext, "Permissions are not granted!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()
    }

    /*
    * saves image to camera gallery
    * */
    private fun saveImageToGallery() {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {


                            photoEditor.saveAsBitmap(object : OnSaveBitmap {
                                override fun onFailure(e: Exception?) {
                                    Toast.makeText(applicationContext, e!!.message, Toast.LENGTH_SHORT).show()
                                }

                                override fun onBitmapReady(saveBitmap: Bitmap?) {

                                    try {

                                        image_preview.source.setImageBitmap(saveBitmap)

                                        //  var path2=Environment.getExternalStorageDirectory().absolutePath+"/InstaFilters"

                                        // String root = Environment.getExternalStorageDirectory().getAbsolutePath();
                                        var fileDir = File(Environment.getExternalStorageDirectory(), "/InstaFilters")

                                            fileDir.mkdir()


                                        var filename = System.currentTimeMillis().toString() + "_instafilter.jpg"
                                        var filepath = File(fileDir.absolutePath, filename)


                                        var fos: FileOutputStream? = null
                                        try {

                                            fos = FileOutputStream(filepath)
                                            // Use the compress method on the BitMap object to write image to the OutputStream
                                            saveBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, fos)

                                            fos.flush()
                                            fos.close()

                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }

                                        if (!TextUtils.isEmpty(filepath.path)) {
                                            val snackbar = Snackbar
                                                    .make(coordinator_layout, "Image saved to gallery!", Snackbar.LENGTH_LONG)
                                                    .setAction("OPEN") {
                                                        openImage(filepath)
                                                    }

                                            snackbar.show()
                                        } else {
                                            val snackbar = Snackbar
                                                    .make(coordinator_layout!!, "Unable to save image!", Snackbar.LENGTH_LONG)

                                            snackbar.show()
                                        }
                                    } catch (e: Exception) {

                                    }
                                }

                            })

                        } else {
                            Toast.makeText(applicationContext, "Permissions are not granted!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()

    }

    // opening image in default image viewer app
    private fun openImage(path: File?) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        var uri= FileProvider.getUriForFile(this, this.applicationContext.packageName + ".provider", path!!)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(/*Uri.fromFile(path)*/uri, "image/*")
        startActivity(intent)

    }

    companion object {

        private val TAG = MainActivity::class.java.simpleName

        val IMAGE_NAME = "dog.jpg"

        val SELECT_GALLERY_IMAGE = 101

        lateinit var filteredImage: Bitmap

        // load native image filters library
        init {
            System.loadLibrary("NativeImageProcessor")
        }
    }
}
