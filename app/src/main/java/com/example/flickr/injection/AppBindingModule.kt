package com.example.flickr.injection
import com.example.flickr.view.activity.MainActivity
import com.example.flickr.view.images.ImagesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AppBindingModule {

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun bindImagesFragment(): ImagesFragment
}
