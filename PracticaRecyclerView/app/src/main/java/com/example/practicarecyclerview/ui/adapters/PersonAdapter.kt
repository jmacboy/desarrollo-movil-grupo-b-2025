package com.example.practicarecyclerview.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practicarecyclerview.databinding.PersonItemViewBinding
import com.example.practicarecyclerview.models.Person

class PersonAdapter(
    private var data: List<Person>
) : RecyclerView.Adapter<PersonAdapter.ViewHolder>() {
    private var listener: PersonClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PersonItemViewBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, listener)
    }

    fun setData(newData: List<Person>) {
        this.data = newData
        notifyDataSetChanged()
    }

    fun setOnPersonClickListener(listener: PersonClickListener) {
        this.listener = listener
    }

//    En caso de trabajarlo con Lambda:
//    fun setOnPersonClickListener(clickEvent: (person: Person) -> Unit) {
//        this.listener = object : PersonClickListener {
//            override fun onPersonClick(person: Person) {
//                clickEvent(person)
//            }
//        }
//    }

    class ViewHolder(private val binding: PersonItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Person, listener: PersonClickListener?) {
            binding.lblItemFullName.text = item.getFullName()
            binding.lblItemPhone.text = item.phone
            binding.root.setOnClickListener {
                listener?.onPersonClick(item)
            }
        }
    }

    interface PersonClickListener {
        fun onPersonClick(person: Person)
    }
}