 package com.syed.bookhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.syed.bookhub.*
import com.syed.bookhub.fragment.AboutFragment
import com.syed.bookhub.fragment.DashboardFragment
import com.syed.bookhub.fragment.FavouriteFragment
import com.syed.bookhub.fragment.ProfileFragment

 class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    var previousmenuitem:MenuItem? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        drawerLayout = findViewById(R.id.drawerLayout)
        frameLayout = findViewById(R.id.frameLayout)
        toolbar = findViewById(R.id.toolbar)
        coordinatorLayout = findViewById(R.id.coorinatorLayout)
        navigationView = findViewById(R.id.navigationView)
        setUpToolbar()

        opendashboard()


        val actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity, drawerLayout, R.string.open_drawer, R.string.close_drawer)
        // the parameters of the function are context of the DrawerToggle and drawer layout and action string which give the states to the navigation drawer
        drawerLayout.addDrawerListener(actionBarDrawerToggle) // the above variable is passed as input to this function
        // this function makes the hamburger icon functional and animating, this sets click listerner to icon
        actionBarDrawerToggle.syncState()// synchronizing the state of the toggle to the state of the navigation drawer( this changes to ham icon to back icon(to close) and vice versa
        // the above functions do not add all the functionalities to the drawer

        // the clicks on the action bar are handled differently see the function "onOptionsItemSelected"

        navigationView.setNavigationItemSelectedListener {

            if(previousmenuitem!= null) // if the item is checked already
            {
                previousmenuitem?.isChecked=false // setting it to false
        }
            it.isCheckable=true
            it.isChecked=true
            previousmenuitem=it


            when(it.itemId)// using it gives us the currently selected Item
            {
                R.id.dashboard ->{
                    //Toast.makeText(this@MainActivity,"Dashboard",Toast.LENGTH_SHORT).show()
                  opendashboard()

                }
                R.id.favourites ->{
                    //Toast.makeText(this@MainActivity,"favourites",Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, FavouriteFragment()).commit()
                    supportActionBar?.title = "Favourites"
                    drawerLayout.closeDrawers()

                }
                R.id.profile ->{
        //   Toast.makeText(this@MainActivity,"profile",Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, ProfileFragment()).commit()//name for the reference of the backstack
                    supportActionBar?.title = "Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.about_app ->{
                    //Toast.makeText(this@MainActivity,"About App",Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, AboutFragment()).commit()
                    supportActionBar?.title = "About App"
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true // @setNavigationItemSelectedListener is used to specify the lambda representation
        }

    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar Title"
        // setting up the hamburger icon
        // using the predefined home button of the action bar
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id =item.itemId
        if(id==android.R.id.home)// since the ham icon is placed in the home button of the action bar
        {
            drawerLayout.openDrawer(GravityCompat.START ) // if the item selected is the home button(ham icon in this case) then we want to open the drawer from where the screen starts
        }
        return super.onOptionsItemSelected(item)
    }
    fun opendashboard(){
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, DashboardFragment()).commit()//apply the transaction
        supportActionBar?.title = "Dashboard"
        drawerLayout.closeDrawers()
        navigationView.setCheckedItem(R.id.dashboard)
  }

 // My own implementation of the onBackPressed method inorder to maintain the order and handle the title change anamoly
    override fun onBackPressed() {
        val fragmentid=supportFragmentManager.findFragmentById(R.id.frameLayout)

        when(fragmentid)
        {

            !is DashboardFragment ->opendashboard()

            else-> super.onBackPressed()

        }




    }

}