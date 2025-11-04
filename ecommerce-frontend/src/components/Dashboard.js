import React, { useState, useEffect } from "react";
import axios from "axios";
import Cookies from "js-cookie";
import Pagination from "./Pagination";
import ProductList from "./ProductList";
import CategoryList from "./CategoryList";
import "bootstrap/dist/css/bootstrap.min.css";

const Dashboard = () => {
  const [view, setView] = useState("categories"); // Start with categories view
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(false);

  const token = Cookies.get("token");

  // Fetch products for the "products" view or specific category
  const fetchProducts = async (page = 1, categoryId = null) => {
    setLoading(true);
    try {
      const url = categoryId
        ? `http://localhost:8080/api/products/category/${categoryId}?page=${page}`
        : `http://localhost:8080/api/products?page=${page}`;

      const res = await axios.get(url, {
        headers: { Authorization: `Bearer ${token}` },
      });

      const content = res.data.content || res.data || [];
      const pages =
        typeof res.data.totalPages === "number" && res.data.totalPages > 0
          ? res.data.totalPages
          : 1;

      setProducts(content);
      setTotalPages(pages);
    } catch (err) {
      console.error("Error fetching products:", err);
      setProducts([]);
      setTotalPages(1);
    } finally {
      setLoading(false);
    }
  };

  // Fetch categories for the "categories" view
  const fetchCategories = async () => {
    try {
      const res = await axios.get("http://localhost:8080/api/categories", {
        headers: { Authorization: `Bearer ${token}` },
      });
      setCategories(res.data || []);
    } catch (err) {
      console.error("Error fetching categories:", err);
      setCategories([]);
    }
  };

  useEffect(() => {
    if (view === "products") {
      fetchProducts(currentPage); // Fetch products for the selected page
    }

    if (view === "categories") {
      fetchCategories(); // Fetch categories when "categories" view is active
    }
  }, [view, currentPage]);

  const handleCategoryClick = (category) => {
    setSelectedCategory(category);
    setView("products"); // Switch to product view when category is clicked
    setCurrentPage(1); // Reset page number to 1 when switching categories
    fetchProducts(1, category.categoryId); // Fetch products for the selected category
  };

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2 className="fw-bold">Dashboard</h2>
        {/* <button
          className="btn btn-outline-primary"
          onClick={() => setView(view === "categories" ? "products" : "categories")}
        >
          {view === "categories" ? "View All Products" : "Back to Categories"}
        </button> */}
      </div>

      {loading ? (
        <p>Loading...</p>
      ) : view === "categories" ? (
        <CategoryList
          categories={categories}
          onCategoryClick={handleCategoryClick}
        />
      ) : (
        <>
          <h4>Products in Category: {selectedCategory?.name}</h4>
          <ProductList products={products} />
          {totalPages > 1 && (
            <Pagination
              totalItems={products.length}
              itemsPerPage={5}
              currentPage={currentPage}
              onPageChange={(p) => setCurrentPage(p)}
            />
          )}
        </>
      )}
    </div>
  );
};

export default Dashboard;
