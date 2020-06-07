package com.androiddevs.confused.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.confused.R
import kotlinx.android.synthetic.main.list_of_states.view.*

class IndiaAdapter(
    var states: MutableList<RegionData>
): RecyclerView.Adapter<IndiaAdapter.StatesViewHolder>(), Filterable{

    var statesCopy : MutableList<RegionData> = ArrayList(states)

    inner class StatesViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_of_states,parent,false)
        return StatesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return states.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: StatesViewHolder, position: Int) {
        Log.d("STATES COPY", "states copy is : $statesCopy")
        holder.itemView.apply{
            stateName.text = states[position].state
            confirmed.text = (states[position].confirmed).toString()
            active.text = (states[position].active).toString()
            deceased.text = (states[position].deceased).toString()
            recovered.text = (states[position].recovered).toString()

        }
    }

    private var stateFilter = object : Filter() {
        @SuppressLint("DefaultLocale")
        override fun performFiltering(constraint: CharSequence?): FilterResults {

            val charSearch = constraint.toString().toLowerCase().trim()
            var filteredList :MutableList<RegionData> = ArrayList<RegionData>()

            if (charSearch.isEmpty()) {
                filteredList.addAll(statesCopy)
            } else {
                for (item in statesCopy) {
                    if (item.state.toLowerCase().contains(charSearch)) {
                        filteredList.add(item)
                    }
                }
            }

            Log.d("FILTERED LIST", "filtered list is : $filteredList")
            val results : FilterResults = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            states.clear()
            states.addAll(results?.values as MutableList<RegionData>)
            notifyDataSetChanged()
        }

    };

    override fun getFilter(): Filter {
        return stateFilter
    }

}