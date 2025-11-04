// src/components/Login.js
import React, { useContext, useState } from "react";
import axios from "axios";
import Cookies from "js-cookie";
import { AuthContext } from "../context/AuthContext";
import { useNavigate, Link } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  const { login } = useContext(AuthContext);

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post("http://localhost:8080/api/users/login", {
        email,
        password,
      });

      const token = response.data.token;

      // ✅ Save token in cookie instead of localStorage
      Cookies.set("token", token, { expires: 1 }); // 1 day expiry

      // ✅ update AuthContext (optional, if used elsewhere)
      login(token);

      setMessage("Login successful!");
      navigate("/dashboard");
    } catch (error) {
      setMessage("Invalid credentials. Please try again.");
    }
  };

  return (
    <div className="d-flex justify-content-center align-items-center vh-100 bg-light">
      <div className="card p-4 shadow-lg" style={{ width: "400px" }}>
        <h3 className="text-center mb-4">Login</h3>

        <form onSubmit={handleLogin}>
          <div className="mb-3">
            <label className="form-label">Email address</label>
            <input
              type="email"
              className="form-control"
              placeholder="Enter email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Password</label>
            <input
              type="password"
              className="form-control"
              placeholder="Enter password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          <button type="submit" className="btn btn-primary w-100">
            Login
          </button>
        </form>

        {message && <div className="alert alert-info mt-3">{message}</div>}

        <p className="mt-3 text-center">
          Don’t have an account?{" "}
          <Link to="/register" className="text-primary">
            Register here
          </Link>
        </p>
      </div>
    </div>
  );
}

export default Login;
