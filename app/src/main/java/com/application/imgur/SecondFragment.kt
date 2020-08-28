package com.application.imgur

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.imgur.databinding.FragmentSecondBinding


class SecondFragment : BaseFragment() {

    private lateinit var binding: FragmentSecondBinding
    private lateinit var viewModel: SecondFragmentViewModel
    private lateinit var adapter: ImageRecyclerAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSecondBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(SecondFragmentViewModel::class.java).initialize(requireContext().applicationContext)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ImageRecyclerAdapter(requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.left = 20
                outRect.right = 20
                outRect.top = 40
                outRect.bottom = 10
            }
        })

        viewModel.selectedImages.observe(viewLifecycleOwner) { images -> adapter.setData(images) }
    }
}