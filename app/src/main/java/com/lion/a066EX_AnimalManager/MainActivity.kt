package com.lion.a066EX_AnimalManager

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.android.material.transition.MaterialSharedAxis

import com.lion.a066EX_AnimalManager.fragment.InputAnimalFragment

import com.lion.a066EX_AnimalManager.fragment.RecyclerViewFragment

import com.lion.a066EX_AnimalManager.fragment.ModifyAnimalFragment
import com.lion.a066EX_AnimalManager.fragment.ShowAnimalFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // 프래그먼트를 교체하는 함수
    fun replaceFragment(
        fragmentName: FragmentName,
        isAddToBackStack: Boolean,
        dataBundle: Bundle?
    ){



        // 프래그먼트 객체
        val newFragment = when (fragmentName) {
            FragmentName.RECYCLERVIEW_FRAGMENT -> RecyclerViewFragment()
            FragmentName.INPUT_ANIMAL_FRAGMENT -> InputAnimalFragment()
            FragmentName.SHOW_ANIMAL_FRAGMENT -> ShowAnimalFragment()
            FragmentName.MODIFY_ANIMAL_FRAGMENT -> ModifyAnimalFragment()
        }

        // bundle 객체가 null이 아니라면
        if (dataBundle != null) {
            newFragment.arguments = dataBundle
        }

        // 프래그먼트 교체
        supportFragmentManager.commit {

            newFragment.exitTransition =
                MaterialSharedAxis(MaterialSharedAxis.Y, /* forward= */ true)
            newFragment.reenterTransition =
                MaterialSharedAxis(MaterialSharedAxis.Y, /* forward= */ false)
            newFragment.enterTransition =
                MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
            newFragment.returnTransition =
                MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)


            replace(R.id.fragmentContainerViewMain, newFragment)
            if (isAddToBackStack) {
                addToBackStack(fragmentName.str)
            }
        }
    }

    // 프래그먼트를 BackStack에서 제거하는 메서드
    fun removeFragment(fragmentName: FragmentName) {
        supportFragmentManager.popBackStack(
            fragmentName.str,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }
}



// 프래그먼트들을 나타내는 값들
enum class FragmentName(var number:Int, var str:String){
    RECYCLERVIEW_FRAGMENT(1, "RecyclerViewFragment"),
    INPUT_ANIMAL_FRAGMENT(4,"InputAnimalFragment"),
    SHOW_ANIMAL_FRAGMENT(5,"ShowAnimalFragment"),
    MODIFY_ANIMAL_FRAGMENT(6,"ModifyAnimalFragment")
}


