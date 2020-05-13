package kr.co.businesscard

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import kotlinx.android.synthetic.main.layout_card.view.*

class BusinessCardAdapter : RecyclerView.Adapter<BusinessCardAdapter.BCViewHolder>() {

    var items: List<BusinessCardItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BCViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.layout_card, parent, false)
        return BCViewHolder(view)
    }

    override fun onBindViewHolder(holder: BCViewHolder, position: Int) {
        val cardItem = items[position]

        holder.nameText.text = cardItem.name
        holder.contactText.text = cardItem.contact
        holder.deleteButton.setOnClickListener {
            Log.d("TAG", "Position: $position item clicked!!")

            Realm.getDefaultInstance().use {
                it.executeTransaction {
                    cardItem.deleteFromRealm()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class BCViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.business_card_name
        val contactText: TextView = itemView.business_card_contact
        val deleteButton: Button = itemView.delete_button
    }
}