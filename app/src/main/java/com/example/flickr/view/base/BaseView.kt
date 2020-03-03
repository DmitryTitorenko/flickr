package com.example.flickr.view.base

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface BaseView : MvpView {

    @AddToEndSingle
    fun showMessage(message: String)
}
