package com.lion.a066EX_AnimalManager.fragment

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lion.a066EX_AnimalManager.databinding.FragmentInputAnimalBinding
import android.widget.CompoundButton.OnCheckedChangeListener
import androidx.appcompat.app.AlertDialog
import com.google.android.material.checkbox.MaterialCheckBox
import com.lion.a066EX_AnimalManager.FragmentName
import com.lion.a066EX_AnimalManager.tools.HideKeyBoard
import com.lion.a066EX_AnimalManager.MainActivity
import com.lion.a066EX_AnimalManager.R
import com.lion.a066EX_AnimalManager.database.AnimalDataBase
import com.lion.a066EX_AnimalManager.database.AnimalModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class InputAnimalFragment : Fragment() {

    lateinit var fragmentInputAnimalBinding: FragmentInputAnimalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentInputAnimalBinding = FragmentInputAnimalBinding.inflate(inflater)

        // 키보드 숨기기 설정
        val hideKeyBoard = HideKeyBoard()
        hideKeyBoard.setupHideKeyboardOnTouch(fragmentInputAnimalBinding.root)

        //함수불러오기
        settingCategory()
        settingGender()
        settingSnack()
        settingAgeAndWeight()
        settingToolBar()
        settingPhoneNumberFormatting()


        return fragmentInputAnimalBinding.root
    }
    private fun settingPhoneNumberFormatting(){
        fragmentInputAnimalBinding.apply {
            editTextPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        }
    }

    private fun settingCategory() {
        fragmentInputAnimalBinding.apply {
            buttonDog.setOnClickListener {
                if (toggleGroupCategory.checkedButtonId != buttonDog.id) {
                    toggleGroupCategory.check(buttonDog.id)
                }
            }
            buttonCat.setOnClickListener {
                if (toggleGroupCategory.checkedButtonId != buttonCat.id) {
                    toggleGroupCategory.check(buttonCat.id)
                }
            }
            buttonParrot.setOnClickListener {
                if (toggleGroupCategory.checkedButtonId != buttonParrot.id) {
                    toggleGroupCategory.check(buttonParrot.id)
                }
            }
        }
    }

    private fun settingGender() {
        fragmentInputAnimalBinding.apply {
            buttonMale.setOnClickListener {
                if (toggleGroupGender.checkedButtonId != buttonMale.id) {
                    toggleGroupGender.check(buttonMale.id)
                }
            }
            buttonFemale.setOnClickListener {
                if (toggleGroupGender.checkedButtonId != buttonFemale.id) {
                    toggleGroupGender.check(buttonFemale.id)
                }
            }
        }


    }

    private fun settingSnack() {
        fragmentInputAnimalBinding.apply {
            // 이벤트 동작 여부 변수
            var isParentUpdating = true
            var isChildrenUpdating = true
            // 전체 체크 박스
            // 체크 상태가 변경되었을 때
            // state : 체크 상태값
            checkBoxAll.addOnCheckedStateChangedListener { checkBox, state ->

                // 동물 체크박스의 이벤트 동작을 막았다면 종료시킨다
                if (isParentUpdating == false) {
                    return@addOnCheckedStateChangedListener
                }

                // 하위 체크박스들의 이벤트 동작을 막아준다.
                isChildrenUpdating = false

                when (state) {
                    // 체크 상태일 때
                    MaterialCheckBox.STATE_CHECKED -> {
                        // 모든 체크박스를 체크한다.
                        checkBoxApple.isChecked = true
                        checkBoxBanana.isChecked = true
                        checkBoxJujube.isChecked = true
                    }
                    // 체크 상태가 아닐 때
                    MaterialCheckBox.STATE_UNCHECKED -> {
                        // 모든 체크스를 체크 해제한다.
                        checkBoxApple.isChecked = false
                        checkBoxBanana.isChecked = false
                        checkBoxJujube.isChecked = false
                    }
                }

                // 하위 체크박스들의 리스너 동작을 풀어준다.
                isChildrenUpdating = true
            }

            // 체크박스들에 설정할 이벤트 람다식
            val checkBoxListener = OnCheckedChangeListener { buttonView, isChecked ->

                // 하위 체크박스의 리스너 동작을 막았다면 중단시킨다
                if (isChildrenUpdating == false) {
                    return@OnCheckedChangeListener
                }

                // 동물 체크박스의 리스너 동작을 막아준다.
                isParentUpdating = false

                // 체크되어 있는 체크박스의 개수를 담을 변수
                var checkedCount = 0

                // 체크박스들을 검사한다.
                if (checkBoxApple.isChecked) {
                    checkedCount++
                }
                if (checkBoxBanana.isChecked) {
                    checkedCount++
                }
                if (checkBoxJujube.isChecked) {
                    checkedCount++
                }

                // 체크박스 개수에 따라 상태를 설정한다.
                checkBoxAll.checkedState = if (checkedCount == 0) {
                    MaterialCheckBox.STATE_UNCHECKED
                } else if (checkedCount == 3) {
                    MaterialCheckBox.STATE_CHECKED
                } else {
                    MaterialCheckBox.STATE_INDETERMINATE
                }

                // 동물 체크박스의 리스너 동작을 허용한다.
                isParentUpdating = true
            }
            // 모든 체크박스에 설정한다
            checkBoxApple.setOnCheckedChangeListener(checkBoxListener)
            checkBoxBanana.setOnCheckedChangeListener(checkBoxListener)
            checkBoxJujube.setOnCheckedChangeListener(checkBoxListener)

        }
    }

    private fun settingToolBar() {
        fragmentInputAnimalBinding.apply {
            materialToolbarInput.title = "입력"
            materialToolbarInput.isTitleCentered = true

            materialToolbarInput.inflateMenu(R.menu.menu_input)


            materialToolbarInput.setOnMenuItemClickListener {

                when (it.itemId) {
                    R.id.itemSave -> {
                        showDialog()
                    }
                }
                true
            }
            materialToolbarInput.setNavigationIcon(android.R.drawable.ic_menu_close_clear_cancel)
            materialToolbarInput.setNavigationOnClickListener {
                val a = activity as MainActivity
                a.removeFragment(FragmentName.INPUT_ANIMAL_FRAGMENT)
            }
        }
    }

    private fun settingAgeAndWeight() {
        fragmentInputAnimalBinding.apply {
            sliderAnimalAge.addOnChangeListener { slider, value, fromUser ->
                textViewAge.text = "나이 ${value.toInt()}살"
            }
            sliderAnimalWeight.addOnChangeListener { slider, value, fromUser ->
                textViewWeight.text = "몸무게 ${value}kg"
            }
        }

    }

    private fun save() {
        fragmentInputAnimalBinding.apply {
            val ownerName:String = textInputLayoutOwnerName.editText?.text.toString()

            val ownerPhoneNumber:String = editTextPhone.text.toString()

            val animalName:String = textInputLayoutAnimalName.editText?.text.toString()

            val category:String = when(toggleGroupCategory.checkedButtonId){
                buttonDog.id -> "강아지"
                buttonCat.id -> "고양이"
                buttonParrot.id -> "앵무새"
                else -> {}
            }.toString()

            val gender:String = when(toggleGroupGender.checkedButtonId){
                buttonMale.id -> "수컷"
                buttonFemale.id -> "암컷"
                else -> ({}).toString()
            }

            var snack:String = ""
            if (checkBoxApple.isChecked) snack += " 사과 "
            if (checkBoxBanana.isChecked) snack += " 바나나 "
            if (checkBoxJujube.isChecked) snack += " 대추 "

            var age:Int = sliderAnimalAge.value.toInt()

            val weight:Int = sliderAnimalWeight.value.toInt()

            val isNeutered: Boolean = switchNeuteringSurgery.isChecked

            val activity = activity as MainActivity
            val animalModel =
                AnimalModel(ownerName,ownerPhoneNumber,animalName,category, gender, snack, age,weight,isNeutered)

            val animalDataBase = AnimalDataBase.getInstance(activity)

            CoroutineScope(Dispatchers.Main).launch {
                async(Dispatchers.IO) {
                    animalDataBase?.animalDataDao()?.insertAnimalData(animalModel)
                }
                activity.removeFragment(FragmentName.INPUT_ANIMAL_FRAGMENT)
            }

        }
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("저장")
            .setMessage("저장하시겠습니까?")
            .setPositiveButton("네") { dialog, _ ->
                save()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

}
