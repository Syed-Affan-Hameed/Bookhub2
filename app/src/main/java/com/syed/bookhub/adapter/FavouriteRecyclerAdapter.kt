package com.syed.bookhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.syed.bookhub.R
import com.syed.bookhub.database.BookEntity


class FavouriteRecyclerAdapter(val context: Context, val bookList: List<BookEntity>): RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>(){



        class FavouriteViewHolder(view: View):RecyclerView.ViewHolder(view){
            val txtBookName: TextView =view.findViewById(R.id.txtfavbookTitle)
            val txtBookAuthor: TextView =view.findViewById(R.id.txtfavbookAUTHOR)
            val txtBookPrice: TextView =view.findViewById(R.id.txtfavbookPRICE)
            val txtBookRating: TextView =view.findViewById(R.id.txtfavBookRate)
            val imgBookImage: ImageView =view.findViewById(R.id.imgfavbook)
            val llcontent: LinearLayout =view.findViewById(R.id.llfavcontent)

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_favourite_single_row,parent,false)
        return FavouriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val book =bookList[position]
        holder.txtBookName.text=book.bookName
        holder.txtBookAuthor.text=book.bookAuthor
        holder.txtBookPrice.text=book.bookPrice
        holder.txtBookRating.text=book.bookRating
        // holder.imgbookima.setImageResource(book.BookImage)
        Picasso.get().load(book.bookImage).error(R.drawable.generic_image).into(holder.imgBookImage)

    }

    override fun getItemCount(): Int {
        return bookList.size
    }


}