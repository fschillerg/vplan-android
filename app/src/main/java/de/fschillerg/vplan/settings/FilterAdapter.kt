package de.fschillerg.vplan.settings

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import de.fschillerg.vplan.R
import de.fschillerg.vplan.preferences.Preferences

class FilterAdapter(private val preferences: Preferences) : RecyclerView.Adapter<FilterAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.filter_text)
        val remove: ImageView = itemView.findViewById(R.id.filter_remove)
    }

    override fun onCreateViewHolder(view: ViewGroup, i: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(view.context).inflate(R.layout.fragment_filter, view, false))
    }

    override fun onBindViewHolder(view: ListViewHolder, i: Int) {
        val text = if (i < preferences.forms.size) {
            preferences.forms.elementAt(i)
        } else {
            preferences.teachers.elementAt(i - preferences.forms.size)
        }
        view.text.text = text
        view.remove.setOnClickListener {
            if (i < preferences.forms.size) {
                preferences.forms = preferences.forms.minusElement(text)
            } else {
                preferences.teachers = preferences.teachers.minusElement(text)
            }
            this.notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return preferences.forms.size + preferences.teachers.size
    }
}
