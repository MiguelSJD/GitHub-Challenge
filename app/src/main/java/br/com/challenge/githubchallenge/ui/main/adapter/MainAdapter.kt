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
    private val item: ArrayList<Item>
): RecyclerView.Adapter<MainAdapter.ViewHolderRepository>() {

    class ViewHolderRepository(view: View): RecyclerView.ViewHolder(view) {
        private val tvRepositoryName = view.findViewById<TextView>(R.id.tv_repository_name)
        private val tvStarsCount = view.findViewById<TextView>(R.id.tv_stars_count)
        private val tvForkCount = view.findViewById<TextView>(R.id.tv_fork_count)
        private val ivOwnerPicture = view.findViewById<ShapeableImageView>(R.id.iv_owner)
        private val tvOwnerName = view.findViewById<TextView>(R.id.tv_owner_name)

        fun bind(items: Item){
            tvRepositoryName.text = items.name
            tvStarsCount.text = items.stargazersCount.toString()
            tvForkCount.text = items.forks.toString()
            tvOwnerName.text = items.owner.login

            val requestOptions = RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.circular_progress_bar)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .signature(ObjectKey(items.updatedAt))

            Glide.with(itemView)
                .load(items.owner.avatarUrl)
                .thumbnail(0.25f)
                .apply(requestOptions)
                .centerCrop()
                .into(ivOwnerPicture)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderRepository =
        ViewHolderRepository(LayoutInflater.from(parent.context).inflate(R.layout.item_repository, parent, false))

    override fun onBindViewHolder(holder: ViewHolderRepository, position: Int) {
        holder.bind(item[position])
    }

    override fun getItemCount(): Int = item.size

    fun addRepositories(repositories:List<Item>){
        this.item.apply {
            addAll(repositories)
        }
    }

}