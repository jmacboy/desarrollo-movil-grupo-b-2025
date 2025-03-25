package com.example.practicaroom.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Person(
    var name: String,
    var lastName: String,
    var age: Int,
    var phone: String,
    var email: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}