package com.lion.a066EX_AnimalManager.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DataDaoAnimal {

    @Insert
    fun insertAnimalData(animalDataModel: AnimalModel)

    @Query("select * from AnimalTable")
    fun selectAnimalDataAll(): List<AnimalModel>

    @Query("select * from AnimalTable where idx = :idx")
    fun selectAnimalDataOne(idx: Int): AnimalModel

    @Query("delete from AnimalTable where idx = :idx")
    fun deleteAnimalData(idx: Int)

    @Query(
        "UPDATE AnimalTable SET " +
                "ownerName = :ownerName, " +
                "ownerPhoneNumber = :ownerPhoneNumber, " +
                "animalName = :animalName, " +
                "category = :category, " +
                "gender = :gender, " +
                "snack = :snack, " +
                "age = :age, " +
                "weight = :weight, " +
                "neuteringSurgery = :neuteringSurgery " +
                "WHERE idx = :idx"
    )
    fun updateAnimalData(
        category: String,
        animalName: String,
        gender: String,
        age: Int,
        snack: String,
        neuteringSurgery: Boolean,
        ownerName: String,
        ownerPhoneNumber: String,
        weight: Int,
        idx: Int
    )

    // 동물 테이블의 모든 데이터를 삭제하는 메서드
    @Query("DELETE FROM AnimalTable")
    fun deleteAllAnimalData()
}


//@Entity(tableName = "AnimalTable")
//data class AnimalModel(
//    val ownerName: String = "",
//    val ownerPhoneNumber: String = "",
//    val name: String = "",
//    val category: String = "",
//    val gender: String = "",
//    var snack: String = "",
//    val age: Int = 0,
//    val weight: Int =0,
//    val neuteringSurgery: Boolean = false,
//
//    ) {
//    @PrimaryKey(autoGenerate = true)
//    var idx: Int = 0
//}
