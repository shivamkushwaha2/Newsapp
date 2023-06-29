package com.example.myapplication
import MySingleton
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.newsapp.News


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PageFragment : Fragment() , NewsItemclicked {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var mAdapter: NewsListAdapter
    lateinit var url: String
    lateinit var layoutManager: LinearLayoutManager
    var isLoading = false
    var totalpage:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_page, container, false)
        val a: RecyclerView = view.findViewById(R.id.recyclerView)
        val btn = view.findViewById<LottieAnimationView>(R.id.loadbutton)
        layoutManager = LinearLayoutManager(context)
        a.layoutManager = layoutManager
        fetchdata(1,btn)
        mAdapter = NewsListAdapter(this)
        a.adapter = mAdapter
        a.addOnScrollListener(recyclerViewOnScrollListener)

        return view
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

                    val btn = view!!.findViewById<LottieAnimationView>(R.id.loadbutton)

                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                        page++
                        if (page<=totalpage) {
                            loadmore(page,btn)
                        }
                    }
                }
            }
        }
    private fun fetchdata(page: Int,btn: LottieAnimationView) {
        isLoading = true

        url ="https://newsapi.org/v2/top-headlines?country=in&category=general&apiKey=&page=$page"
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
                btn.visibility = View.GONE
            },
            Response.ErrorListener {
                isLoading = false
                btn.visibility = View.GONE
            }

        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }


        MySingleton.getInstance(requireContext().applicationContext).addToRequestQueue(jsonObjectRequest)
    }
    override fun onitemclicked(item: News) {
        val url = item.url
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(url))
    }
    fun loadmore(page: Int,btn: LottieAnimationView) {
        btn.visibility = View.VISIBLE
        isLoading = true
        fetchdata(page,btn)
    }


}
