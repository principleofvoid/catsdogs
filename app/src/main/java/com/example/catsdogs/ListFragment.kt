package com.example.catsdogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.view_list_item.view.*

private const val ARG_DATA_LIST = "ARG_DATA_LIST"
private const val STATE_LAYOUT_MANAGER = "STATE_LAYOUT_MANAGER"

fun createListFragment(dataList: List<CatDogItem>): ListFragment {
    val fragment = ListFragment()

    fragment.arguments = Bundle().apply {
        putParcelableArrayList(ARG_DATA_LIST, ArrayList(dataList))
    }

    return fragment
}

class ListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelableArrayList<CatDogItem>(ARG_DATA_LIST)?.let { data ->
            recycler_list.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = ListAdapter(data) { position, data ->
                    startDetailsActivity(
                        context =context,
                        position = position,
                        imageUrl = data.imageUrl,
                        text = data.title
                    )
                }
            }
        }

        savedInstanceState?.let { state ->
            recycler_list.layoutManager?.onRestoreInstanceState(state.getParcelable(STATE_LAYOUT_MANAGER))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(STATE_LAYOUT_MANAGER, recycler_list.layoutManager?.onSaveInstanceState())
        super.onSaveInstanceState(outState)
    }

    private class ListAdapter(private val data: List<CatDogItem>, val itemClickListener: ((Int, CatDogItem) -> Unit)? = null)
        : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private class ListViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.view_list_item, parent, false)
            return ListViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder.itemView.apply {
                val number = position + 1
                item_number.text = number.toString()
                item_text.text = data[position].title
                Picasso.with(context).load(data[position].imageUrl).into(item_image)
                setOnClickListener {
                    itemClickListener?.invoke(number, data[position])
                }
            }
        }

        override fun getItemCount() = data.size
    }
}