package com.tradeflow.backend.repository;

import com.tradeflow.backend.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(attributePaths = {"category", "sourceRegions", "tags"})
    Optional<Product> findBySlug(String slug);

    @EntityGraph(attributePaths = {"category", "sourceRegions", "tags"})
    @Query("SELECT p FROM Product p WHERE p.isActive = true")
    List<Product> findAllActive();

    @EntityGraph(attributePaths = {"category", "sourceRegions", "tags"})
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.category.id = :categoryId")
    List<Product> findByCategoryActive(@Param("categoryId") Long categoryId);

    @EntityGraph(attributePaths = {"category", "sourceRegions", "tags"})
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.isFeatured = true ORDER BY p.sortOrder")
    List<Product> findFeaturedProducts();

    @EntityGraph(attributePaths = {"category", "sourceRegions", "tags"})
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Product> searchProducts(@Param("query") String query);
}
