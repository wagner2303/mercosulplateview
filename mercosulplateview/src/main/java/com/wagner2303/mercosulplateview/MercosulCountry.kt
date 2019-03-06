package com.wagner2303.mercosulplateview

enum class MercosulCountry(val id: Int, val countryName: String, val flagRes: Int?, val mercosurLogoRes: Int) {
    BRASIL(
        id = 0,
        countryName = "BRASIL",
        flagRes = R.drawable.flag_brasil,
        mercosurLogoRes = R.drawable.logo_mercosul
    ),
    ARGENTINA(
        id = 1,
        countryName = "REPUBLICA ARGENTINA",
        flagRes = R.drawable.flag_argentina,
        mercosurLogoRes = R.drawable.logo_mercosur
    ),
    URUGUAY(
        id = 2,
        countryName = "URUGUAY",
        flagRes = R.drawable.flag_uruguay,
        mercosurLogoRes = R.drawable.logo_mercosur
    ),
    PARAGUAY(
        id = 3,
        countryName = "REPUBLICA DEL PARAGUAY",
        flagRes = R.drawable.flag_paraguay,
        mercosurLogoRes = R.drawable.logo_mercosur
    ),
    VENEZUELA(
        id = 4,
        countryName = "REPUBLICA BOLIVARIANA DE VENEZUELA",
        flagRes = null,
        mercosurLogoRes = R.drawable.logo_mercosur
    );

    companion object {
        fun fromId(id: Int): MercosulCountry? = values().find { it.id == id }
    }
}