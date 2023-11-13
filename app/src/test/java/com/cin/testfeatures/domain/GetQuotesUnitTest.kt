package com.cin.testfeatures.domain

import com.cin.testfeatures.clean_mvvm_dagger.data.QuoteRepository
import com.cin.testfeatures.clean_mvvm_dagger.domain.GetQuotesUseCase
import com.cin.testfeatures.clean_mvvm_dagger.domain.model.Quote
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetQuotesUnitTest {
    /**
     *
     * Objetivo de la clase:
     * Probar clase que modela funcionalidad de Objeto Fake
     * Atributos:
     * @MockK -> Controlar todas las posibles respuestas de la clase Mockeada
     * @RelaxedMockK -> El sistema proporciona una resuesta por defecto
     *
     * ATRIBUTOS -> CLASES A PROBAR E INSTANCIA DE CLASES PRUEBA
     *
     * */

    @RelaxedMockK
    private lateinit var quoteRepository: QuoteRepository
    private lateinit var getQuotesUseCase: GetQuotesUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getQuotesUseCase = GetQuotesUseCase(quoteRepository)
    }

    /**
     * INICIO DE TEST
     * TEST DEL CONSUMO DEl SERVICIO DE CITAS
     * GIVEN WHEN THEN
     * estilo semi estructurado para representar los tests
     *
     * GIVEN -> A nuestro mock (el repositorio) le tenemos que dar la respuesta que queremos que devuelva, en este caso una lista vacía.
     * WHEN -> Llamamos a nuestro caso de uso.
     * THEN -> Tenemos que verificar que la función correcta del repositorio ha sido llamada.
     */

    @Test
    fun `when the api doesnt return anything then get values from database`() = runBlocking {
        //Given
        coEvery { quoteRepository.getAllQuotesFromApi() } returns emptyList()

        //When
        getQuotesUseCase()

        //Then exactly = 1 Llamado una sola vez
        coVerify(exactly = 1) { quoteRepository.getAllQuotesFromDatabase() }
    }

    /**
     * TEST RETORNA ALGO
     * GIVEN -> SE ESPERA QUE RETORNE LA CITA DE ARISTIDEVS
     * WHEN -> SE LLAMA AL CASO DE USO
     * THEN -> PROBAR QUE LA RESPUESTA ES LA QUE PROPORCIONAMOS
     * */
    @Test
    fun `when the api return something then get values from database`() = runBlocking {
        //Given
        val myList = listOf(Quote("Déjame un comentario", "AristiDevs"))
        coEvery { quoteRepository.getAllQuotesFromApi() } returns myList

        //When
        val response = getQuotesUseCase()

        //Then
        coVerify(exactly = 1) { quoteRepository.clearQuotes() }
        coVerify(exactly = 1) { quoteRepository.insertQuotes(any()) }
        coVerify(exactly = 0) { quoteRepository.getAllQuotesFromDatabase() }
        assert(response == myList)
    }
}