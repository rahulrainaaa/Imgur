package com.application.imgur

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.imgur.databinding.ItemRcCommentLayoutBinding
import com.application.imgur.databinding.ItemRcSimpleLayoutBinding
import com.squareup.picasso.Picasso

class ImageRecyclerAdapter(private val context: Context, private val btnSaveClickListener: View.OnClickListener, private val btnEditClickListener: View.OnClickListener) :
    RecyclerView.Adapter<ImageRecyclerAdapter.BaseHolder>() {

    open class BaseHolder(view: View) : RecyclerView.ViewHolder(view)
    class SimpleItemHolder(val binding: ItemRcSimpleLayoutBinding) : BaseHolder(binding.root)
    class CommentItemHolder(val binding: ItemRcCommentLayoutBinding) : BaseHolder(binding.root)

    private enum class CellType(val code: Int) {
        SimpleCell(1), CommentCell(2)
    }

    private var images = emptyList<Image>()
    private var editPosition = -1

    override fun getItemViewType(position: Int): Int {
        return if (position == editPosition) CellType.CommentCell.code
        else CellType.SimpleCell.code
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        val inflater = LayoutInflater.from(context)
        return if (viewType == CellType.CommentCell.code) {
            val commentCellBinding = ItemRcCommentLayoutBinding.inflate(inflater, parent, false)
            commentCellBinding.imgBtnSave.setOnClickListener(btnSaveClickListener)
            CommentItemHolder(commentCellBinding)
        } else {
            val simpleCellBinding = ItemRcSimpleLayoutBinding.inflate(inflater, parent, false)
            simpleCellBinding.imgBtnEdit.setOnClickListener(btnEditClickListener)
            SimpleItemHolder(simpleCellBinding)
        }
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {

        val image = images[position]

        if (holder is CommentItemHolder) {
            holder.binding.imgBtnSave.tag = position
            Picasso.with(holder.binding.imageView.context)
                .load(image.link)
                .into(holder.binding.imageView)

            holder.binding.etComment.setText(image.comment ?: "")

        } else if (holder is SimpleItemHolder) {
            holder.binding.imgBtnEdit.tag = position
            Picasso.with(holder.binding.imageView.context)
                .load(image.link)
                .into(holder.binding.imageView)

            holder.binding.txtComment.text = image.comment ?: ""
        }
    }

    override fun getItemCount() = images.size

    fun setData(images: List<Image>) {
        this.images = images
        notifyDataSetChanged()
    }

    fun setEditingPosition(position: Int) {
        val oldEditPosition = editPosition
        if (position >= 0) editPosition = position
        notifyItemChanged(position)
        if (oldEditPosition >= 0) notifyItemChanged(oldEditPosition)
    }

}