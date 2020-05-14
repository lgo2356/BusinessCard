package kr.co.businesscard

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class BusinessCardItem : RealmObject() {
    @PrimaryKey
    @Required
    internal var contact: String = ""

    @Required
    internal var photoUrl: String = ""

    @Required
    internal var name: String = ""
}
