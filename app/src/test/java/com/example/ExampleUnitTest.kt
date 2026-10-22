package com.example

import com.example.data.ProductCategory
import com.example.data.ProductRepository
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun verifyProductRepositoryCatalog() {
    val items = ProductRepository.items
    assertTrue("Product repository should have items", items.isNotEmpty())
    
    // Check that each category has products
    val categories = items.map { it.category }.toSet()
    assertTrue("Should contain Stickers", categories.contains(ProductCategory.STICKERS))
    assertTrue("Should contain Presets", categories.contains(ProductCategory.PRESETS))
    assertTrue("Should contain Name Tags", categories.contains(ProductCategory.NAME_TAGS))
    assertTrue("Should contain Sheets", categories.contains(ProductCategory.SHEETS))
    
    // Validate each product has positive price and non-empty name
    items.forEach { product ->
      assertTrue(product.name.isNotBlank())
      assertTrue(product.price > 0.0)
      assertTrue(product.features.isNotEmpty())
    }
  }
}
