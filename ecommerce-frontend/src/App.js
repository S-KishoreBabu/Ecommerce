// src/App.js
import React, { useContext } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate, useLocation } from "react-router-dom";
import Register from "./components/Register";
import Login from "./components/Login";
import Dashboard from "./components/Dashboard";
import CategoryList from "./components/CategoryList";
import ProductList from "./components/ProductList";
import AllProducts from "./components/AllProducts";
import SearchResults from "./components/SearchResults";
import Navbar from "./components/Navbar";
import { AuthContext } from "./context/AuthContext";

function AppContent() {
  const { isAuthenticated } = useContext(AuthContext);
  const location = useLocation();

  // ✅ Hide navbar on login and register pages
  const hideNavbar =
    location.pathname === "/login" || location.pathname === "/register";

  return (
    <>
      {!hideNavbar && isAuthenticated && <Navbar />}

      <Routes>
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route
          path="/dashboard"
          element={isAuthenticated ? <Dashboard /> : <Navigate to="/login" />}
        />
        <Route
          path="/"
          element={
            isAuthenticated ? <Navigate to="/dashboard" /> : <Navigate to="/login" />
          }
        />
        <Route path="/categories" element={<CategoryList />} />
        <Route path="/products/:id" element={<ProductList />} />
        <Route path="/all-products" element={<AllProducts />} />
        <Route path="/search" element={<SearchResults />} />
      </Routes>
    </>
  );
}

export default function App() {
  return (
    <Router>
      <AppContent />
    </Router>
  );
}
