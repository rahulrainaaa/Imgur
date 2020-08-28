package com.application.imgur

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.imgur.databinding.ItemRcCommentLayoutBinding
import com.application.imgur.databinding.ItemRcSimpleLayoutBinding
import com.squareup.picasso.Picasso

class ImageRecyclerAdapter(private val context: Context, private val btnSaveClickListener: View.OnClickListener? = null, private val btnEditClickListener: View.OnClickListener? = null) :
    RecyclerView.Adapter<ImageRecyclerAdapter.BaseHolder>() {

    open class BaseHolder(view: View) : RecyclerView.ViewHolder(view)
    class SimpleItemHolder(val binding: ItemRcSimpleLayoutBinding) : BaseHolder(binding.root)
    class CommentItemHolder(val binding: ItemRcCommentLayoutBinding) : BaseHolder(binding.root)

    private enum class CellType(val code: Int) {
        SimpleCell(1), CommentCell(2)
    }

    var images = emptyList<Image>()
    private var editPosition = -1
    private lateinit var commentItemCellBinding: ItemRcCommentLayoutBinding

    override fun getItemViewType(position: Int): Int {
        return if (position == editPosition) CellType.CommentCell.code
        else CellType.SimpleCell.code
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        val inflater = LayoutInflater.from(context)
        return if (viewType == CellType.CommentCell.code) {
            val commentCellBinding = ItemRcCommentLayoutBinding.inflate(inflater, parent, false)
            commentItemCellBinding = commentCellBinding
            commentCellBinding.imgBtnSave.setOnClickListener(btnSaveClickListener)
            if (btnEditClickListener == null) commentCellBinding.imgBtnSave.visibility = View.GONE
            CommentItemHolder(commentCellBinding)
        } else {
            val simpleCellBinding = ItemRcSimpleLayoutBinding.inflate(inflater, parent, false)
            simpleCellBinding.imgBtnEdit.setOnClickListener(btnEditClickListener)
            if (btnEditClickListener == null) simpleCellBinding.imgBtnEdit.visibility = View.GONE
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
        if (position >= 0) notifyItemChanged(position)
        editPosition = position
        if (oldEditPosition >= 0) notifyItemChanged(oldEditPosition)
    }

    fun getCurrentInputComment() = commentItemCellBinding.etComment.text.toString()
}