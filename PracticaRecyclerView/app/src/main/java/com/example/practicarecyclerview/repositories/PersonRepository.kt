package com.example.practicarecyclerview.repositories

import com.example.practicarecyclerview.models.Person

object PersonRepository {
    private val data = mutableListOf(
        Person(1, "John", "Doe", 25, "1234567890", "jhon@test.com"),
        Person(2, "Jane", "Doe", 30, "123456780", "jane@test.com"),
        Person(3, "Foo", "Bar", 35, "123456790", "foo@test.com"),
        Person(4, "Juan", "Perez", 40, "123456890", "juan@test.com"),
        Person(5, "Maria", "Gonzalez", 20, "123457890", "maria@test.com"),
        Person(6, "Pedro", "Jimenez", 50, "123467890", "pedro@test.com"),
        Person(7, "Luis", "Gomez", 45, "123567890", "luis@test.com"),
        Person(8, "John", "Doe", 25, "124567890", "jhon@test.com"),
        Person(9, "Jane", "Doe", 30, "134567890", "jane@test.com"),
        Person(10, "Foo", "Bar", 35, "234567890", "foo@test.com"),
        Person(11, "Juan", "Perez", 40, "01234567890", "juan@test.com"),
        Person(12, "Maria", "Gonzalez", 20, "0234567890", "maria@test.com"),
        Person(13, "Pedro", "Jimenez", 50, "034567890", "pedro@test.com"),
        Person(14, "Luis", "Gomez", 45, "04567890", "luis@test.com"),
        Person(15, "John", "Doe", 25, "0567890", "jhon@test.com"),
        Person(16, "Jane", "Doe", 30, "067890", "jane@test.com"),
        Person(17, "Foo", "Bar", 35, "0890", "foo@test.com"),
        Person(18, "Juan", "Perez", 40, "01234567890", "juan@test.com"),
        Person(19, "Maria", "Gonzalez", 20, "011234567890", "maria@test.com"),
        Person(20, "Pedro", "Jimenez", 50, "0111234567890", "pedro@test.com"),
        Person(21, "Luis", "Gomez", 45, "0111234567890", "luis@test.com"),

        )

    fun getPersonList(): List<Person> {
        return data
    }

    fun getPersonById(id: Int): Person? {
        return data.find { it.id == id }
    }
}