import React, { useEffect } from "react";
import { useAuth } from "../hooks/useAuth";
import { useSelector, useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { login } from "../redux-tool/authSlice";
import SignInForm from "../components/SignInForm";
import Header from "../components/Header";

const LoginPage = () => {
  const { isAuth } = useAuth();
  const navigate = useNavigate();
  const dispatch = useDispatch();

  useEffect(() => {
    if (isAuth) {
      console.log("i am here");
      navigate("/");
    }
  }, [isAuth]);

  const handleLogin = async ({ username, password }, e) => {
    e.preventDefault();
    await dispatch(login({ username, password, navigate }));
  };

  return (
    <div>
      <Header/>
      <SignInForm handleClick={handleLogin} />
    </div>
  );
};

export default LoginPage;
