package com.example.flickr.view.images

import com.example.flickr.model.Item
import com.example.flickr.view.base.BaseView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

interface IImagesView : BaseView {

    @AddToEndSingle
    fun getList(list: List<Item>)

    @OneExecution
    fun updateList(list: List<Item>)
}