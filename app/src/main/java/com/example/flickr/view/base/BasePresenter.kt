package com.example.flickr.view.base

import moxy.MvpPresenter

abstract class BasePresenter<TPresenterView : BaseView> : MvpPresenter<TPresenterView>() {

    protected fun handleError(e: Throwable) {
        e.printStackTrace()
        viewState.showMessage(e.message.toString())
    }
}
