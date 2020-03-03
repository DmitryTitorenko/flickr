package com.example.flickr.view.images

import android.content.Context
import com.example.flickr.data.helpers.GlideUtils
import com.example.flickr.data.helpers.RealmUtils
import com.example.flickr.data.helpers.URLUtils
import com.example.flickr.data.repository.Repo
import com.example.flickr.view.base.BasePresenter
import io.realm.Realm
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class ImagesPresenter @Inject constructor(
    private val repo: Repo,
    private val realm: Realm,
    private val context: Context
) : BasePresenter<IImagesView>() {

    override fun onFirstViewAttach() {
        viewState.getList(RealmUtils.getAllItems(realm))
        super.onFirstViewAttach()
    }

    fun search(text: String) =
        presenterScope.launch {
            val response = repo.search(text)
            val imageURL = URLUtils.createURLForImage(response)
            GlideUtils.loadImage(context, imageURL) { path -> insertItemToDB(path, text) }
        }

    private fun insertItemToDB(savedImagePath: String, searchText: String) {
        RealmUtils.insertItemToDB(realm, savedImagePath, searchText)
        val newList = RealmUtils.getAllItems(realm)
        viewState.updateList(newList)
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
