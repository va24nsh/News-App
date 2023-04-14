package com.example.newsfresh

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest


class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var mAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerV = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerV.layoutManager = LinearLayoutManager(this)
        mAdapter = NewsListAdapter(this)
        fetchData()
        recyclerV.adapter = mAdapter

    }

    private fun fetchData() {
        val url = "https://newsdata.io/api/1/news?apikey=pub_203264e38665cb9f7b2c5832471b57b9f808a&q=gaming"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            {
                val newsJsonArray = it.getJSONArray("results")
                val newsArray = ArrayList<News>()
                for(i in 0 until newsJsonArray.length()) {
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title") ,
                        newsJsonObject.getString("language"),
                        newsJsonObject.getString("link"),
                        "https://images4.alphacoders.com/853/85336.jpg"
//                        newsJsonObject.getString("https://images4.alphacoders.com/853/85336.jpg")
                    )
                    newsArray.add(news)
                }

                mAdapter.updateNews(newsArray)
            },
            {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            }
        )

        //Add request to request queue
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()

//        builder.setInitialActivityHeightPx(50).setCloseButtonPosition(CustomTabsIntent.CLOSE_BUTTON_POSITION_END).build()

        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }

}