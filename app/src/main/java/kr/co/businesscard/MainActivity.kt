package kr.co.businesscard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.RealmChangeListener
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val businessCardAdapter: BusinessCardAdapter = BusinessCardAdapter()
    private val cardRealm: Realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        business_card_recycler.adapter = businessCardAdapter
        business_card_recycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        businessCardAdapter.setItems(cardRealm.where(BusinessCardItem::class.java).findAll())
        cardRealm.addChangeListener { businessCardAdapter.notifyDataSetChanged() }

        search_button.setOnClickListener {

        }

        register_button.setOnClickListener {
            moveToActivity(RegisterActivity::class.java)
        }

        delete_button.setOnClickListener {

        }
    }

    private fun moveToActivity(activity: Class<*>) {
        val intent = Intent(this, activity)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFindViewByIdCache()
        cardRealm.close()
    }
}
