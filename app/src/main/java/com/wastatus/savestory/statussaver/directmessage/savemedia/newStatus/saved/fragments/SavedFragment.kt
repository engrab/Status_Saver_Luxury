package com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.whatsapp.viewModels.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wastatus.savestory.statussaver.directmessage.savemedia.R
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.fragments.fragments.pojos.SavedModel
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.fragments.fragments.viewModels.SavedViewModel
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.whatsapp.viewModels.adapters.SavedAdapter
import java.util.*


class SavedFragment : Fragment() {

    private lateinit var rv: RecyclerView
    private lateinit var waAdapter: SavedAdapter
    private lateinit var viewModel: SavedViewModel

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
        waAdapter = SavedAdapter(requireContext())
        rv.layoutManager = GridLayoutManager(requireContext(), 3)
        rv.adapter = waAdapter
        viewModel = ViewModelProvider(this).get(SavedViewModel::class.java)
        viewModel.getMedia()
        viewModel.savedList.observe(viewLifecycleOwner) {
            waAdapter.setAdapter(it as ArrayList<SavedModel>)

        }



    }


}