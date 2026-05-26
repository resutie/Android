package com.example.fominapolinaalekseevna

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.net.URL
import java.util.concurrent.Executors

class GameRecyclerAdapter(
    private val items: List<GameItem>
) : RecyclerView.Adapter<GameRecyclerAdapter.GameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.description.text = item.description
        holder.image.setImageResource(android.R.drawable.ic_menu_gallery)
        loadImageFromUrl(item.imageUrl, holder.image)
    }

    override fun getItemCount(): Int = items.size

    class GameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.itemImage)
        val title: TextView = view.findViewById(R.id.itemTitle)
        val description: TextView = view.findViewById(R.id.itemDescription)
    }

    private fun loadImageFromUrl(url: String, imageView: ImageView) {
        imageView.tag = url
        imageLoader.execute {
            runCatching {
                URL(url).openStream().use { input ->
                    BitmapFactory.decodeStream(input)
                }
            }.onSuccess { bitmap ->
                imageView.post {
                    if (imageView.tag == url && bitmap != null) {
                        imageView.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }

    companion object {
        private val imageLoader = Executors.newFixedThreadPool(3)
    }
}
