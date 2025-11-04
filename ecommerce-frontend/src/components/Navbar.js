// src/components/Navbar.js
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

function Navbar() {
  const navigate = useNavigate();
  const [query, setQuery] = useState("");
  const [viewCategories, setViewCategories] = useState(true);  // New state to toggle between categories and products

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  const handleSearch = (e) => {
    e.preventDefault();
    if (query.trim() !== "") {
      navigate(`/search?q=${encodeURIComponent(query)}`);
    }
  };

  const toggleView = () => {
    setViewCategories(!viewCategories);
    // Depending on the view, navigate to either categories or all products
    if (!viewCategories) {
      navigate("/all-products");
    } else {
      navigate("/categories");
    }
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-primary px-4">
      <span
        className="navbar-brand"
        style={{ cursor: "pointer" }}
        onClick={() => navigate("/dashboard")}
      >
        E-Commerce
      </span>

      <div className="ms-auto d-flex align-items-center">
        <form className="d-flex me-3" onSubmit={handleSearch}>
          <input
            type="text"
            className="form-control me-2"
            placeholder="Search products..."
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            style={{ width: "250px" }}
          />
          <button className="btn btn-outline-light" type="submit">
            Search
          </button>
        </form>

        {/* Toggle button to switch between categories and products */}
        <button
          className="btn btn-outline-light me-2"
          onClick={toggleView}
        >
          {viewCategories ?  "View Categories" : "View All Products"}
        </button>

        <button className="btn btn-danger" onClick={handleLogout}>
          Logout
        </button>
      </div>
    </nav>
  );
}

export default Navbar;
