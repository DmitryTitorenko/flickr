package com.example.flickr.view.images

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.flickr.R
import com.example.flickr.data.helpers.RealmUtils
import com.example.flickr.data.helpers.SaveImageToStorageDir
import com.example.flickr.data.helpers.URLUtils
import com.example.flickr.model.Item
import com.example.flickr.model.ResponseImageList
import com.example.flickr.view.base.BaseFragment
import dagger.Lazy
import io.realm.Realm
import kotlinx.android.synthetic.main.images_fragment.*
import javax.inject.Inject


class ImagesFragment : BaseFragment(), IImagesView {

    @Inject
    lateinit var daggerPresenter: Lazy<ImagesPresenter>

    @InjectPresenter
    lateinit var presenter: ImagesPresenter

    @ProvidePresenter
    fun providePresenter(): ImagesPresenter = daggerPresenter.get()

    companion object {
        private const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 111
        private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 222
    }

    private lateinit var realm: Realm
    private lateinit var searchText: String
    private lateinit var itemsList: List<Item>

    private var permReadStorage = false
    private var permWriteStorage = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.images_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO request multiple permissions
        if (requestPermission1() && requestPermission2()) {
            setListener()
            getItemList()
            initAdapter()
        }
    }

    override fun handleSearch(list: ResponseImageList) {
        val imageURL = URLUtils.createURLForImage(list)
        loadImage(imageURL)
    }

    private fun setListener() {
        etSearchField.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                Log.d("IME_ACTION_DONE", "EditorInfo.IME_ACTION_DONE")
                searchText = etSearchField.text.toString()
                presenter.search(searchText)
            }
            return@setOnEditorActionListener false
        }
    }

    private fun loadImage(url: String) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val savedImagePath = SaveImageToStorageDir.save(requireContext(), resource)
                    RealmUtils.insertItemToDB(realm, savedImagePath, searchText)
                    updateAdapter()
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    private fun getItemList() {

        // Get a Realm instance for this thread
        realm = Realm.getDefaultInstance()
        itemsList = RealmUtils.getAllItems(realm)
    }

    private fun initAdapter() {
        rvViewImages.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        rvViewImages.layoutManager = linearLayoutManager
        val itemAdapter = ImagesAdapter(itemsList)
        rvViewImages.adapter = itemAdapter
    }

    private fun updateAdapter() {
        itemsList = RealmUtils.getAllItems(realm)
        val itemAdapter = ImagesAdapter(itemsList)
        itemAdapter.notifyDataSetChanged()
        rvViewImages.adapter = itemAdapter
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    permReadStorage = true
                    if (requestPermission2() && requestPermission2()) {
                        setListener()
                        getItemList()
                        initAdapter()
                    }
                }
                return
            }

            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    permWriteStorage = true
                    if (requestPermission2() && requestPermission2()) {
                        setListener()
                        getItemList()
                        initAdapter()
                    }
                }
                return
            }
        }
    }

    private fun requestPermission1(): Boolean {
        var isGranted = false
        context?.let {
            if (ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                it.activity()?.let {
                    // Permission is not granted
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            it,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    ) {
                    } else {
                        requestPermissions(
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                        )
                    }
                }
            } else {
                isGranted = true
            }
        }
        return isGranted
    }

    private fun requestPermission2(): Boolean {
        var isGranted = false
        context?.let {
            if (ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                it.activity()?.let {
                    // Permission is not granted
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            it,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    ) {
                    } else {
                        requestPermissions(
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                        )
                    }
                }
            } else {
                isGranted = true
            }
        }
        return isGranted
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}

tailrec fun Context.activity(): Activity? = when {
    this is Activity -> this
    else -> (this as? ContextWrapper)?.baseContext?.activity()
}
