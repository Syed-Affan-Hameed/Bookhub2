package com.syed.bookhub.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import com.syed.bookhub.R
import com.syed.bookhub.database.BookDatabase
import com.syed.bookhub.database.BookEntity
import com.syed.bookhub.util.ConnectionManager
import org.json.JSONObject
import org.w3c.dom.Text


class DesccriptionActivity : AppCompatActivity() {

    lateinit var txtBookName: TextView
    lateinit var txtBookAuthor: TextView
    lateinit var txtBookPrice: TextView
    lateinit var txtBookRating: TextView
    lateinit var imgBookImage: ImageView
    lateinit var txtBookDesc: TextView
    lateinit var btnaddtofav: Button
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var toolbar: Toolbar

    var bookId: String? = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desccription)

        txtBookName = findViewById(R.id.txtBookNAME)
        txtBookAuthor = findViewById(R.id.txtBookAutHOR)
        txtBookPrice = findViewById(R.id.txtBookCOST)
        txtBookRating=findViewById(R.id.txtBookRatING)
        imgBookImage = findViewById(R.id.imgBookImAGE)
        txtBookDesc = findViewById(R.id.txtBookdesc)
        btnaddtofav = findViewById(R.id.btnaddtofav)
        progressBar = findViewById(R.id.progressBarDes)
        progressBar.visibility = View.VISIBLE
        progressLayout = findViewById(R.id.progressbarlayOUT)
        progressLayout.visibility = View.VISIBLE


        toolbar=findViewById(R.id.destoolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Book Details"

        if (intent != null && bookId != "100") {
            bookId = intent.getStringExtra("book_id")

        } else {
            Toast.makeText(this@DesccriptionActivity, "Some error has occurred", Toast.LENGTH_SHORT)
                .show()
            finish()
        }

        val queue = Volley.newRequestQueue(this@DesccriptionActivity)
        val url = "http://13.235.250.119/v1/book/fetch_books/"


        val jsonparams = JSONObject()
        jsonparams.put("book_id", bookId)
if(ConnectionManager().checkConnectivity(this@DesccriptionActivity)) {
    val jsonRequest = object : JsonObjectRequest(
        Request.Method.POST,
        url,
        jsonparams,
        Response.Listener {

            // addinn try catch block

            try {
                val success = it.getBoolean("success")
                if (success) {

                    val bookJsonObject = it.getJSONObject("book_data")
                    progressLayout.visibility = View.GONE
                    val bookImageUrl=bookJsonObject.getString("image")
                    Picasso.get().load(bookJsonObject.getString("image"))
                        .error(R.drawable.generic_image).into(imgBookImage)
                    txtBookName.text = bookJsonObject.getString("name")
                    txtBookAuthor.text = bookJsonObject.getString("author")
                    txtBookPrice.text = bookJsonObject.getString("price")
                    txtBookRating.text = bookJsonObject.getString("rating")
                    txtBookDesc.text = bookJsonObject.getString("description ")

                    val bookEntity=BookEntity(bookId?.toInt() as Int,
                        txtBookName.text.toString(),
                                txtBookAuthor.text.toString(),
                                txtBookPrice.text.toString(),
                                txtBookRating.text.toString(),
                                txtBookDesc.text.toString(),bookImageUrl
                    )

                    val checkFav=DBAsyncTask(applicationContext,bookEntity,1).execute()
                    val isFav=checkFav.get()

                    if(isFav){
                        btnaddtofav.text="Remove From favourites"
                        val favColor=ContextCompat.getColor(applicationContext,R.color.FavouriteColor)
                        btnaddtofav.setBackgroundColor(favColor)

                    }else{
                        btnaddtofav.text="Add to favourites"
                        val favColor=ContextCompat.getColor(applicationContext,R.color.design_default_color_on_primary)
                        btnaddtofav.setBackgroundColor(favColor)
                    }
                    btnaddtofav.setOnClickListener {
                        if(!DBAsyncTask(applicationContext,bookEntity,1).execute().get()){
                            val async =DBAsyncTask(applicationContext,bookEntity,2).execute()
                            val result=async.get()
                            if(result){
                                Toast.makeText(
                                    this@DesccriptionActivity,
                                    "Book Added to favourites",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                btnaddtofav.text="remove from favourites"
                                val favColor=ContextCompat.getColor(applicationContext,R.color.FavouriteColor)
                                btnaddtofav.setBackgroundColor(favColor)
                            } else{
                                Toast.makeText(
                                    this@DesccriptionActivity,
                                    "Some error has occurred",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                        else{
                            val async =DBAsyncTask(applicationContext,bookEntity,3).execute()
                            val result=async.get()
                            if(result){
                                Toast.makeText(
                                    this@DesccriptionActivity,
                                    "Book removed from favourites",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                btnaddtofav.text="Add to favourites"
                                val favColor=ContextCompat.getColor(applicationContext,R.color.design_default_color_on_primary )
                                btnaddtofav.setBackgroundColor(favColor)
                            } else{
                                Toast.makeText(
                                    this@DesccriptionActivity,
                                    "Some error has occurred",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }


                        }
                    }

                } else {
                    Toast.makeText(
                        this@DesccriptionActivity,
                        "Some error has occurred",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } catch (e1: Exception) {
                Toast.makeText(
                    this@DesccriptionActivity,
                    "Some error has occurred",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }


        },
        Response.ErrorListener {
            Toast.makeText(
                this@DesccriptionActivity,
                "Some Volley error has occurred",
                Toast.LENGTH_SHORT
            )
                .show()
        }) {
        override fun getHeaders(): MutableMap<String, String> {
            val headers = HashMap<String, String>()
            headers["Content-type"] = "application/json"
            headers["token"] = "7efda86e9569a1"
            return headers
        }
    }
    queue.add(jsonRequest)
}
        else{

    val dailog = AlertDialog.Builder(this@DesccriptionActivity)
    dailog.setTitle("Error")
    dailog.setMessage("Internet Connection Not Found!")
    dailog.setPositiveButton("Settings") { text, listener ->
        val settingsintent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
        startActivity(settingsintent)
        finish()
    }
    dailog.setNegativeButton("Cancel") { text, listener ->
        ActivityCompat.finishAffinity(this@DesccriptionActivity)
    }
    dailog.create()
    dailog.show()
        }
    }
    class DBAsyncTask(val context: Context, val bookEntity: BookEntity, val mode:Int): AsyncTask<Void,Void,Boolean>(){
//        mode 1-> Check book is in Favourites
//        mode 2-> Save the book into the DB as Favourite
//        mode 3-> Remove the favourite book
            val db= Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()
        override fun doInBackground(vararg p0: Void?): Boolean {
            when(mode){

                1->{
                    val book:BookEntity?=db.bookDao().getBookById(bookEntity.bookId.toString())
                    db.close()
                    return book!=null
                }
                2->{
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                     return true
                }
                3->{
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }
            return false
        }
    }
}