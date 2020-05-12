package kr.co.businesscard

import io.realm.Realm
import io.realm.RealmConfiguration

class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        Realm.setDefaultConfiguration(
            RealmConfiguration.Builder()
                .build()
        )
    }
}