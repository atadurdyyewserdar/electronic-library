import React, { useEffect } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router";
import Header from "../components/Header";
import SignUpForm from "../components/SignUpForm";
import { useAuth } from "../hooks/useAuth";
import { register } from "../redux-tool/authSlice";

const RegistrationPage = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { isAuth } = useAuth();

  useEffect(() => {
    if (isAuth) navigate("/");
  }, [isAuth]);

  const handleSignUp = ({ firstName, lastName, username, password, email }) => {
    console.log("usname:", username);
    console.log("em:", email);
    dispatch(register({ firstName, lastName, username, password, email, navigate }));
  };

  return (
    <div>
      <Header/>
      <SignUpForm handleClick={handleSignUp} />
    </div>
  );
};

export default RegistrationPage;
