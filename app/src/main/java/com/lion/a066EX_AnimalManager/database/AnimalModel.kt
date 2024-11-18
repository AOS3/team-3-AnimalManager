package com.lion.a066EX_AnimalManager.database

import androidx.room.Entity
import androidx.room.PrimaryKey

//사용자가 공란을 입력하더라도 빈칸으로 들어가게끔 기본값을정함
@Entity(tableName = "AnimalTable")
data class AnimalModel(
    val ownerName: String = "",
    val ownerPhoneNumber: String = "",
    val animalName: String = "",
    val category: String = "",
    val gender: String = "",
    var snack: String = "",
    val age: Int = 0,
    val weight: Int = 0,
    val neuteringSurgery: Boolean = false,
) {
    @PrimaryKey(autoGenerate = true)
    var idx: Int = 0
}
