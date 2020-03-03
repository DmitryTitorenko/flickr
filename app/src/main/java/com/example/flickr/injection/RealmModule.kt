package com.example.flickr.injection

import dagger.Module
import dagger.Provides
import io.realm.Realm

@Module
class RealmModule {

    @Provides
    fun buildRealm(): Realm {
        return Realm.getDefaultInstance()
    }
}