package com.example.myapplication

import MySingleton
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.newsapp.News

class Science : AppCompatActivity(), NewsItemclicked {
    lateinit var mAdapter: NewsListAdapter
    lateinit var url: String
    lateinit var layoutManager: LinearLayoutManager
    var isLoading = false
    var totalpage:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_technology)

        val a: RecyclerView = findViewById(R.id.tech_recyclerView)
        layoutManager = LinearLayoutManager(this)
        a.layoutManager = layoutManager
        fetchdata(1)
        mAdapter = NewsListAdapter(this)
        a.adapter = mAdapter
        a.addOnScrollListener(recyclerViewOnScrollListener)

        val toolbar: Toolbar = findViewById<View>(R.id.tool) as Toolbar

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }


    }
    var page = 1
    val recyclerViewOnScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (isLoading){
                    return
                }else {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                        page++
                        if (page<=totalpage) {
                            loadmore(page)
                        }
                    }
                }
            }
        }
    private fun fetchdata(page:Int) {
        isLoading = true
        url ="https://newsapi.org/v2/top-headlines?country=in&category=science&apiKey=ef78e990b17546c98dec95130aa71ba4&page=$page"
        val jsonObjectRequest = object: JsonObjectRequest(
            Method.GET, url,
            null,
            Response.Listener {
                val totalresults:Int = it.getInt("totalResults")
                totalpage = totalresults/20
                if (totalresults%20 != 0){
                    totalpage=totalpage+1
                }
                val jsonArray = it.getJSONArray("articles")
                val newsarray = ArrayList<News>()
                for(i in 0 until jsonArray.length()) {
                    val newsJsonObject = jsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                    newsarray.add(news)

                }

                mAdapter.updatenews(newsarray)
                isLoading = false
                val btn = findViewById<LottieAnimationView>(R.id.loadbutton)
                btn.visibility = View.GONE
            },
            Response.ErrorListener {
                isLoading = false
                val btn = findViewById<LottieAnimationView>(R.id.loadbutton)
                btn.visibility = View.GONE
            }

        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }


        MySingleton.getInstance(this.applicationContext).addToRequestQueue(jsonObjectRequest)
    }

    override fun onitemclicked(item: News) {
        val url = item.url
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }

    fun loadmore(page: Int) {
        val btn = findViewById<LottieAnimationView>(R.id.loadbutton)
        btn.visibility = View.VISIBLE
        isLoading = true
        fetchdata(page)
    }
}