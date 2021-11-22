package com.syed.bookhub.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.syed.bookhub.R
import com.syed.bookhub.adapter.FavouriteRecyclerAdapter
import com.syed.bookhub.database.BookDatabase
import com.syed.bookhub.database.BookEntity

// TODO: Rename parameter arguments, choose names that match


/**
 * A simple [Fragment] subclass.
 * Use the [FavouriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavouriteFragment : Fragment() {
    // TODO: Rename and change types of parameters
        lateinit var recyclerFavourite: RecyclerView
        lateinit var progressfavLayout: RelativeLayout
        lateinit var progressfavBar: ProgressBar
         lateinit var layoutManager : RecyclerView.LayoutManager
        lateinit var recyclerAdapter: FavouriteRecyclerAdapter

        var dbBookList= listOf<BookEntity>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_favourite, container, false)
        recyclerFavourite=view.findViewById(R.id.favrecycler)
        progressfavLayout=view.findViewById(R.id.progressfavLayout)
        progressfavBar=view.findViewById(R.id.progressBarfav)

        layoutManager=GridLayoutManager(activity as Context,2)
        dbBookList =RetrieveFavourites(activity as Context).execute().get()

        if(activity!=null){
            progressfavLayout.visibility=View.GONE
            recyclerAdapter= FavouriteRecyclerAdapter(activity as Context,dbBookList)
            recyclerFavourite.adapter=recyclerAdapter
            recyclerFavourite.layoutManager=layoutManager
        }

        return view
    }

    class RetrieveFavourites(val context:Context): AsyncTask<Void,Void,List<BookEntity>>(){
       override fun doInBackground(vararg p0:Void?):List<BookEntity>{
        val db= Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()
           return db.bookDao().getAllBooks()
       }

    }


}