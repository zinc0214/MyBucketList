package womenproject.com.mybury.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.databinding.LayoutSliderItemBinding

class SliderAdapter : RecyclerView.Adapter<SliderAdapter.SliderItemViewHolder>() {

    private val data: ArrayList<String> = ArrayList()
    private lateinit var binding : LayoutSliderItemBinding
    var callback: Callback? = null
    val clickListener = View.OnClickListener { v -> v?.let { callback?.onItemClicked(it) } }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderItemViewHolder {
        binding = LayoutSliderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SliderItemViewHolder(binding)
    }


    override fun onBindViewHolder(holder: SliderItemViewHolder, position: Int) {
        holder.apply {
            bind(clickListener, data[position])
        }
    }

    fun setData(data: ArrayList<String>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface Callback {
        fun onItemClicked(view: View)
    }

    class SliderItemViewHolder(private val binding: LayoutSliderItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(selectListener : View.OnClickListener, position: String) {
            binding.apply {
                //numberSelectListener = selectListener
                binding.tvItem.text = position
            }
        }
    }
}