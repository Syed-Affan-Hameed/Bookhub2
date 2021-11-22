package com.syed.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.syed.bookhub.R
import com.syed.bookhub.activity.DesccriptionActivity
import com.syed.bookhub.model.Book
import org.w3c.dom.Text

class DashboardRecyclerAdapter(val context:Context,val itemList:ArrayList<Book>):RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>(){
    // ViewHolder is present inside the adapter
    // creating a class to inherit the ViewHolder class
    class DashboardViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val textView:TextView=view.findViewById(R.id.txtRecyclerRowItem)
        val txtauth:TextView=view.findViewById(R.id.txtauthname)
        val txtbookcost:TextView=view.findViewById(R.id.txtbookcost)
        val txtRating:TextView=view.findViewById(R.id.txtRating)
        val imgbookima:ImageView=view.findViewById(R.id.imgbookimg)
        val rllayout:RelativeLayout=view.findViewById(R.id.rllayout)


    }
 // these methods are responsible for setting the adapter to the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)
     return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        // putting the text in the text section of the textview
        val book =itemList[position]
        holder.textView.text=book.BookName
        holder.txtauth.text=book.BookAuthor
        holder.txtbookcost.text=book.BookPrice
        holder.txtRating.text=book.BookRating
       // holder.imgbookima.setImageResource(book.BookImage)
        Picasso.get().load(book.BookImage).error(R.drawable.generic_image).into(holder.imgbookima)

        holder.rllayout.setOnClickListener {

           // Toast.makeText(context,"Click Click ${holder.textView.text} not functional yet",Toast.LENGTH_SHORT).show()
            val intent = Intent(context,DesccriptionActivity::class.java)
            intent.putExtra("book_id",book.bookId)
                context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return itemList.size
    }
}