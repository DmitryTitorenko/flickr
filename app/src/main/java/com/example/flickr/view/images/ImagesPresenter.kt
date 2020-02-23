package com.example.flickr.view.images

import com.arellomobile.mvp.InjectViewState
import com.example.flickr.data.repository.Repo
import com.example.flickr.view.base.BasePresenter
import kotlinx.coroutines.launch
import javax.inject.Inject

@InjectViewState
class ImagesPresenter @Inject constructor(private val repo: Repo) :
    BasePresenter<IImagesView>() {

    fun search(text: String) = launch {
        val response = repo.search(text)
        viewState.handleSearch(response)
    }
}
