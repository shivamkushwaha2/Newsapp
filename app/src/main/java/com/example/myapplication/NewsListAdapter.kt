package com.example.myapplication

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.newsapp.News

class NewsListAdapter(private val listner: NewsItemclicked): RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {
    private val items: ArrayList<News> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemnews, parent, false)
        val viewHolder = NewsViewHolder(view)
        view.setOnClickListener {
            listner.onitemclicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {

        val currentItem = items[position]
        holder.author.text = "Source - " + currentItem.author
        holder.titleview.text = currentItem.title
        Glide.with(holder.itemView.context).load(currentItem.imageurl).listener(object : RequestListener<Drawable> {
                override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    holder.progress.visibility = View.GONE
                    return false
                }

                override fun onLoadFailed(e: GlideException?, model: Any?,target: com.bumptech.glide.request.target.Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    return false
                }
            }).into(holder.image)

    }



    fun updatenews(updatednews: ArrayList<News>) {
//        items.clear()
        items.addAll(updatednews)
        notifyDataSetChanged()
    }


   inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image: ImageView = itemView.findViewById(R.id.image)
        val titleview: TextView = itemView.findViewById(R.id.title)
        val author: TextView = itemView.findViewById(R.id.author)
       val progress: LottieAnimationView = itemView.findViewById(R.id.progress)

   }
}
interface NewsItemclicked{
    fun onitemclicked(item: News)
}