package com.androiddevs.coronastats.ui.fragments

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
import com.androiddevs.coronastats.R
import com.androiddevs.coronastats.ui.*
import com.androiddevs.coronastats.ui.adapters.IndiaAdapter
import com.androiddevs.coronastats.ui.poko_classes.AllData
import com.androiddevs.coronastats.ui.poko_classes.RegionData
import kotlinx.android.synthetic.main.india_fragment.view.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


private lateinit var errorMsg: TextView
private lateinit var indiaLoading: ProgressBar
private lateinit var rv : RecyclerView
private lateinit var thisActivity : Activity
private lateinit var indiaAdapter : IndiaAdapter
private lateinit var indiaLayout : ConstraintLayout
private lateinit var indiaCard : CardView

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
        indiaLoading = view.findViewById(R.id.indiaProgressBar)
        rv = view.findViewById<RecyclerView>(R.id.listOfStatesInIndiaRV)
        thisActivity = activity!!
        errorMsg = view.findViewById(R.id.india_error_msg)
        indiaLayout = view.findViewById<ConstraintLayout>(R.id.india_layout)
        indiaCard = view.findViewById(R.id.india_text_card)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.india_top_menu, menu)

        val searchItem : MenuItem = menu.findItem(R.id.searchBar)
        val searchView : SearchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search for a state..."

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

//    var ind_call : Call<India>? = null
//    var allData : India? = null
//    var ind_state_api : indiaStateApi? = null
//    var ind_state_call : Call<List<IndiaStateData>>? = null
//    var allStates : List<IndiaStateData>? = null


//    "https://api.covid19india.org/"
//    "https://api.apify.com/v2/key-value-stores/toDWvRj1JpTXiM8FF/records/LATEST?disableRedirect=true"
//    "https://api.covidindiatracker.com/state_data.json"
//    "https://api.covidindiatracker.com/total.json"

    override fun doInBackground(vararg params: Void?): Void? {

        val okHttpClient = OkHttpClient.Builder()
            .writeTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .callTimeout(1, TimeUnit.MINUTES)
            .build()

        val retrofitInd: Retrofit =
            Retrofit.Builder()
                .baseUrl("https://api.apify.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

//        val retrofitState : Retrofit =
//            Retrofit.Builder()
//                .baseUrl("https://api.covidindiatracker.com/")
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()

        ind_api = retrofitInd.create(indiaApi::class.java)
        ind_call = ind_api?.getFullData()
//        ind_call = ind_api?.getIndiaData()

//        ind_state_api = retrofitState.create(indiaStateApi::class.java)
//        ind_state_call = ind_state_api?.getIndiaStateData()

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
                indiaCard.visibility = View.VISIBLE
                allData = response.body()
                allStates = getStates(allData, allStates)
                indiaLoading.visibility = View.GONE
                populateAllStatesList(allStatesList, allStates)
                displayInfo(allData)
                indiaAdapter = IndiaAdapter(allStatesList)
                rv.layoutManager = LinearLayoutManager(thisActivity.applicationContext)
                rv.adapter = IndiaAdapter(allStatesList)
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
//    @SuppressLint("SetTextI18n")
//    override fun onPostExecute(result: Void?) {
//        super.onPostExecute(result)
//
//        ind_call?.enqueue(object : Callback<India> {
//            override fun onFailure(call: Call<India>, t: Throwable) {
//                Log.e(TAG, "ERROR : " + t.message)
//                Log.e(TAG, "ERROR : $call")
//                indiaLoading.visibility = View.GONE
//                indiaAdapter = IndiaAdapter(mutableListOf())
//                errorMsg.visibility = View.VISIBLE
//                errorMsg.text = "No internet. Please retry."
//            }
//
//            override fun onResponse(call: Call<India>, response: Response<India>) {
//                allData = response.body()
//                displayInfo(allData)
//            }
//
//            private fun displayInfo(allData: India?) {
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
//
//        })
//
//        ind_state_call?.enqueue(object : Callback<List<IndiaStateData>> {
//            override fun onFailure(call: Call<List<IndiaStateData>>, t: Throwable) {
//                Log.e(TAG, "ERROR : " + t.message)
//                Log.e(TAG, "ERROR : $call")
//                indiaLoading.visibility = View.GONE
//                indiaAdapter = IndiaAdapter(mutableListOf())
//                errorMsg.visibility = View.VISIBLE
//                errorMsg.text = "No internet. Please retry."
//            }
//
//            override fun onResponse(
//                call: Call<List<IndiaStateData>>,
//                response: Response<List<IndiaStateData>>
//            ) {
//                allStates = response.body()
//                var sortedAllStates = allStates?.sortedBy { it?.state }
//                indiaAdapter = IndiaAdapter(sortedAllStates as MutableList<IndiaStateData>) //allStates as MutableList<IndiaStateData>)
//                rv.adapter = indiaAdapter
//                rv.layoutManager = LinearLayoutManager(thisActivity.applicationContext)
//                indiaLoading.visibility = View.GONE
//            }
//
//        })
//
//    }

}

