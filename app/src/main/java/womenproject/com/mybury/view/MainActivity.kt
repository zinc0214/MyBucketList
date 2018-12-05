package womenproject.com.mybury.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.ActivityMainBinding

/**
 * Created by HanAYeon on 2018. 11. 26..
 */

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController


   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

       drawerLayout = binding.drawerLayout
       navController = Navigation.findNavController(this, R.id.nav_fragment)

       appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun showDialog() {
        BaseDialog().showDialog()
    }
}