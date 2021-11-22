package com.syed.bookhub.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.syed.bookhub.R
import com.syed.bookhub.adapter.DashboardRecyclerAdapter
import com.syed.bookhub.model.Book
import com.syed.bookhub.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

//import android.support.v7.app.AlertDialog;


/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerAdapter: DashboardRecyclerAdapter
    lateinit var btnCheckInternet: Button
    lateinit var progressbarLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    //    var bookInfoList= arrayListOf<String>(
//            "P.S I Love You", " The Great Gatsby"," Anna Karenina","Madam Bovary","War and Peace","Lolita","Middlemarch","The Adventures of HuckleBerry Finn"," Moby-Dick","The Lord of the Rings"
//    )
    val bookInfoList = arrayListOf<Book>()

    var ratingComparator = Comparator<Book>{book1,book2 ->
    if(book1.BookRating.compareTo(book2.BookRating, true)==0){
         book1.BookName.compareTo(book2.BookName, true)
    }
        else{ book1.BookRating.compareTo(book2.BookRating, true)}

    }
    //    (Book("P.S I Love You","Cecelia Ahern","Rs.499","4.5",R.drawable.ps_ily),
//            Book("The Great Gatsby","F.Scott","Rs.499","4.5",R.drawable.great_gatsby),
//                    Book("Anna Karenina","Leo Tolstoy","Rs.499","4.5",R.drawable.anna_kare),
//    Book("Madamme Bovary","Gustave","Rs.499","4.5",R.drawable.madame),
//    Book("War and Peace","Leo Tolstoy","Rs.499","4.5",R.drawable.war_and_peace),
//            Book("Lolita","Vladamir Nabokov","Rs.499","4.5",R.drawable.lolita),
//            Book("Middlemarch","George Elliot","Rs.499","4.3",R.drawable.middlemarch),
//                    Book("The Adventures of Huckleberry Finn","Mark Twain","Rs.499","4.5",R.drawable.adventures_finn),
//            Book("Moby-Dick","Herman","Rs.499","4.9",R.drawable.moby_dick),
//                    Book("The Lord of the Rings","J.R.R Tolkien","Rs.499","4.5",R.drawable.lord_of_rings))

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        setHasOptionsMenu(true)
        recyclerDashboard =
                view.findViewById(R.id.recyclerDashboard) // as onCreateView method requires a view to survive then everything we will add in this method must be added in the view that this fragment will display and this View is then inflated inside the fragment
        layoutManager = LinearLayoutManager(activity)
        recyclerAdapter = DashboardRecyclerAdapter(activity as Context, bookInfoList)
        btnCheckInternet = view.findViewById(R.id.btnCheckInternet)

        progressbarLayout = view.findViewById(R.id.progressbarLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressbarLayout.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
        btnCheckInternet.setOnClickListener {


            if (ConnectionManager().checkConnectivity(activity as Context)) {

                val dailog = AlertDialog.Builder(activity as Context)
                dailog.setTitle("Success")
                dailog.setMessage("Internet Connection Found")
                dailog.setPositiveButton("OK") { text, listener ->

                }
                dailog.setNegativeButton("Cancel") { text, listener ->

                }
                dailog.create()
                dailog.show()
            } else {
                val dailog = AlertDialog.Builder(activity as Context)
                dailog.setTitle("Error")
                dailog.setMessage("Internet Connection Not Found!")
                dailog.setPositiveButton("OK") { text, listener ->

                }
                dailog.setNegativeButton("Cancel") { text, listener ->

                }
                dailog.create()
                dailog.show()
            }
        }


        // Associating the layout manager and recyclerAdapter with the RecyclerView

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v1/book/fetch_books/"
        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest =
                    object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                        println("Response from the server is $it")

                        try {
                            progressbarLayout.visibility = View.GONE
                            progressBar.visibility = View.GONE
                            val success = it.getBoolean("success")// key name is success

                            if (success) {

                                val data =
                                        it.getJSONArray("data") // data variable is  the json array which is data
                                for (i in 0 until data.length()) {
                                    val bookJsonObject = data.getJSONObject(i)
                                    val bookObject = Book(
                                            bookJsonObject.getString("book_id"),
                                            bookJsonObject.getString("name"),
                                            bookJsonObject.getString("author"),
                                            bookJsonObject.getString("rating"),
                                            bookJsonObject.getString("price"),
                                            bookJsonObject.getString("image")
                                    )
                                    bookInfoList.add(bookObject)
                                    recyclerDashboard.adapter = recyclerAdapter
                                    recyclerDashboard.layoutManager = layoutManager

//                                recyclerDashboard.addItemDecoration(
//                                    DividerItemDecoration(
//                                        recyclerDashboard.context,
//                                        (layoutManager as LinearLayoutManager).orientation
//                                    )
//                                )


                                }
                            } else {
                                Toast.makeText(activity as Context, " Some error has occured", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: JSONException) {
                            Toast.makeText(activity as Context, "Some Unexpected Error", Toast.LENGTH_SHORT).show()
                        }

                    }, Response.ErrorListener {
                        if (activity != null) {
                            Toast.makeText(activity as Context, " Some Volley error has occured", Toast.LENGTH_SHORT).show()
                        }
                    }) {

                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] =
                                    "application/json" // specifies that the format in which the data is exchanged is in the form of json
                            headers["token"] = "7efda86e9569a1"
                            return headers
                        }
                    }

            queue.add((jsonObjectRequest))
        } else {
            val dailog = AlertDialog.Builder(activity as Context)
            dailog.setTitle("Error")
            dailog.setMessage("Internet Connection Not Found!")
            dailog.setPositiveButton("Settings") { text, listener ->
                val settingsintent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsintent)
                activity?.finish()
            }
            dailog.setNegativeButton("Cancel") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dailog.create()
            dailog.show()
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id=item?.itemId
        if(id==R.id.action_sort){
            Collections.sort(bookInfoList,ratingComparator)
            bookInfoList.reverse()

        }
        recyclerAdapter.notifyDataSetChanged()// Notify the adapter that the order of the data has changed
        return super.onOptionsItemSelected(item)
    }
}