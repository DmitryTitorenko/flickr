package com.example.flickr.view.activity

import android.os.Bundle
import com.example.flickr.R
import com.example.flickr.view.base.BaseActivity
import com.example.flickr.view.base.BaseView
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

interface IMainView : BaseView

class MainActivity : BaseActivity(), IMainView {

    @Inject
    lateinit var presenterProvider: Provider<MainPresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
