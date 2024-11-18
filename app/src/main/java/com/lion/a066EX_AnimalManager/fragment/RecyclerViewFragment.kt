package com.lion.a066EX_AnimalManager.fragment

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lion.a066EX_AnimalManager.DataList
import com.lion.a066EX_AnimalManager.FragmentName
import com.lion.a066EX_AnimalManager.MainActivity
import com.lion.a066EX_AnimalManager.R
import com.lion.a066EX_AnimalManager.database.AnimalDataBase
import com.lion.a066EX_AnimalManager.database.AnimalModel
import com.lion.a066EX_AnimalManager.databinding.FragmentRecyclerViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RecyclerViewFragment : Fragment() {
    lateinit var fragmentRecyclerViewBinding: FragmentRecyclerViewBinding

    var dataList = DataList.animalList


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentRecyclerViewBinding = FragmentRecyclerViewBinding.inflate(layoutInflater)


        // 리싸이클러뷰
        settingRecyclerViewMain()
        // 플로팅버튼
        settingFabMain()
        // 데이터를 읽어와 RecyclerView를 갱신
        refreshRecyclerView()

        fragmentRecyclerViewBinding.apply {
            materialToolbarRecycler.title = "동물정보"
            materialToolbarRecycler.isTitleCentered = true
            materialToolbarRecycler.inflateMenu(R.menu.menu_main)

            materialToolbarRecycler.setOnMenuItemClickListener {

                when (it.itemId) {
                    R.id.main_toolbar_menu_all_delete -> {
                        showDeleteConfirmationDialog()
                    }
                }
                true
            }
        }

        return fragmentRecyclerViewBinding.root
    }

    private fun settingRecyclerViewMain() {
        fragmentRecyclerViewBinding.apply {
            // 어뎁터 셋팅
            val recyclerViewAdapter = RecyclerViewAdapter(dataList, this@RecyclerViewFragment)
            recyclerViewMain.adapter = recyclerViewAdapter
            recyclerViewMain.layoutManager = LinearLayoutManager(activity)
            // 구분선
//            val deco = MaterialDividerItemDecoration(
//                requireActivity(),
//                MaterialDividerItemDecoration.VERTICAL
//            )
//            recyclerViewMain.addItemDecoration(deco)
        }
    }

    private fun settingFabMain() {
        fragmentRecyclerViewBinding.apply {
            floatingActionButton.setOnClickListener {
                val a1 = activity as MainActivity
                a1.replaceFragment(FragmentName.INPUT_ANIMAL_FRAGMENT, true, null)
            }
        }
    }

    private fun refreshRecyclerView() {
        val mainActivity = activity as MainActivity
        val animalDataBase = AnimalDataBase.getInstance(mainActivity)
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                animalDataBase?.animalDataDao()?.selectAnimalDataAll()
            }
            dataList = work1.await() as MutableList<AnimalModel>

            fragmentRecyclerViewBinding.recyclerViewMain.adapter =
                RecyclerViewAdapter(dataList, this@RecyclerViewFragment)
        }
    }

    private fun showDeleteConfirmationDialog() {
        val activity = activity as MainActivity
        val animalDataBase = AnimalDataBase.getInstance(activity)
        CoroutineScope(Dispatchers.Main).launch {
            val empty = withContext(Dispatchers.IO) {
                animalDataBase?.animalDataDao()?.selectAnimalDataAll() ?: mutableListOf()
            }

            if (empty.isEmpty()) {
                Toast.makeText(requireContext(), "등록된 동물정보가 없습니다", Toast.LENGTH_SHORT).show()
            } else {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("삭제")
                    .setMessage("모두 삭제 하시겠습니까?")
                    .setPositiveButton("네") { dialog, _ ->
                        CoroutineScope(Dispatchers.Main).launch {
                            withContext(Dispatchers.IO) {
                                animalDataBase?.animalDataDao()?.deleteAllAnimalData()
                            }
                            refreshRecyclerView()
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("취소") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }
    }

}

