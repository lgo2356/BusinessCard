package kr.co.businesscard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import io.reactivex.rxjava3.disposables.Disposable
import io.realm.Realm
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val businessCardAdapter: BusinessCardAdapter = BusinessCardAdapter()
    private val cardRealm: Realm = Realm.getDefaultInstance()
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        business_card_recycler.adapter = businessCardAdapter
        business_card_recycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        cardRealm.addChangeListener { businessCardAdapter.notifyDataSetChanged() }
        businessCardAdapter.items = cardRealm.where(BusinessCardItem::class.java).findAll()

        search_button.setOnClickListener {
            moveToActivity(SearchActivity::class.java)
        }

        register_button.setOnClickListener {
            moveToActivity(RegisterActivity::class.java)
        }

        delete_button.setOnClickListener {
            moveToActivity(DeleteActivity::class.java)
        }

        url_button.setOnClickListener {
            moveToActivity(UrlActivity::class.java)
        }
    }

    private fun moveToActivity(activity: Class<*>) {
        val intent = Intent(this, activity)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFindViewByIdCache()
        disposable?.dispose()
        cardRealm.close()
    }
}
