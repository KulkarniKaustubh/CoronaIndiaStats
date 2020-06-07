package com.androiddevs.confused.ui.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.confused.R
import com.androiddevs.confused.ui.*
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


private lateinit var errorMsg: TextView
private lateinit var stateLoading: ProgressBar
private lateinit var rv : RecyclerView
private lateinit var thisActivity: Activity
private var stateAdapter: StateAdapter? = null

class StateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.state_fragment, container, false)
        stateInfo().execute()
        setHasOptionsMenu(true)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        stateLoading = view.findViewById(R.id.stateProgressBar)
        rv = view.findViewById<RecyclerView>(R.id.listOfDistrictsInStatesRV)
        thisActivity = activity!!
        errorMsg = view.findViewById(R.id.state_error_msg)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.india_top_menu, menu)

        val searchItem : MenuItem = menu.findItem(R.id.searchBar)
        val searchView : SearchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("HERE", "we are here")
                stateAdapter?.filter?.filter(newText)
                return false
            }

        });
    }
}

private class stateInfo : AsyncTask<Void, Void, Void>() {

    lateinit var singleParsed: String
    var finalParsed: String = ""
    var districts : MutableList<List<DistrictData>>? = mutableListOf()
    var allStates: List<State>? = null
    var api : stateApi? = null
    var call : Call<List<State>>? = null

    override fun doInBackground(vararg params: Void?): Void? {

        val okHttpClient = OkHttpClient.Builder()
            .writeTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .callTimeout(1, TimeUnit.MINUTES)
            .build()

        val retrofit: Retrofit =
            Retrofit.Builder()
                .baseUrl("https://api.covid19india.org/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        api = retrofit.create(stateApi::class.java)
        call = api?.getState()

        return null
    }

    @SuppressLint("SetTextI18n")
    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)

        call?.enqueue(object : Callback<List<State>> {
            override fun onFailure(call: Call<List<State>>, t: Throwable) {
                Log.e(TAG, "ERROR : " + t.message)
                stateLoading.visibility = View.GONE
                stateAdapter = StateAdapter(mutableListOf())
                errorMsg.visibility = View.VISIBLE
                errorMsg.text = "No internet. Please retry."
            }

            override fun onResponse(call: Call<List<State>>, response: Response<List<State>>) {
                Log.d(TAG, "code is : " + response.code().toString())
                allStates = response.body()
                districts = getDistrictData(allStates, districts)
                Log.d(TAG, "districts is : $districts")
                Log.d(TAG, "states is : $allStates")
                stateLoading.visibility = View.GONE
                stateAdapter = StateAdapter(allStates!! as MutableList<State>)
                rv.adapter = stateAdapter
                rv.layoutManager = LinearLayoutManager(thisActivity.applicationContext)
//                displayInfo(allStates, districts)
            }

            private fun displayInfo(
                allStates: List<State>?,
                districts: MutableList<List<DistrictData>>?
            ) {
                for (i in 0 until allStates?.size!!) {
                    singleParsed = "State: " + allStates?.get(i)?.state + "\n\n"
                    for (j in 0 until districts?.get(i)?.size!!) {
                        singleParsed += "District: " + districts?.get(i)?.get(j).district + "\n" +
                                "Active: " + districts?.get(i)?.get(j).active + "\n" +
                                "Confirmed: " + districts?.get(i)?.get(j).confirmed + "\n" +
                                "Deceased: " + districts?.get(i)?.get(j).deceased + "\n" +
                                "Recovered: " + districts?.get(i)?.get(j).recovered + "\n\n"
                    }
                    finalParsed += singleParsed + "\n"
                }
//                stateText.text = finalParsed
            }

            private fun getDistrictData(
                allStates: List<State>?,
                districts: MutableList<List<DistrictData>>?
            ): MutableList<List<DistrictData>>? {
                for (i in 0 until allStates?.size!!) {
                    districts?.add(allStates?.get(i).districtData)
                }
                return districts
            }
        })
    }
}