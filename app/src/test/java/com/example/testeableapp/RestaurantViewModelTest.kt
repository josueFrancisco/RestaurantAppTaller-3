package com.example.testeableapp

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testeableapp.model.MenuData
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

class RestaurantViewModelTest {

    @Test
    fun AgregarItem() {
        val viewModel = RestaurantViewModel()

        viewModel.addItem(1)
        assertEquals(1, viewModel.quantities.value[1])

    }

    @Test
    fun CambioItem() {
        val viewModel = RestaurantViewModel()

        viewModel.addItem(1)
        viewModel.incrementItem(1)
        assertEquals(2, viewModel.quantities.value[1])

        viewModel.decrementItem(1)
        assertEquals(1, viewModel.quantities.value[1])

    }

    @Test
    fun decrementItem_eliminaElProductoCuandoLaCantidadEsUno() {
        val viewModel = RestaurantViewModel()

        viewModel.addItem(1)
        viewModel.decrementItem(1)
        assertEquals(null, viewModel.quantities.value[1])
    }

    @Test
    fun total_calculaCorrectamenteElImporteDelPedido() {
        val viewModel = RestaurantViewModel()

        viewModel.addItem(1)
        viewModel.incrementItem(1)
        viewModel.addItem(6)
        assertEquals(13.0, viewModel.total.value, 0.001)
    }

    //TEST ADICIONALES UNITARIOS
    //Test #1 Este test comprueba que cuando se añade un producto, la aplicación guarda correctamente la cantidad y calcula bien el precio total.
    @Test
    fun incrementarItemAumentaCantidadYTotal() {
        val viewModel = RestaurantViewModel()

        val item = MenuData.items.first()

        viewModel.addItem(item.id)

        val quantity = viewModel.quantities.value[item.id]
        val total = viewModel.total.value

        assert(quantity == 1)
        assert(total == item.price)
    }

    //Test #2
    //Este test no genera el pedido si esta vacio
    @Test
    fun noSeGeneraConfirmacionSiElPedidoEstaVacio() {
        val viewModel = RestaurantViewModel()

        viewModel.placeOrder()

        val confirmation = viewModel.confirmation.value

        assert(confirmation == null)
    }

}

