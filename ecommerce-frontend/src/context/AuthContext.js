// src/context/AuthContext.js
import React, { createContext, useState, useEffect } from "react";
import Cookies from "js-cookie";
import axios from "axios";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  // On app start, restore token from cookie (if present) and set axios header
  useEffect(() => {
    const token = Cookies.get("token");
    if (token) {
      // set axios default Authorization header
      axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
      setIsAuthenticated(true);
    } else {
      // ensure no leftover header
      delete axios.defaults.headers.common["Authorization"];
      setIsAuthenticated(false);
    }
  }, []);

  // login: save token (cookie) + set axios header + update state
  const login = (token) => {
    if (!token) return;
    Cookies.set("token", token, { expires: 1 }); // 1 day
    axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
    setIsAuthenticated(true);
  };

  // logout: remove token, remove axios header, update state
  const logout = () => {
    Cookies.remove("token");
    delete axios.defaults.headers.common["Authorization"];
    setIsAuthenticated(false);
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
