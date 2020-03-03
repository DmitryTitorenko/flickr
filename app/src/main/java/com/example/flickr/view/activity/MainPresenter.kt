package com.example.flickr.view.activity

import com.example.flickr.view.base.BasePresenter
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor() : BasePresenter<IMainView>()
