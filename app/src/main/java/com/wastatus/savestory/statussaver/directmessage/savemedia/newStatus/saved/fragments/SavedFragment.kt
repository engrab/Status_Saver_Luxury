package com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.whatsapp.viewModels.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wastatus.savestory.statussaver.directmessage.savemedia.R
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.fragments.fragments.pojos.StatusModel
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.fragments.fragments.viewModels.StatusViewModel
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.whatsapp.viewModels.adapters.SavedAdapter
import kotlin.collections.ArrayList


class SavedFragment : Fragment() {

    private lateinit var rv: RecyclerView
    private lateinit var waAdapter: SavedAdapter
     val viewModel: StatusViewModel by activityViewModels()
    private lateinit var isEmptyList: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        rv = view.findViewById(R.id.rv)
        isEmptyList = view.findViewById(R.id.isEmptyList)
        waAdapter = SavedAdapter(requireContext())
        rv.layoutManager = GridLayoutManager(requireContext(), 3)
        rv.adapter = waAdapter

        viewModel.savedList.observe(viewLifecycleOwner) {
            it.reversed()
            waAdapter.setAdapter(it as ArrayList<StatusModel>)
            waAdapter.notifyDataSetChanged()
            if (it.size>0){
                isEmptyList.visibility = View.GONE
            }else{
                isEmptyList.visibility = View.VISIBLE
            }

        }



    }


}