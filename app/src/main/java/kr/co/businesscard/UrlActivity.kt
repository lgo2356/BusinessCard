package kr.co.businesscard

import android.accounts.NetworkErrorException
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.realm.Realm
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_search.url_recycler
import kotlinx.android.synthetic.main.activity_url.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


class UrlActivity : AppCompatActivity() {
    private var disposable: Disposable? = null
    private val urlAdapter: UrlAdapter = UrlAdapter()
    private val urlRealm: Realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_url)

        url_recycler.adapter = urlAdapter
        url_recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        urlRealm.addChangeListener { urlAdapter.notifyDataSetChanged() }
        urlAdapter.items = urlRealm.where(UrlModel::class.java).findAll()  // 검색 결과 set


        refresh_button.setOnClickListener {
            val urlModel = UrlModel()
            urlModel.key = "current_user_url"
            urlModel.value = "https://github.com/lgo2356/BusinessCard/"

            Realm.getDefaultInstance().use { realm ->
                realm.executeTransaction {
                    realm.copyToRealmOrUpdate(urlModel)
                }
            }

        }

        request()
    }

    private fun request() {
        loading_progressbar.visibility = View.VISIBLE

        disposable = Observable
            .create<String> { subscriber ->
                val result = Api.requestApi()

                if (result == null) {
                    subscriber.onError(NullPointerException("api response is null"))
                } else {
                    subscriber.onNext(result)
                    subscriber.onComplete()
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { json ->
                    //success
                    Log.d("Success", "start!!")
                    val jsonObj: JsonObject = JsonParser.parseString(json).asJsonObject
                    val keys: Set<String> = jsonObj.keySet()
                    val urlModel = UrlModel()
                    Realm.getDefaultInstance().use { realm ->
                        realm.executeTransaction {
                            for (key in keys) {
                                urlModel.key = key
                                urlModel.value = jsonObj[key].asString

                                realm.copyToRealmOrUpdate(urlModel)
                            }
                        }
                    }
                },
                {
                    //Error.
                    loading_progressbar.visibility = View.GONE
                    Log.d("OkHttp", "Error: ${it.printStackTrace()}")
                },
                {
                    //Complete.
                    loading_progressbar.visibility = View.GONE
                }
            )
    }


    override fun onDestroy() {
        super.onDestroy()
        clearFindViewByIdCache()
        disposable?.dispose()
    }
}
