package kr.co.businesscard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_delete.*
import kotlinx.android.synthetic.main.activity_delete.name_edit
import kotlinx.android.synthetic.main.activity_delete.submit_button
import kotlinx.android.synthetic.main.activity_search.*

class DeleteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete)

        submit_button.setOnClickListener {
            val nameStr: String? = name_edit.text.toString()

            if (nameStr?.isNotEmpty() == true) {
                deleteAll(nameStr)
                finish()
            }
        }
    }

    private fun deleteAll(name: String) {
        Realm.getDefaultInstance().use { realm ->
            val founds = realm.where<BusinessCardItem>()
                .equalTo("name", name)
                .findAll()
            realm.executeTransaction {
                founds.deleteAllFromRealm()
            }
        }
    }
}