package com.lion.a066EX_AnimalManager.fragment

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.checkbox.MaterialCheckBox
import android.widget.CompoundButton.OnCheckedChangeListener
import androidx.appcompat.app.AlertDialog
import com.lion.a066EX_AnimalManager.FragmentName
import com.lion.a066EX_AnimalManager.MainActivity
import com.lion.a066EX_AnimalManager.R
import com.lion.a066EX_AnimalManager.database.AnimalDataBase
import com.lion.a066EX_AnimalManager.databinding.FragmentModifyAnimalBinding
import com.lion.a066EX_AnimalManager.tools.HideKeyBoard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ModifyAnimalFragment : Fragment() {
    lateinit var fragmentModifyAnimalBinding: FragmentModifyAnimalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentModifyAnimalBinding = FragmentModifyAnimalBinding.inflate(inflater)

        // 키보드 숨기기 설정
        val hideKeyBoard = HideKeyBoard()
        hideKeyBoard.setupHideKeyboardOnTouch(fragmentModifyAnimalBinding.root)

        // 함수불러오기
        settingToolBar()
        settingEditText()
        settingSnack()
        settingPhoneNumberFormatting()
        settingCategory()
        settingGender()
        settingAgeAndWeight()

        return fragmentModifyAnimalBinding.root
    }
    private fun settingPhoneNumberFormatting(){
        fragmentModifyAnimalBinding.apply {
            editTextPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        }
    }

    private fun settingCategory() {
        fragmentModifyAnimalBinding.apply {
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
        fragmentModifyAnimalBinding.apply {
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

    private fun settingAgeAndWeight() {
        fragmentModifyAnimalBinding.apply {
            sliderAnimalAge.addOnChangeListener { slider, value, fromUser ->
                textViewAge.text = "나이 ${value.toInt()}살"
            }
            sliderAnimalWeight.addOnChangeListener { slider, value, fromUser ->
                textViewWeight.text = "몸무게 ${value}kg"
            }
        }

    }

    private fun settingToolBar() {
        fragmentModifyAnimalBinding.apply {
            materialToolbarModify.title = "수정"
            materialToolbarModify.isTitleCentered = true
            materialToolbarModify.inflateMenu(R.menu.menu_modify)

            materialToolbarModify.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.itemDone -> {
                        showDialog()
                    }
                }
                true
            }


            materialToolbarModify.setNavigationIcon(android.R.drawable.ic_menu_close_clear_cancel)
            materialToolbarModify.setNavigationOnClickListener {
                val a = activity as MainActivity
                a.removeFragment(FragmentName.MODIFY_ANIMAL_FRAGMENT)

            }
        }
    }

    private fun settingEditText() {
        fragmentModifyAnimalBinding.apply {
            if (arguments != null) {
                val idx = arguments?.getInt("idx")!!
                val a = activity as MainActivity
                val animalDataBase = AnimalDataBase.getInstance(a)

                CoroutineScope(Dispatchers.Main).launch {
                    val animalData = withContext(Dispatchers.IO) {
                        animalDataBase?.animalDataDao()?.selectAnimalDataOne(idx)
                    }

                    //데이터가 불러와진후에 메인쓰레드에서 UI업데이트를 하는것으로바꿈
                    //코루틴의 비동기 특성 때문에 EditText 필드에 데이터 설정이 지연되거나 반영되지 않을 수 있습니다.
                    // 데이터가 코루틴 내에서 불러와지는 동안 UI가 준비되지 않은 상태일 가능성이 있습니다.
                    //
                    //아래 수정된 코드에서는 withContext(Dispatchers.Main)을 추가하여 데이터가 불러와진 후 UI 업데이트가
                    // 메인 스레드에서 이루어지도록 보장합니다:
                    withContext(Dispatchers.Main) {
                        textInputLayoutOwnerName.editText?.setText(animalData?.ownerName.orEmpty())

                        editTextPhone.setText(animalData?.ownerPhoneNumber.orEmpty())

                        textInputLayoutAnimalName.editText?.setText(animalData?.animalName.orEmpty())

                        val category = animalData?.category.orEmpty()
                        if (category == "강아지") {
                            toggleGroupCategory.check(buttonDog.id)
                        }
                        if (category == "고양이") {
                            toggleGroupCategory.check(buttonCat.id)
                        }
                        if (category == "앵무새") {
                            toggleGroupCategory.check(buttonParrot.id)
                        }

                        val gender = animalData?.gender.orEmpty()
                        if (gender == "수컷") {
                            toggleGroupGender.check(buttonMale.id)
                        }
                        if (gender == "암컷") {
                            toggleGroupGender.check(buttonFemale.id)
                        }

                        //좋아하는간식
                        // animalData?.snack의 문자열을 단어로 분리하여 리스트로 저장
                        val snackList = animalData?.snack?.split(" ") ?: emptyList()

                        // 체크박스 상태 설정
                        // snackList에 모든 과일이 있는 경우 전체 체크박스 선택
                        checkBoxAll.isChecked = snackList.size == 3

                        checkBoxApple.isChecked = "사과" in snackList
                        checkBoxBanana.isChecked = "바나나" in snackList
                        checkBoxJujube.isChecked = "대추" in snackList


                        val age = animalData?.age
                        if (age != null) {
                            sliderAnimalAge.value = age.toFloat()
                        }
                        val weight = animalData?.weight
                        if (weight != null) {
                            sliderAnimalWeight.value = weight.toFloat()
                        }
                        val isNeutering = animalData?.neuteringSurgery
                        if (isNeutering == true) {
                            switchNeuteringSurgery.isChecked = true
                        } else {
                            switchNeuteringSurgery.isChecked = false
                        }


                    }
                }
            }
        }
    }

    private fun settingSnack() {
        fragmentModifyAnimalBinding.apply {
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

    private fun modify() {
        fragmentModifyAnimalBinding.apply {
            if (arguments != null) {
                val idx = arguments?.getInt("idx")!!
                val ownerName: String = textInputLayoutOwnerName.editText?.text.toString()

                val ownerPhoneNumber: String = editTextPhone.text.toString()

                val animalName: String = textInputLayoutAnimalName.editText?.text.toString()

                val category: String = when (toggleGroupCategory.checkedButtonId) {
                    buttonDog.id -> "강아지"
                    buttonCat.id -> "고양이"
                    buttonParrot.id -> "앵무새"
                    else -> {}
                }.toString()

                val gender: String = when (toggleGroupGender.checkedButtonId) {
                    buttonMale.id -> "수컷"
                    buttonFemale.id -> "암컷"
                    else -> ({}).toString()
                }

                var snack: String = ""
                if (checkBoxApple.isChecked) snack += " 사과 "
                if (checkBoxBanana.isChecked) snack += " 바나나 "
                if (checkBoxJujube.isChecked) snack += " 대추 "

                var age: Int = sliderAnimalAge.value.toInt()

                val weight: Int = sliderAnimalWeight.value.toInt()

                val isNeutered: Boolean = switchNeuteringSurgery.isChecked

                val activity = activity as MainActivity

                val animalDataBase = AnimalDataBase.getInstance(activity)

                CoroutineScope(Dispatchers.Main).launch {
                    async(Dispatchers.IO) {
                        animalDataBase?.animalDataDao()?.updateAnimalData(
                            category,
                            animalName,
                            gender,
                            age,
                            snack,
                            isNeutered,
                            ownerName,
                            ownerPhoneNumber,
                            weight,
                            idx
                        )
                    }
                }
                activity.removeFragment(FragmentName.MODIFY_ANIMAL_FRAGMENT)
            }

        }
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("수정된 내용을 저장")
            .setMessage("수정된 내용을 저장하시겠습니까?")
            .setPositiveButton("네") { dialog, _ ->
                modify()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }


}