package com.example.practicaroom.repositories

import android.content.Context
import com.example.practicaroom.db.models.Person

object PersonRepository {
    private suspend fun insertPerson(context: Context, person: Person): Long {
        return RoomRepository
            .getRoomInstance(context)
            .personDao()
            .insertPerson(person)
    }

    suspend fun getPersonById(context: Context, id: Int): Person {
        return RoomRepository
            .getRoomInstance(context)
            .personDao()
            .getById(id)
    }

    suspend fun getPersonList(context: Context): List<Person> {
        return RoomRepository
            .getRoomInstance(context)
            .personDao()
            .getAll()
    }

    suspend fun deletePerson(context: Context, person: Person) {
        RoomRepository
            .getRoomInstance(context)
            .personDao()
            .deletePerson(person)
    }

    suspend fun savePerson(context: Context, person: Person): Int {
        if (person.id == 0) {
            return insertPerson(context, person).toInt()
        } else {
            updatePerson(context, person)
            return person.id
        }
    }

    private suspend fun updatePerson(context: Context, person: Person) {
        return RoomRepository
            .getRoomInstance(context)
            .personDao()
            .updatePerson(person)
    }
}