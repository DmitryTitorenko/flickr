package com.example.flickr.model

import io.realm.RealmObject

open class Item(var imagePath: String = "",
                var textSearch: String = "") : RealmObject() {

}