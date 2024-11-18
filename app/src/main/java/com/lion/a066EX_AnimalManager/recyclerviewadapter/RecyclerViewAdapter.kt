package com.lion.a066EX_AnimalManager.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lion.a066EX_AnimalManager.FragmentName
import com.lion.a066EX_AnimalManager.MainActivity
import com.lion.a066EX_AnimalManager.R
import com.lion.a066EX_AnimalManager.database.AnimalModel
import com.lion.a066EX_AnimalManager.databinding.RowBinding

class RecyclerViewAdapter(
    private val dataList: List<AnimalModel>,
    private val fragment: RecyclerViewFragment
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(var rowBinding: RowBinding) : RecyclerView.ViewHolder(rowBinding.root),
        OnClickListener {
        override fun onClick(v: View?) {
            if (dataList.isNotEmpty()) {
                val activity = fragment.activity as MainActivity
                val dataBundle = Bundle()
                dataBundle.putInt("idx", dataList[adapterPosition].idx)
                activity.replaceFragment(FragmentName.SHOW_ANIMAL_FRAGMENT, true, dataBundle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowBinding = RowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(rowBinding)
        rowBinding.root.setOnClickListener(viewHolder)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return if (dataList.isEmpty()) 1 else dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (dataList.isEmpty()) {
            holder.rowBinding.textViewRow.text = "등록된 동물정보가\n 없습니다"
            holder.rowBinding.imageView.setImageResource(R.drawable.livestock)
        } else {
            when (dataList[position].category) {
                "강아지" -> {holder.rowBinding.imageView.setImageResource(R.drawable.blackdog)}
                "고양이" -> {holder.rowBinding.imageView.setImageResource(R.drawable.blackcat)}
                else -> {holder.rowBinding.imageView.setImageResource(R.drawable.blackparrot)}
            }

            holder.rowBinding.textViewRow.text = dataList[position].animalName
        }
    }
}
