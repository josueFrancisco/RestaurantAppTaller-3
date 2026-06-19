package com.example.testeableapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.testeableapp.model.MenuData
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RestaurantOrderTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    @Before
    fun setup() {
        composeTestRule.waitForIdle()
    }

    @Test
    fun mensajePedidoVacioVisibleAlInicio() {
        composeTestRule
            .onNodeWithTag("emptyOrderMessage")
            .assertExists()
            .assertTextEquals("El pedido está vacío.")
    }

    @Test
    fun todosLosItemsDelMenuSonVisibles() {
        MenuData.items.forEach { item ->
            composeTestRule
                .onNodeWithTag("menuItemName_${item.id}")
                .assertIsDisplayed()
        }
    }

    @Test
    fun elTotalSeActualizaAlAgregarItem() {

        composeTestRule.waitForIdle()

        val item = MenuData.items.first()

        composeTestRule
            .onNodeWithTag("addButton_${item.id}")
            .performClick()

        composeTestRule.waitForIdle()

        val expectedTotal = "%.2f €".format(item.price)

        composeTestRule
            .onNodeWithTag("totalValue")
            .performScrollTo()
            .assertIsDisplayed()
            .assertTextEquals(expectedTotal)
    }


    // TEST ADICIONALES DE UI #1
    //Test basico para ver si el nombre de la app carga correctamente
    @Test
    fun tituloDeAPP() {
        composeTestRule
            .onNodeWithTag("appTitle")
            .assertExists()
            .assertTextEquals("Restaurante El Sabor")
    }

    // TEST ADICIONALES DE UI #2
    //Test Para ver si el boton de agregar pedido aparece al agregar items
    @Test
    fun botonRealizarPedido() {

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("placeOrderButton")
            .assertDoesNotExist()

        val item = MenuData.items.first()

        composeTestRule
            .onNodeWithTag("addButton_${item.id}")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("placeOrderButton")
            .performScrollTo()
            .assertIsDisplayed()
    }

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

    @Test
    fun realizarPedidoGeneraConfirmacion() {
        val viewModel = RestaurantViewModel()

        val item = MenuData.items.first()

        viewModel.addItem(item.id)
        viewModel.placeOrder()

        val confirmation = viewModel.confirmation.value

        assert(confirmation != null)
        assert(confirmation?.itemCount == 1)
        assert(confirmation?.total == item.price)
    }

}