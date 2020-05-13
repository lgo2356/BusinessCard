package kr.co.businesscard

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.realm.Realm
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_search.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.lang.reflect.Field

class SearchActivity : AppCompatActivity() {
    private var disposable: Disposable? = null
    private val fieldNames: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val fields: Array<Field> = TestData::class.java.declaredFields
        for (field in fields) {
            fieldNames.add(field.name)
        }

        val adapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, fieldNames)
        text_list.adapter = adapter

        disposable = Observable.just(OkHttpClient())
            .subscribeOn(Schedulers.io())
            .subscribe {
                val url = "https://api.github.com/"
                val request: Request = Request.Builder().url(url).build()
                val response: Response = it.newCall(request).execute()
                val resultJson: String? = response.body?.string()
                val testJson: TestData = Gson().fromJson(resultJson, TestData::class.java)

                Realm.getDefaultInstance().use { realm1 ->
                    realm1.executeTransaction { realm2 ->
                        realm2.copyToRealmOrUpdate(testJson)
                    }
                }
            }

        search_button.setOnClickListener {
            Realm.getDefaultInstance().use {
                val key: Long? = 0
                val results = it.where(TestData::class.java).equalTo("key", key).findAll()

                results[0]?.authorizationsUrl

                val testData: TestData = TestData()

//                for (result in results) {
//                    result
//                    Log.d("TAG", result)
//                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFindViewByIdCache()
        disposable?.dispose()
    }
}
