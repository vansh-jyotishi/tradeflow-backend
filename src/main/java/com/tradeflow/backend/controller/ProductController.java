package com.tradeflow.backend.controller;

import com.tradeflow.backend.dto.ApiResponse;
import com.tradeflow.backend.entity.Category;
import com.tradeflow.backend.entity.Product;
import com.tradeflow.backend.repository.CategoryRepository;
import com.tradeflow.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryRepository categoryRepository;

    // ─── Categories ───────────────────────────────────────

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<Category>>> getCategories() {
        List<Category> categories = categoryRepository.findByIsActiveTrueOrderBySortOrder();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    // ─── Products ─────────────────────────────────────────

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<Page<Product>>> getProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search,
            @PageableDefault(size = 12) Pageable pageable) {

        Page<Product> products;
        if (search != null && !search.isBlank()) {
            products = productService.searchProducts(search, pageable);
        } else if (categoryId != null) {
            products = productService.getProductsByCategory(categoryId, pageable);
        } else {
            products = productService.getAllActiveProducts(pageable);
        }
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/products/featured")
    public ResponseEntity<ApiResponse<List<Product>>> getFeaturedProducts() {
        return ResponseEntity.ok(ApiResponse.success(productService.getFeaturedProducts()));
    }

    @GetMapping("/products/{slug}")
    public ResponseEntity<ApiResponse<Product>> getProduct(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductBySlug(slug)));
    }
}
