package kr.co.businesscard

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.url_recycler
import kotlinx.android.synthetic.main.activity_url.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class UrlActivity : AppCompatActivity() {
    private var disposable: Disposable? = null
    private val fieldNames: ArrayList<String> = ArrayList()
    private val urls: ArrayList<String> = ArrayList()
    private val urlAdapter: UrlAdapter = UrlAdapter()
    private val urlRealm: Realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_url)

        url_recycler.adapter = urlAdapter
        url_recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        urlRealm.addChangeListener { urlAdapter.notifyDataSetChanged() }
        urlAdapter.items = urlRealm.where(UrlItem::class.java).findAll()

        disposable = Observable.create<OkHttpClient> { subscriber ->
            Log.d("OkHttp", "Start Rx")
            subscriber.onNext(OkHttpClient())
        }
            .subscribeOn(Schedulers.io())
            .subscribe(
                { client ->
                    val url = "https://api.github.com/"
                    val request: Request = Request.Builder().url(url).build()
                    val response: Response = client.newCall(request).execute()
                    val resultJson: String? = response.body?.string()
                    val testJson: UrlItem = Gson().fromJson(resultJson, UrlItem::class.java)

                    Realm.getDefaultInstance().use { realm1 ->
                        realm1.executeTransaction { realm2 ->
                            realm2.copyToRealmOrUpdate(testJson)
                        }
                    }
                },
                {
                    Log.d("OkHttp", "Error: ${it.printStackTrace()}")
                }
            )

        refresh_button.setOnClickListener {
            Realm.getDefaultInstance().use { realm ->
                realm.executeTransaction {
                    val urlItem: UrlItem = UrlItem()
                    urlItem.currentUserUrl = "abc"
                    realm.copyToRealmOrUpdate(urlItem)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFindViewByIdCache()
        disposable?.dispose()
    }
}
