package kr.co.businesscard

import io.realm.Realm
import io.realm.RealmObject


inline fun <reified T : RealmObject> T.deleteFromRealmWithTransaction() {
    realm.executeTransaction { deleteFromRealm() }
}

class RealmController {
    fun registerBusinessCard(info: BusinessCardItem) {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction {
                it.copyToRealm(info)
            }
        }
    }

//    fun deleteBusinessCardItem(key: String) {
//        Realm.getDefaultInstance().use { r ->
//            val found = r.where<BusinessCardItem>().equalTo("key", key).findFirst()
//            if (found != null) {
//                r.executeTransaction {
//                    found.deleteFromRealm()
//                }
//            }
//        }
//    }
//
//    fun clearBusinessCardItem() {
//        Realm.getDefaultInstance().use {
//            it.executeTransaction { r ->
//                r.where<BusinessCardItem>().findAll().deleteAllFromRealm()
//            }
//        }
//    }
//
////    fun insertIntoTable(change: Realm.() -> Unit) {
////        Realm.getDefaultInstance().use {
////            it.change()
////        }
////    }
//
//    inline fun <reified T : RealmObject> newInstance(onCreate: Realm.(T) -> Unit): T {
//        val item = T::class.java.getConstructor().newInstance()
//        return Realm.getDefaultInstance().use {
//            val managedItem = it.copyToRealmOrUpdate(item)
//            it.onCreate(managedItem)
//            it.copyFromRealm(managedItem)
//        }
//    }
}
