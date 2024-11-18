package com.lion.a066EX_AnimalManager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.lion.a066EX_AnimalManager.FragmentName
import com.lion.a066EX_AnimalManager.MainActivity
import com.lion.a066EX_AnimalManager.R
import com.lion.a066EX_AnimalManager.database.AnimalDataBase
import com.lion.a066EX_AnimalManager.database.AnimalModel
import com.lion.a066EX_AnimalManager.databinding.FragmentShowAnimalBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ShowAnimalFragment : Fragment() {
    lateinit var fragmentShowAnimalBinding: FragmentShowAnimalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentShowAnimalBinding = FragmentShowAnimalBinding.inflate(inflater)

        setting()
        settingTextView()

        return fragmentShowAnimalBinding.root
    }

    private fun setting() {
        fragmentShowAnimalBinding.apply {
            materialToolbarShow.title = "보기"
            materialToolbarShow.isTitleCentered = true


            materialToolbarShow.inflateMenu(R.menu.menu_show)

            materialToolbarShow.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.itemDelete -> {
                        showDialogDelete()
                    }

                    R.id.itemModify -> {
                        showDialogModify()
                    }
                }
                true
            }

            materialToolbarShow.setNavigationIcon(android.R.drawable.ic_menu_close_clear_cancel)
            materialToolbarShow.setNavigationOnClickListener {
                val activity = activity as MainActivity
                activity.removeFragment(FragmentName.SHOW_ANIMAL_FRAGMENT)
            }
        }
    }

    private fun settingTextView() {
        fragmentShowAnimalBinding.apply {
            if (arguments != null) {
                val idx = arguments?.getInt("idx")!!

                val activity = activity as MainActivity
                val animalDataBase = AnimalDataBase.getInstance(activity)

                CoroutineScope(Dispatchers.Main).launch {
                    val work1 = async(Dispatchers.IO) {
                        animalDataBase?.animalDataDao()?.selectAnimalDataOne(idx)
                    }
                    val animalModel = work1.await() as AnimalModel

                    when(animalModel.category){
                        "강아지"->{imageViewAnimal.setImageResource(R.drawable.dog)}
                        "고양이"->{imageViewAnimal.setImageResource(R.drawable.cat)}
                        else->{imageViewAnimal.setImageResource(R.drawable.parrot)}
                    }

                    textViewAnimalName.append(animalModel.animalName)
                    textViewAnimalCategory.append(animalModel.category)
                    textViewAnimalGender.append(animalModel.gender)
                    textViewSnack.append(animalModel.snack)
                    textViewAnimalAge.append("${animalModel.age}살")
                    textViewAnimalWeight.append("${animalModel.weight}kg")

                    when(animalModel.neuteringSurgery){
                        true ->textViewNeuteringSurgery.append("완료")
                        else ->textViewNeuteringSurgery.append("미완료")
                    }

                    textViewOwnerName.append(animalModel.ownerName)
                    textViewOwnerPhoneNumber.append(animalModel.ownerPhoneNumber)
                }
            }
        }
    }

    private fun delete() {
        fragmentShowAnimalBinding.apply {
            if (arguments != null) {
                val idx = arguments?.getInt("idx")!!

                val activity = activity as MainActivity
                val animalDataBase = AnimalDataBase.getInstance(activity)

                CoroutineScope(Dispatchers.Main).launch {
                    async(Dispatchers.IO) {
                        animalDataBase?.animalDataDao()?.deleteAnimalData(idx)
                    }
                    activity.removeFragment(FragmentName.SHOW_ANIMAL_FRAGMENT)
                }
            }
        }
    }

    private fun showDialogDelete() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("삭제")
            .setMessage("삭제하시겠습니까?")
            .setPositiveButton("네") { dialog, _ ->
                delete()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->

                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showDialogModify() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("수정")
            .setMessage("수정하시겠습니까?")
            .setPositiveButton("네") { dialog, _ ->
                if (arguments != null) {
                    val idx = arguments?.getInt("idx")!!
                    val dataBundle = Bundle()
                    dataBundle.putInt("idx", idx)
                    val activity = activity as MainActivity
                    activity.replaceFragment(
                        FragmentName.MODIFY_ANIMAL_FRAGMENT,
                        true,
                        dataBundle
                    )
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