package com.cin.testfeatures.domain

import com.cin.testfeatures.clean_mvvm_dagger.data.QuoteRepository
import com.cin.testfeatures.clean_mvvm_dagger.domain.GetRandomQuoteUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetRandomQuoteUseCaseTest {
    /**
     *
     * Objetivo de la clase:
     * Probar clase que modela funcionalidad de Objeto Fake
     * Atributos:
     * @MockK -> Controlar todas las posibles respuestas de la clase Mockeada
     * @RelaxedMockK -> El sistema proporciona una resuesta por defecto
     *
     * ATRIBUTOS -> CLASES A PROBAR E INSTANCIA DE CASO DE PRUEBA
     *
     * */

    @RelaxedMockK
    private lateinit var quoteRepository: QuoteRepository
    lateinit var getRandomQuoteUseCase: GetRandomQuoteUseCase

    /**
     * CONFIGURACION EN @Before donde se:
     * Configura libreria Mockk
     * Inicializar Obj a testear con repositorio Mockkeado
     * */

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getRandomQuoteUseCase = GetRandomQuoteUseCase(quoteRepository)
    }

    /**
     * TEST RETORNA ALGO
     * GIVEN -> SE ESPERA QUE RETORNE LA CITA DE ARISTIDEVS
     * WHEN -> SE LLAMA AL CASO DE USO
     * THEN -> PROBAR QUE LA RESPUESTA ES LA QUE PROPORCIONAMOS
     * */

    //Correr prueba al inicio de la funcion
    @Test
    fun `when database is empty then return null`() = runBlocking {
        //GIVEN -> Se espera obtener un listado vacio de la base de datos
        // every o coEvery si es desde una coorutina. Se define lo que se espera obtener
        coEvery { quoteRepository.getAllQuotesFromDatabase() } returns emptyList()
        //WHEN -> Almacena la respuesta
        val response = getRandomQuoteUseCase()
        //THEN -> Comprobar que retorna  una respuesta nula
        assert(response == null)
    }
}