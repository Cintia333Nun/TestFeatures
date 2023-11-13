package com.cin.testfeatures.tabs_recycler.datasource

class DataSourceClothes(private val type: TypeClothes) {

    fun getDataClothes(): List<ModelClothes> {
        return when (type) {
            TypeClothes.DRESS -> getClothesDress()
            TypeClothes.SHIRT -> getClothesShit()
            TypeClothes.JEANS -> getClothesJeans()
        }
    }

    private fun getClothesDress(): List<ModelClothes> {
        return listOf(
            ModelClothes(
                "Vestido de playa",
                "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/vestidos-flores-kendall-jenner-selena-gomez-2-1617698719.jpeg",
                "Unico color\nTallas: CH,M,G,EG,EEG,"
            ),
            ModelClothes(
                "Vestido de gala",
                "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/vestidos-flores-kendall-jenner-selena-gomez-2-1617698719.jpeg",
                "Unico color\nTallas: CH,M,G,EG,EEG,"
            ),
            ModelClothes(
                "Vestido",
                "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/vestidos-flores-kendall-jenner-selena-gomez-2-1617698719.jpeg",
                "Unico color\nTallas: CH,M,G,EG,EEG,"
            ),
        )
    }

    private fun getClothesShit(): List<ModelClothes> {
        return listOf(
            ModelClothes(
                "Playera",
                "https://cdn.shopify.com/s/files/1/0405/9811/0367/products/14-Verde-800x800.jpg?v=1605882871",
                "Unisex\nVarios colores\nTallas varias\nColores:\nNegra\nBlanco\nAzul\nMorado\nGris\nTalla: G,EG, EEG, EEEG"
            ),
        )
    }

    private fun getClothesJeans(): List<ModelClothes> {
        return listOf(
            ModelClothes(
                "Pantalones",
                "https://contents.mediadecathlon.com/p1786958/k\$2b0a8a97ea3b1154f2f3734009451fe2/pantalon-montana-y-trekking-viaje-forclaz-100-hombre-gris.jpg?&f=800x800",
                "Mujer\nTiro 3/4\nCoordinar\nColores:\nAzul\nRosa\nMorado\nNegro\nTalla: M,G,CH,EG"
            ),
            ModelClothes(
                "Pantalones cuadrados",
                "https://contents.mediadecathlon.com/p1786958/k\$2b0a8a97ea3b1154f2f3734009451fe2/pantalon-montana-y-trekking-viaje-forclaz-100-hombre-gris.jpg?&f=800x800",
                "Mujer\nTiro 3/4\nCoordinar\nColores:\nAzul\nRosa\nMorado\nNegro\nTalla: M,G,CH,EG"
            ),
            ModelClothes(
                "Pantalon formal",
                "https://contents.mediadecathlon.com/p1786958/k\$2b0a8a97ea3b1154f2f3734009451fe2/pantalon-montana-y-trekking-viaje-forclaz-100-hombre-gris.jpg?&f=800x800",
                "Mujer\nTiro 3/4\nCoordinar\nColores:\nAzul\nRosa\nMorado\nNegro\nTalla: M,G,CH,EG"
            ),
            ModelClothes(
                "Pantalon casual",
                "https://contents.mediadecathlon.com/p1786958/k\$2b0a8a97ea3b1154f2f3734009451fe2/pantalon-montana-y-trekking-viaje-forclaz-100-hombre-gris.jpg?&f=800x800",
                "Mujer\nTiro 3/4\nCoordinar\nColores:\nAzul\nRosa\nMorado\nNegro\nTalla: M,G,CH,EG"
            ),
            ModelClothes(
                "Pantalon mezclilla",
                "https://contents.mediadecathlon.com/p1786958/k\$2b0a8a97ea3b1154f2f3734009451fe2/pantalon-montana-y-trekking-viaje-forclaz-100-hombre-gris.jpg?&f=800x800",
                "Mujer\nTiro 3/4\nCoordinar\nColores:\nAzul\nRosa\nMorado\nNegro\nTalla: M,G,CH,EG"
            ),
        )
    }
}