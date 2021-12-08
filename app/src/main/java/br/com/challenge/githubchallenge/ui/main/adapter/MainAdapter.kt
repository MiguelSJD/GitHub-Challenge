package br.com.challenge.githubchallenge.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.challenge.githubchallenge.R
import br.com.challenge.githubchallenge.data.model.Item
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.imageview.ShapeableImageView

class MainAdapter(
    private val items: ArrayList<Item>,
    private val listener: OnRepositoryClickListener
): RecyclerView.Adapter<MainAdapter.ViewHolderRepository>() {

    class ViewHolderRepository(view: View): RecyclerView.ViewHolder(view) {
        private val tvRepositoryName = view.findViewById<TextView>(R.id.tv_repository_name)
        private val tvStarsCount = view.findViewById<TextView>(R.id.tv_stars_count)
        private val tvForkCount = view.findViewById<TextView>(R.id.tv_fork_count)
        private val ivOwnerPicture = view.findViewById<ShapeableImageView>(R.id.iv_owner)
        private val tvOwnerName = view.findViewById<TextView>(R.id.tv_owner_name)

        fun bind(item: Item){
            tvRepositoryName.text = item.name
            tvStarsCount.text = item.stargazersCount.toString()
            tvForkCount.text = item.forks.toString()
            tvOwnerName.text = item.owner.login

            val requestOptions = RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.circular_progress_bar)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .signature(ObjectKey(item.updatedAt))

            Glide.with(itemView)
                .load(item.owner.avatarUrl)
                .thumbnail(0.25f)
                .apply(requestOptions)
                .centerCrop()
                .into(ivOwnerPicture)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderRepository =
        ViewHolderRepository(LayoutInflater.from(parent.context).inflate(R.layout.item_repository, parent, false))

    override fun onBindViewHolder(holder: ViewHolderRepository, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            listener.onRepositoryClicked(items[position])
        }
    }

    override fun getItemCount(): Int = items.size

    fun addRepositories(repositories:List<Item>){
        this.items.apply {
            addAll(repositories)
        }
    }

    fun updateRepositories(repositories:List<Item>){
        this.items.apply {
            clear()
            addAll(repositories)
        }
    }

    interface OnRepositoryClickListener {
        fun onRepositoryClicked(item: Item)
    }

}