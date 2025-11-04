import React, { useEffect, useState } from "react";
import axios from "axios";
import { useParams, useNavigate, useSearchParams } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";

function ProductList() {
  const { id } = useParams(); // ✅ category ID from URL
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();

  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(1); // ✅ start from page 1
  const [totalPages, setTotalPages] = useState(0);

  // ✅ Read the page number from URL query params (default = 1)
  const pageFromUrl = parseInt(searchParams.get("page")) || 1;

  // ✅ Fetch products for this category + page
  const fetchProducts = (page = 1) => {
    setLoading(true);

    // ⚠️ Backend expects 0-based page index, so subtract 1 when calling API
    axios
      .get(`http://localhost:8080/api/products/category/${id}?page=${page - 1}&size=5`)
      .then((response) => {
        setProducts(response.data.content);
        setTotalPages(response.data.totalPages);
        setCurrentPage(page);
        setLoading(false);

        // ✅ Update URL with the current (1-based) page
        setSearchParams({ page });
      })
      .catch((error) => {
        console.error("Error fetching products:", error);
        setLoading(false);
      });
  };

  // ✅ When category or page changes, reload data
  useEffect(() => {
    if (id) fetchProducts(pageFromUrl);
  }, [id, pageFromUrl]);

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
      <h3 className="text-center mb-4">Products in Category #{id}</h3>

      {products.length === 0 ? (
        <p className="text-center text-muted">No products found.</p>
      ) : (
        <div className="row">
          {products.map((product) => (
            <div className="col-md-4 mb-3" key={product.productId}>
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

      {/* ✅ Pagination controls */}
      <div className="d-flex justify-content-center align-items-center mt-4 gap-3">
        <button
          className="btn btn-outline-secondary"
          disabled={currentPage === 1}
          onClick={() => fetchProducts(currentPage - 1)}
        >
          ◀ Previous
        </button>

        <span>
          Page {currentPage} of {totalPages}
        </span>

        <button
          className="btn btn-outline-secondary"
          disabled={currentPage >= totalPages}
          onClick={() => fetchProducts(currentPage + 1)}
        >
          Next ▶
        </button>
      </div>

      {/* ✅ Back button */}
      <div className="text-center mt-4">
        <button
          className="btn btn-secondary"
          onClick={() => navigate("/categories")}
        >
          ← Back to Categories
        </button>
      </div>
    </div>
  );
}

export default ProductList;
