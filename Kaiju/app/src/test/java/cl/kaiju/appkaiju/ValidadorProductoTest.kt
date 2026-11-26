package cl.kaiju.appkaiju

import cl.kaiju.appkaiju.util.ValidadorProducto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ValidadorProductoTest {

    @Test
    fun `test 1 - datos validos retornan null sin errores`() {
        val resultado = ValidadorProducto.validarFormulario(
            sku = "SKU-101",
            nombre = "Polera Kaiju roja",
            categoria = "Poleras",
            precio = "19990",
            stock = "10",
            stockMinimo = "2"
        )
        assertNull(resultado)
    }

    @Test
    fun `test 2 - sku vacio o por defecto retorna error de sku`() {
        val resultadoVacio = ValidadorProducto.validarFormulario(
            sku = "   ",
            nombre = "Polera Kaiju azul",
            categoria = "Poleras",
            precio = "15000",
            stock = "10",
            stockMinimo = "2"
        )
        val resultadoDefecto = ValidadorProducto.validarFormulario(
            sku = "SKU-",
            nombre = "Polera Kaiju blanca",
            categoria = "Poleras",
            precio = "15000",
            stock = "10",
            stockMinimo = "2"
        )

        assertEquals("Ingresa un SKU válido.", resultadoVacio)
        assertEquals("Ingresa un SKU válido.", resultadoDefecto)
    }

    @Test
    fun `test 3 - nombre en blanco retorna error de nombre`() {
        val resultado = ValidadorProducto.validarFormulario(
            sku = "SKU-104",
            nombre = "   ",
            categoria = "Poleras",
            precio = "15000",
            stock = "10",
            stockMinimo = "2"
        )
        assertEquals("Ingresa el nombre del producto.", resultado)
    }

    @Test
    fun `test 4 - precio negativo o no numerico retorna error de precio`() {
        val negativo = ValidadorProducto.validarFormulario(
            sku = "SKU-104",
            nombre = "Polera Verde Kaiju",
            categoria = "Poleras",
            precio = "-500",
            stock = "10",
            stockMinimo = "2"
        )
        val noNumerico = ValidadorProducto.validarFormulario(
            sku = "SKU-105",
            nombre = "Polera Morada Kaiju",
            categoria = "Poleras",
            precio = "abc",
            stock = "10",
            stockMinimo = "2"
        )

        assertEquals("Ingresa un precio válido.", negativo)
        assertEquals("Ingresa un precio válido.", noNumerico)
    }

    @Test
    fun `test 5 - stock inicial negativo o invalido retorna error de stock`() {
        val negativo = ValidadorProducto.validarFormulario(
            sku = "SKU-106",
            nombre = "Polera Amarilla Kaiju",
            categoria = "Poleras",
            precio = "15000",
            stock = "-3",
            stockMinimo = "2"
        )
        val texto = ValidadorProducto.validarFormulario(
            sku = "SKU-101",
            nombre = "Polera Roja Kaiju",
            categoria = "Poleras",
            precio = "15000",
            stock = "dos",
            stockMinimo = "2"
        )

        assertEquals("Ingresa un stock inicial válido.", negativo)
        assertEquals("Ingresa un stock inicial válido.", texto)
    }
}