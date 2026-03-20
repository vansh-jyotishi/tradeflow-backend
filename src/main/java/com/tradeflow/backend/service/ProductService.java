package com.tradeflow.backend.service;

import com.tradeflow.backend.entity.Product;
import com.tradeflow.backend.exception.ResourceNotFoundException;
import com.tradeflow.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    @Cacheable("products")
    public List<Product> getAllActiveProducts() {
        return productRepository.findAllActive();
    }

    @Transactional(readOnly = true)
    public Product getProductBySlug(String slug) {
        return productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "slug", slug));
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryActive(categoryId);
    }

    @Transactional(readOnly = true)
    @Cacheable("featuredProducts")
    public List<Product> getFeaturedProducts() {
        return productRepository.findFeaturedProducts();
    }

    @Transactional(readOnly = true)
    public List<Product> searchProducts(String query) {
        return productRepository.searchProducts(query);
    }

    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product updated) {
        Product existing = getProductById(id);
        existing.setName(updated.getName());
        existing.setSlug(updated.getSlug());
        existing.setDescription(updated.getDescription());
        existing.setShortDescription(updated.getShortDescription());
        existing.setCategory(updated.getCategory());
        existing.setIconClass(updated.getIconClass());
        existing.setAvgShipmentValue(updated.getAvgShipmentValue());
        existing.setHsCode(updated.getHsCode());
        existing.setIsFeatured(updated.getIsFeatured());
        existing.setIsActive(updated.getIsActive());
        existing.setSortOrder(updated.getSortOrder());
        return productRepository.save(existing);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        product.setIsActive(false);
        productRepository.save(product);
    }
}
