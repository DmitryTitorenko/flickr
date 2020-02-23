package com.example.flickr.view.images

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.flickr.model.ResponseImageList
import com.example.flickr.view.base.BaseView

interface IImagesView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun handleSearch(list: ResponseImageList)
}