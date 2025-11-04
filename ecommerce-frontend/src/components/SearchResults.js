// src/components/SearchResults.js
import React, { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";

function SearchResults() {
  const [searchParams] = useSearchParams();
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(true);

  const query = searchParams.get("q");

  useEffect(() => {
    if (!query) return;

    setLoading(true);
    axios
      .get(`http://localhost:8080/api/search?q=${query}`)
      .then((res) => {
        setResults(res.data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Error fetching search results:", err);
        setLoading(false);
      });
  }, [query]);

  if (loading) {
    return (
      <div className="text-center mt-5">
        <div className="spinner-border text-primary" role="status"></div>
        <p>Searching for "{query}"...</p>
      </div>
    );
  }

  if (results.length === 0) {
    return (
      <div className="text-center mt-5">
        <h5>No products found for "{query}" 😕</h5>
      </div>
    );
  }

  return (
    <div className="container mt-4">
      <h3 className="text-center mb-4">Search Results for "{query}"</h3>
      <div className="row">
        {results.map((product, index) => (
          <div className="col-md-4 mb-3" key={index}>
            <div className="card shadow-sm h-100">
              <div className="card-body text-center">
                <h5 className="card-title">{product.name}</h5>
                <p>
                  <strong>MRP:</strong> ₹{product.mrp}
                </p>
                <p>
                  <strong>Discounted Price:</strong> ₹{product.discountedPrice}
                </p>
                <p>
                  <strong>Quantity:</strong> {product.quantity}
                </p>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default SearchResults;