package com.jackson.module.home.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jackson.module.home.R

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(this, Observer {
            textView.text = it
        })

        val pi = context?.packageManager?.getPackageInfo(context?.packageName!!, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Log.i("liao", "versionName=${pi?.versionName},versionCode=${pi?.longVersionCode}")
        } else {
//            Log.i("liao", "versionName=${pi?.versionName},versionCode=${pi?.versionCode}")
        }

        textView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("jackson://search")
            startActivity(intent)
        }

        return root
    }
}