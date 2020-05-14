package kr.co.businesscard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.realm.Realm
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.IOException
import java.util.*

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
        businessCardAdapter.items =
            cardRealm.where(BusinessCardItem::class.java).findAll()  // 명함 테이블 전체 검색 결과를 set

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

        test_button.setOnClickListener {
            // TODO
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitAPI = retrofit.create(RetrofitAPI::class.java)

            val retrofitAPI = setRetrofitInit(retrofit)

            retrofitAPI.getUrlList()?.enqueue(object Callback<String>)

            callUrlList(retrofitAPI)
        }
    }

    private fun setRetrofitInit(retrofit: Retrofit): RetrofitAPI {
        return retrofit.create(RetrofitAPI::class.java)
    }

    private fun callUrlList(retrofitAPI: RetrofitAPI) {
        val urlList: Call<String> = retrofitAPI.getUrlList()
        urlList.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                val result: String? = response.body()
                Log.d("Retrofit", result!!)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                t.printStackTrace()
            }
        })
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
