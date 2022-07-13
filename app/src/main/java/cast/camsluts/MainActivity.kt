package cast.camsluts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.commit
import cast.camsluts.fragments.LoaderFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var toolBar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            .apply {
                toolBar = findViewById(R.id.toolBar)
                progressBar = findViewById(R.id.progressBar)
                drawerLayout = findViewById(R.id.drawerLayout)
                navigationView = findViewById(R.id.navigationView)
            }
        setSupportActionBar(toolBar).apply {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
        drawerLayout.apply { addDrawerListener(
            ActionBarDrawerToggle(this@MainActivity, drawerLayout,toolBar,0,0).apply
        {
            syncState()
            drawerArrowDrawable.color = ContextCompat.getColor(this@MainActivity, R.color.white) })
            isClickable = true
        }
        navigationView.apply { itemIconTintList = null }.setNavigationItemSelectedListener(this)
        setActionBarTitle("Female Cams", "on Chaturbate")
        writeToast("Open Female Cams")
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId){
            R.id.favorite -> {}
            R.id.chaturbate_featured -> {replaceFragment("https://chaturbate.com/?page=")
                setActionBarTitle("Featured Cams", "on Chaturbate")
                writeToast("Open Featured Cams")
            }
            R.id.chaturbate_female -> {replaceFragment("https://chaturbate.com/female-cams/?page=")
                setActionBarTitle("Female Cams", "on Chaturbate")
                writeToast("Open Female Cams")
            }
            R.id.chaturbate_male -> {replaceFragment("https://chaturbate.com/male-cams/?page=")
                setActionBarTitle("Male Cams", "on Chaturbate")
                writeToast("Open Male Cams")
            }
            R.id.chaturbate_couple -> {replaceFragment("https://chaturbate.com/couple-cams/?page=")
                setActionBarTitle("Couple Cams", "on Chaturbate")
                writeToast("Open Couple Cams")
            }
            R.id.chaturbate_trans -> {replaceFragment("https://chaturbate.com/trans-cams/?page=")
                setActionBarTitle("Trans Cams", "on Chaturbate")
                writeToast("Open Trans Cams")
            }
            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                writeToast("Open Settings")
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private fun writeToast(message:String){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }

    private fun setActionBarTitle(title:String, subtitle:String){
        supportActionBar?.apply {
            setTitle(title)
            setSubtitle(subtitle)
        }
    }

    private fun replaceFragment(Url:String){
        supportFragmentManager.commit{
            replace(R.id.fragmentContainerView, LoaderFragment.newInstance(Url))
        }
    }

}