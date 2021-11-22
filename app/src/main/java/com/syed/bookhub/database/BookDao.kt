package com.syed.bookhub.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookDao {
    @Insert   // simple
    fun insertBook(bookEntity:BookEntity)

    @Delete
    fun deleteBook(bookEntity: BookEntity)

    //get all books from the database this is complex we need to write the query
    @Query("Select * from books")
    fun getAllBooks():List<BookEntity>

        @Query("Select * from books Where bookId=:bookId")
     fun getBookById(bookId:String): BookEntity
}