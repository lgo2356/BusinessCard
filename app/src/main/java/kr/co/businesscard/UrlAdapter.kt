package kr.co.businesscard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_url_recycler.view.*

class UrlAdapter : RecyclerView.Adapter<UrlAdapter.ViewHolder>() {
    var items: List<UrlItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.layout_url_recycler, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val urlItem = items[position]

        holder.urlNameText.text = "Current user url"
        holder.urlValueText.text = urlItem.currentUserUrl
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val urlNameText: TextView = itemView.text_url_name
        val urlValueText: TextView = itemView.text_url_value
    }
}
