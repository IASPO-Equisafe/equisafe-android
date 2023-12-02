    package com.iaspo.equisafe

    import android.content.Intent
    import android.os.Bundle
    import com.google.android.material.bottomnavigation.BottomNavigationView
    import androidx.appcompat.app.AppCompatActivity
    import androidx.navigation.findNavController
    import androidx.navigation.ui.setupWithNavController
    import com.iaspo.equisafe.databinding.ActivityMainBinding
    import com.iaspo.equisafe.ui.map.MapsActivity

    class MainActivity : AppCompatActivity() {

        private lateinit var binding: ActivityMainBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val navView: BottomNavigationView = binding.bottomNavigation

            binding.fabMap.setOnClickListener {
                startActivity(Intent(this@MainActivity, MapsActivity::class.java))
            }

            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            navView.setupWithNavController(navController)
            navView.background = null
        }
    }