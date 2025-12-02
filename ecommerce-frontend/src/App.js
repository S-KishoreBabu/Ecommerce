import React, { useContext } from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
  useLocation,
} from "react-router-dom";

import Register from "./components/Register";
import Login from "./components/Login";
import Dashboard from "./components/Dashboard";
import CategoryList from "./components/CategoryList";
import ProductList from "./components/ProductList";
import AllProducts from "./components/AllProducts";
import SearchResults from "./components/SearchResults";
import Navbar from "./components/Navbar";

import { AuthProvider, AuthContext } from "./context/AuthContext";

/**
 * Inline PrivateRoute for React Router v6.
 * If user is authenticated, render children.
 * Otherwise redirect to /login and preserve the attempted location in state.
 */
function PrivateRoute({ children }) {
  const { isAuthenticated } = useContext(AuthContext);
  const location = useLocation();

  if (isAuthenticated) return children;
  return <Navigate to="/login" state={{ from: location }} replace />;
}

function AppContent() {
  const { isAuthenticated } = useContext(AuthContext);
  const location = useLocation();

  // Hide navbar on login and register pages
  const hideNavbar =
    location.pathname === "/login" || location.pathname === "/register";

  return (
    <>
      {!hideNavbar && isAuthenticated && <Navbar />}

      <Routes>
        {/* Public routes */}
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />

        {/* Protected routes */}
        <Route
          path="/dashboard"
          element={
            <PrivateRoute>
              <Dashboard />
            </PrivateRoute>
          }
        />

        <Route
          path="/"
          element={
            <PrivateRoute>
              <Dashboard />
            </PrivateRoute>
          }
        />

        <Route
          path="/categories"
          element={
            <PrivateRoute>
              <CategoryList />
            </PrivateRoute>
          }
        />

        <Route
          path="/products/:id"
          element={
            <PrivateRoute>
              <ProductList />
            </PrivateRoute>
          }
        />

        <Route
          path="/all-products"
          element={
            <PrivateRoute>
              <AllProducts />
            </PrivateRoute>
          }
        />

        <Route
          path="/search"
          element={
            <PrivateRoute>
              <SearchResults />
            </PrivateRoute>
          }
        />

        {/* Fallback: redirect unknown routes to / (which in turn sends to dashboard/login) */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </>
  );
}

export default function App() {
  // Wrap entire app with AuthProvider so AuthContext.useEffect runs on app start
  return (
    <AuthProvider>
      <Router>
        <AppContent />
      </Router>
    </AuthProvider>
  );
}
