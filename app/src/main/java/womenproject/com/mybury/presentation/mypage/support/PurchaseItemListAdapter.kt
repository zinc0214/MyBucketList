package womenproject.com.mybury.presentation.mypage.support

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import womenproject.com.mybury.R
import womenproject.com.mybury.data.PurchasableItem
import womenproject.com.mybury.databinding.ItemSupportPurchaseBinding

class PurchaseItemListAdapter(private val purchasableItems: List<PurchasableItem>) :
        RecyclerView.Adapter<PurchaseItemListAdapter.PurchaseViewHolder>() {

    val selectedItemNum: PurchasableItem?
        get() = if (checkedPosition != -1) {
            purchasableItems[checkedPosition]
        } else null

    // if checkedPosition = -1, there is no default selection
    // if checkedPosition = 0, 1st item is selected by default
    private var checkedPosition = purchasableItems.indexOfFirst { it.isPurchasable }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            PurchaseViewHolder(ItemSupportPurchaseBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(@NonNull purchaseViewHolder: PurchaseViewHolder, position: Int) {
        purchaseViewHolder.bind(purchasableItems[position])
    }

    override fun getItemCount(): Int {
        return purchasableItems.size
    }

    inner class PurchaseViewHolder(private val binding: ItemSupportPurchaseBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PurchasableItem) {
            binding.info = item
            Log.e("myBury", "item.isPurchasable :  ${item.itemName} ,, ${item.dpYn}")

            try {
                Glide.with(binding.imageView).load(item.itemImg)
                        .override(300, 300)
                        .placeholder(R.drawable.place_holder)
                        .into(binding.imageView)
            } catch (ex: IllegalArgumentException) {
                Log.e("Glide-tag", "....")
            }


            if (!item.isPurchasable) {
                itemView.isEnabled = false
                itemView.isClickable = false
                binding.contentLayout.background = binding.root.context.getDrawable(R.drawable.shape_b4b4b4_r12)
            }

            if (checkedPosition == -1) {
                binding.contentLayout.isSelected = true
                binding.itemPriceTextView.isSelected = true
            } else {
                if (checkedPosition == adapterPosition) {
                    binding.contentLayout.isSelected = true
                    binding.itemPriceTextView.isSelected = true
                } else {
                    binding.contentLayout.isSelected = false
                    binding.itemPriceTextView.isSelected = false
                }
            }
            itemView.setOnClickListener {
                binding.contentLayout.isSelected = true
                binding.itemPriceTextView.isSelected = true
                if (checkedPosition != adapterPosition) {
                    notifyItemChanged(checkedPosition)
                    checkedPosition = adapterPosition
                }
            }
        }
    }


}