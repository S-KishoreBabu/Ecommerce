// src/components/AllProducts.js
import React, { useEffect, useState } from "react";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";

function AllProducts() {
  const [products, setProducts] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false); // Only true during fetching

  const size = 8; // Number of products per page

  useEffect(() => {
    fetchProducts(page);
  }, [page]);

  const fetchProducts = (pageNumber) => {
    setLoading(true);
    axios
      .get(`http://localhost:8080/api/products?page=${pageNumber}&size=${size}`)
      .then((res) => {
        setProducts(res.data.content);
        setTotalPages(res.data.totalPages);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Error fetching products:", err);
        setLoading(false);
      });
  };

  if (loading) {
    return (
      <div className="text-center mt-5">
        <div className="spinner-border text-primary" role="status"></div>
        <p>Loading products...</p>
      </div>
    );
  }

  return (
    <div className="container mt-4">
      <h3 className="text-center mb-4">All Products</h3>

      {products.length === 0 ? (
        <p className="text-center text-muted">No products found.</p>
      ) : (
        <div className="row">
          {products.map((product) => (
            <div key={product.productId} className="col-md-3 mb-4">
              <div className="card shadow-sm h-100">
                <div className="card-body">
                  <h5 className="card-title">{product.name}</h5>
                  <p className="card-text text-muted">
                    <strong>MRP:</strong> ₹{product.mrp} <br />
                    <strong>Discounted:</strong> ₹{product.discountedPrice}
                  </p>
                  <p>
                    <strong>Quantity:</strong> {product.quantity}
                  </p>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Pagination controls */}
      <div className="d-flex justify-content-center align-items-center mt-4 gap-3">
        <button
          className="btn btn-outline-secondary"
          disabled={page === 0}
          onClick={() => setPage(page - 1)}
        >
          ◀ Previous
        </button>

        <span>
          Page {page + 1} of {totalPages}
        </span>

        <button
          className="btn btn-outline-secondary"
          disabled={page + 1 === totalPages}
          onClick={() => setPage(page + 1)}
        >
          Next ▶
        </button>
      </div>
    </div>
  );
}

export default AllProducts;
