package com.example.practicaretrofitcrud.api.dto

import java.io.Serializable

typealias Books = ArrayList<Book>

data class Book(
    var nombre: String,
    var autor: String,
    var editorial: String,
    var imagen: String,
    var sinopsis: String,
    var isbn: String,
    var id: Long = 0,
    var calificacion: Long = 0,
    var generos: List<Genero> = emptyList()
) : Serializable {
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        0,
        0,
        emptyList()
    )
}

data class Genero(
    val id: Long,
    val nombre: String,
): Serializable

