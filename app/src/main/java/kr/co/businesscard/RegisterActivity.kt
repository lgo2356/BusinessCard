package kr.co.businesscard

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private val subject: PublishSubject<View> = PublishSubject.create()
    private var disposable: Disposable? = null

    init {
        disposable = subject
            .map { BusinessCardItem() }
            .map {
                try {
                    val editName = name_edit.text.toString()
                    val editContact = contact_edit.text.toString()

                    it.apply {
                        if (editName != "" || editContact != "") {
                            name = editName
                            contact = editContact
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()

                    it.apply {
                        name = ""
                        contact = ""
                    }
                    throw e
                }
            }
            .subscribe {
                if (it.name != "" && it.contact != "") {
                    val controller = RealmController()
                    controller.insertCardInfo(it)
                }
                finish()
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val item: BusinessCardItem? = BusinessCardItem()

        if (item?.name?.isNotEmpty() == true && item.contact != "") {
            val controller = RealmController()
            controller.insertCardInfo(item)
        }

        submit_button.setOnClickListener {
            subject.onNext(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
        subject.onComplete()
//        subject.onError(NullPointerException())
    }
}