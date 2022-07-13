package cast.camsluts.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import cast.camsluts.R
import cast.camsluts.adapters.LoaderAdapter.ViewHolder
import cast.camsluts.models.Model
import cast.camsluts.sites.Chaturbate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.RecyclerView
import cast.camsluts.DetailActivity
import kotlinx.android.synthetic.main.recyclerview_layout.view.*

class LoaderAdapter (private val context: Context): RecyclerView.Adapter<ViewHolder>() {
    var items = mutableListOf<Model>()

    var favoritesList = mutableListOf<Model>()

    interface RecyclerViewAdapterDelegate{
        fun onLoadMore()
    }

    var delegate: RecyclerViewAdapterDelegate? = null

    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView), View.OnClickListener  {
        private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
        private var repo = Chaturbate()
        val modelName: TextView = itemView.findViewById(R.id.tv_name)
        val modelImage: ImageView = itemView.findViewById(R.id.iw_image)
        val modelAge: TextView = itemView.findViewById(R.id.tv_age)
        val modelLabel: TextView = itemView.findViewById(R.id.tv_label)
        val modelDescription: TextView = itemView.findViewById(R.id.tv_description)
        val modelViews: TextView = itemView.findViewById(R.id.tv_views)
        val modelLink: TextView = itemView.findViewById(R.id.tv_link)
        init{itemView.setOnClickListener(this)}
        override fun onClick(v: View?) {
            if (this.modelLink.text.contains("chaturbate")){
                repo = Chaturbate()
                var videoLink:String
                scope.launch {
                    videoLink = repo.parseVideo(modelLink.text.toString())
                    withContext(Dispatchers.Main) {
                        val intent: Intent = Intent(v!!.context, DetailActivity::class.java)
                        intent.putExtra("title", modelName.text)
                        intent.putExtra("link", videoLink)
                        Toast.makeText(itemView.context, modelName.text, Toast.LENGTH_LONG).show()
                        v.context.startActivity(intent)
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (!items.contains(Model(items[position].Name,
                items[position].Image,
                items[position].Label,
                items[position].Age,
                items[position].Description,
                items[position].Views,
                items[position].Link))){
            // holder.itemView.visibility = View.GONE
        }else{
            holder.modelName.text = items[position].Name
            Glide.with(holder.itemView)
                .load(items[position].Image)
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.chaturbate_nopicture)
                .dontTransform()
                .into(holder.modelImage)
            holder.modelDescription.text = items[position].Description
            holder.modelViews.text = items[position].Views
            holder.modelAge.text = items[position].Age
            holder.modelLabel.text = items[position].Label
            holder.modelLink.text = items[position].Link
            //Scroll infinity (load more)
          //  if (position == items.size - 3) {
           //     delegate?.onLoadMore()
           // }
            //holder.itemView.favorite_check.isChecked = favoritesList.contains(items[position])
            holder.itemView.favorite_check.setOnClickListener {
                if (it.favorite_check.isChecked) {
                    favoritesList.add(items[position])
                    Toast.makeText(holder.itemView.context,holder.modelName.text.toString() + " saved in Favorites!",
                        Toast.LENGTH_LONG).show()
                } else {
                    favoritesList.remove(items[position])
                    Toast.makeText(holder.itemView.context,holder.modelName.text.toString() + " removed from Favorites",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun getModels():MutableList<Model>{
       return items
    }
    fun addMore(data: MutableList<Model>){
        items.addAll(data)
        notifyDataSetChanged()
    }
    fun clearAll(){
        items.removeAll { true }
        notifyDataSetChanged()
    }

}