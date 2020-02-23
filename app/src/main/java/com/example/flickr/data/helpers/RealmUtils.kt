package com.example.flickr.data.helpers

import android.util.Log
import com.example.flickr.model.Item
import io.realm.Realm
import io.realm.kotlin.where

object RealmUtils {
    fun insertItemToDB(realm: Realm, savedImagePath: String, searchText: String) {
        realm.beginTransaction()
        realm.copyToRealm(
            Item(
                savedImagePath,
                searchText
            )
        )
        realm.commitTransaction()
        val itemsInDB2: List<Item> = realm.where<Item>().findAll()
        Log.d("REALM_TEST", "itemsInDB.size2 " + itemsInDB2.size)
    }

    fun getAllItems(realm: Realm): List<Item> {
        val itemsInDB2: List<Item> = realm.where<Item>().findAll()
        itemsInDB2.forEach {
            Log.d(
                "historySearchModel.items.", "imagePath: " + it.imagePath + System.lineSeparator() +
                        "textSearch: " + it.textSearch
            )
        }
        return itemsInDB2
    }
}
