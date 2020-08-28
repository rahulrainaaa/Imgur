package com.application.imgur

import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.imgur.databinding.FragmentFirstBinding

class FirstFragment : BaseFragment() {

    private lateinit var binding: FragmentFirstBinding
    private lateinit var viewModel: FirstFragmentViewModel
    private lateinit var imageRecyclerAdapter: ImageRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(FirstFragmentViewModel::class.java).initialize(requireActivity().applicationContext)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_first_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_comments -> requireActivity().findNavController(R.id.nav_host_fragment).navigate(R.id.SecondFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadSavedInstanceAndAddObserver()
        imageRecyclerAdapter = ImageRecyclerAdapter(requireContext(), btnSaveOnClickListener, btnEditOnClickListener)
        binding.recyclerView.adapter = imageRecyclerAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.left = 20
                outRect.right = 20
                outRect.top = 40
                outRect.bottom = 10
            }
        })

        binding.etSearch.setOnEditorActionListener { textView, actionId, _ ->

            if (actionId === EditorInfo.IME_ACTION_NEXT) {
                val strSearchKeyword = textView.text.toString()
                if (strSearchKeyword.isNullOrBlank()) {
                    toast(R.string.enter_search_text)
                    return@setOnEditorActionListener false
                }
                hideSoftKeyboard()
                viewModel.fetchContent(keyword = strSearchKeyword)
            }
            return@setOnEditorActionListener true
        }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisiblePosition: Int = layoutManager.findLastVisibleItemPosition()

                if (lastVisiblePosition == (imageRecyclerAdapter.images.size - 1) && !viewModel.flagWebImagesEof) {
                    viewModel.fetchContent(page = viewModel.pageNo + 1, keyword = viewModel.strQuery)
                }
            }
        })
    }

    private fun loadSavedInstanceAndAddObserver() {

        binding.etSearch.setText(viewModel.strQuery)

        viewModel.allImages.observe(viewLifecycleOwner) { images ->
            (binding.recyclerView.adapter as ImageRecyclerAdapter).setData(images)
        }

        viewModel.flagHttpProcessingLiveData.observe(viewLifecycleOwner) { isHttpProcessing ->
            if (isHttpProcessing) showProgressDialog(getString(R.string.please_wait))
            else dismissProgressDialog()
        }
    }

    private val btnEditOnClickListener = View.OnClickListener { btnEdit ->

        val position = btnEdit.tag as Int
        imageRecyclerAdapter.setEditingPosition(position)
    }

    private val btnSaveOnClickListener = View.OnClickListener { btnSave ->

        val position = btnSave.tag as Int
        val image = viewModel.allImages.value!![position]
        image.comment = imageRecyclerAdapter.getCurrentInputComment()
        viewModel.updateDBImage(image)
        imageRecyclerAdapter.setEditingPosition(-1)
    }

}