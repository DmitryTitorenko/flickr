package com.example.flickr.view.images

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flickr.R
import com.example.flickr.model.Item
import com.example.flickr.view.base.BaseFragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.images_fragment.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider


class ImagesFragment : BaseFragment(), IImagesView {

    @Inject
    lateinit var presenterProvider: Provider<ImagesPresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    private lateinit var itemsList: List<Item>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.images_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions()
        } else {
            setListener()
            initAdapter()
        }
    }

    override fun getList(list: List<Item>) {
        itemsList = list
    }

    override fun updateList(list: List<Item>) {
        itemsList = list
        updateAdapter()
    }

    private fun setListener() {
        etSearchField.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                val searchText = etSearchField.text.toString()
                presenter.search(searchText)
            }
            return@setOnEditorActionListener false
        }
    }

    private fun initAdapter() {
        rvViewImages.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        rvViewImages.layoutManager = linearLayoutManager
        val itemAdapter = ImagesAdapter(itemsList)
        rvViewImages.adapter = itemAdapter
    }

    private fun updateAdapter() {
        val itemAdapter = ImagesAdapter(itemsList)
        itemAdapter.notifyDataSetChanged()
        rvViewImages.adapter = itemAdapter
    }

    private fun requestPermissions() {
        Dexter.withActivity(requireActivity())
            .withPermissions(
                listOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    setListener()
                    initAdapter()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>, token: PermissionToken?
                ) {
                }
            })
            .check()
    }
}
