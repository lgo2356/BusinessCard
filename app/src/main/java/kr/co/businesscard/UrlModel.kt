package kr.co.businesscard

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UrlModel : RealmObject() {
    @PrimaryKey
    var key: String? = null

    var value: String? = null
}
