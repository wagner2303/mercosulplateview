package com.wagner2303.mercosulplateview

enum class MercosulPlateType (val id: Int, val colorId: Int) {
    Particular (id = 0, colorId = R.color.particular),
    Comercial (id = 1, colorId = R.color.comercial),
    Especial (id = 2, colorId = R.color.especial),
    Oficial (id = 3, colorId = R.color.oficial),
    Diplomatico (id = 4, colorId = R.color.diplomatico),
    Colecionador (id = 5, colorId = R.color.colecionador);

    companion object {
        fun fromId(id: Int): MercosulPlateType? = values().find { it.id == id }
    }
}
