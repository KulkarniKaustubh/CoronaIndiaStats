package com.ktdevs.coronastats.ui.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ktdevs.coronastats.R
import com.ktdevs.coronastats.ui.*
import com.ktdevs.coronastats.ui.adapters.IndiaAdapter
import kotlinx.android.synthetic.main.india_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private lateinit var indiaLayout : ConstraintLayout
private lateinit var errorMsg: TextView
//private lateinit var indiaText: TextView
private lateinit var indiaLoading: ProgressBar
private lateinit var rv : RecyclerView
private lateinit var thisActivity : Activity
private lateinit var indiaAdapter : IndiaAdapter
private  lateinit var indiaCard: CardView

class IndiaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater.inflate(R.layout.india_fragment, container, false)
        setHasOptionsMenu(true)
        allInfo().execute()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        indiaLayout = view.findViewById<ConstraintLayout>(R.id.india_layout)
        indiaLoading = view.findViewById(R.id.indiaProgressBar)
        rv = view.findViewById<RecyclerView>(R.id.listOfStatesInIndiaRV)
        thisActivity = activity!!
        indiaCard = view.findViewById(R.id.india_text_card)
        errorMsg = view.findViewById(R.id.india_error_msg)
//        indiaText = view.findViewById(R.id.india_text)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.india_top_menu, menu)

        val searchItem : MenuItem = menu.findItem(R.id.searchBar)
        val searchView : SearchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search for states..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                indiaAdapter.filter.filter(newText)
                return false
            }

        });
    }

}

private class allInfo : AsyncTask<Void, Void, Void>() {

    var indiaDetails: String = ""
    var ind_api: indiaApi? = null
    var ind_call: Call<AllData>? = null
    var allData: AllData? = null
    var allStates : MutableList<RegionData>? = mutableListOf()
    var allStatesList : MutableList<RegionData> = mutableListOf()

//    "https://api.covid19india.org/"
//    "https://api.apify.com/v2/key-value-stores/toDWvRj1JpTXiM8FF/records/LATEST?disableRedirect=true"

    override fun doInBackground(vararg params: Void?): Void? {

        val retrofit: Retrofit =
            Retrofit.Builder()
                .baseUrl("https://api.apify.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        ind_api = retrofit.create(indiaApi::class.java)
        ind_call = ind_api?.getFullData()

        return null
    }

    @SuppressLint("SetTextI18n")
    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)

        ind_call?.enqueue(object : Callback<AllData> {
            override fun onFailure(call: Call<AllData>, t: Throwable) {
                Log.e(TAG, "ERROR : " + t.message)
                Log.e(TAG, "ERROR : $call")
                indiaLoading.visibility = View.GONE
                indiaAdapter = IndiaAdapter(mutableListOf())
                errorMsg.visibility = View.VISIBLE
                errorMsg.text = "No internet. Please retry."
            }

            @SuppressLint("WrongConstant")
            override fun onResponse(call: Call<AllData>, response: Response<AllData>) {
                Log.d(TAG, "code is : " + response.code().toString())
                allData = response.body()
                indiaCard.visibility = View.VISIBLE
                allStates = getStates(allData, allStates)
                indiaLoading.visibility = View.GONE
                populateAllStatesList(allStatesList, allStates)
//                displayInfo(allData)
                displayInfo(allData)
                indiaAdapter = IndiaAdapter(allStatesList)
                rv.adapter = indiaAdapter
                rv.layoutManager = LinearLayoutManager(thisActivity.applicationContext)
//                rv.layoutManager = LinearLayoutManager(thisActivity,LinearLayout.VERTICAL,false)
            }


            private fun populateAllStatesList(
                allStatesList: MutableList<RegionData>,
                allStates: MutableList<RegionData>?
            ) {
                allStatesList.addAll(allStates!!)
            }

            private fun displayInfo(allData: AllData?) {
                allData?.apply {
                    indiaLayout.india_confirmed.text = "Confirmed: " + confirmed
                    indiaLayout.india_active.text = "Active: " + active
                    indiaLayout.india_deceased.text = "Deceased: " + deaths
                    indiaLayout.india_recovered.text = "Recovered: " + recovered

                }
                indiaLayout.india_text.text = "INDIA"
            }
//            private fun displayInfo(allData: AllData?) {
//                allData?.apply {
//                    indiaDetails =
//                        "India Data" + "\n" +
//                                "Confirmed: " + confirmed + "\n" +
//                                "Active: " + active + "\n" +
//                                "Deceased: " + deaths + "\n" +
//                                "Recovered: " + recovered
//                }
//                indiaText.text = indiaDetails
//            }

            private fun getStates(
                allData: AllData?,
                allStates: MutableList<RegionData>?
            ): MutableList<RegionData>? {
                for (i in 0 until allData?.regionData?.size!!) {
                    allStates?.add(allData?.regionData?.get(i))
                }
                return allStates
            }

        })

    }


}

