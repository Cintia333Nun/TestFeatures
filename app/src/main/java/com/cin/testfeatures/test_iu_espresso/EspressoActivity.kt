package com.cin.testfeatures.test_iu_espresso

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cin.testfeatures.R
import com.cin.testfeatures.databinding.ActivityEspressoBinding

/**
 * Espresso es un framework de pruebas de IU que se utiliza para escribir pruebas concisas y
 * fáciles de leer. Proporciona métodos para interactuar con los elementos de la interfaz de
 * usuario, como botones, campos de texto, y realizar acciones como hacer clic, escribir texto y
 * desplazarse por la pantalla.
 * También puedes verificar el estado de los elementos, como el texto mostrado o la visibilidad.
 * Espresso es ampliamente utilizado y se integra bien con el marco de pruebas JUnit.
 *
 * Para comenzar con las pruebas de IU automatizadas, debes crear casos de prueba en el directorio
 * androidTest de tu proyecto. Allí, puedes escribir los métodos de prueba y utilizar las API
 * proporcionadas por Espresso para interactuar con la interfaz de usuario y verificar el
 * comportamiento esperado.
 *
 *
 *
 * COMO AGREGAR ESPRESSO A MI PROYECTO
 * 1. Agregar Gradle nivel app:
 *      androidTestImplementation 'androidx.test.espresso:espresso-core:<version>'
 *      androidTestImplementation 'androidx.test.espresso:espresso-contrib:<version>'
 *      androidTestImplementation 'androidx.test.espresso:espresso-intents:<version>'
 * 2. Agrega la siguiente dependencia para JUnit:
 *      androidTestImplementation 'androidx.test.ext:junit:<version>'
 * 3. Sincroniza los cambios en tu proyecto para descargar las dependencias necesarias.
 *
 *
 *
 * Realizar pruebas:
 * @see com.cin.testfeatures.EspressoActivityTest
 * */
class EspressoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEspressoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initListeners()
    }

    private fun initBinding() {
        binding = ActivityEspressoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initListeners() {
        binding.buttonTest.setOnClickListener {
            binding.buttonTest.text = getString(R.string.test_espresso_finish)
        }
    }
}