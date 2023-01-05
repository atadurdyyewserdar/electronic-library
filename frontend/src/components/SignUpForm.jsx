import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { Link } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import { useSelector } from "react-redux";
import { Alert } from "@mui/material";

function Copyright(props) {
  return (
    <Typography
      variant="body2"
      color="text.secondary"
      align="center"
      {...props}
    >
      {"Copyright © "}
      <Link to="/">Electronic-Library</Link> {new Date().getFullYear()}
      {"."}
    </Typography>
  );
}

const theme = createTheme();

const SignUpForm = ({ handleClick }) => {
  const navigate = useNavigate();
  const { isAuth } = useAuth();
  const { error } = useSelector((state) => state.auth);
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [password, setPassword] = useState("");

  const handleFirstName = (e) => {
    e.preventDefault();
    setFirstName(e.target.value);
  };
  const handleLastName = (e) => {
    e.preventDefault();
    setLastName(e.target.value);
  };
  const handlePassword = (e) => {
    e.preventDefault();
    setPassword(e.target.value);
  };
  const handleUsername = (e) => {
    e.preventDefault();
    setUsername(e.target.value);
  };
  const handleEmail = (e) => {
    e.preventDefault();
    setEmail(e.target.value);
  };

  const handleSignUp = () => {
    handleClick({ firstName, lastName, username, password, email });
  };
  return (
    <ThemeProvider theme={theme}>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: "secondary.main" }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign up
          </Typography>
          <Box
            // component="form"
            // noValidate
            // onSubmit={() => handleSignUp()}
            sx={{ mt: 3 }}
          >
            <Grid container spacing={2}>
              <Grid item xs={12}>
                {/* {error && <Alert severity="error">{error}</Alert>}
                {error === "INVALID PASSWORD" && (
                  <Alert severity="error">{error}</Alert>
                )} */}
                {error === "INVALID PASSWORD" ? (
                  <Alert severity="error" sx={{textAlign: "start"}}>
                    {error}
                    <div>1. A digit must occur at least once</div>
                    <div>2. A lower case letter must occur at least once</div>
                    <div>3. No whitespace allowed</div>
                    <div>4. Password should contain at least 8 characters</div>
                  </Alert>
                ) : (
                  error && <Alert severity="error">{error}</Alert>
                )}
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  autoComplete="given-name"
                  name="firstName"
                  required
                  fullWidth
                  label="First Name"
                  autoFocus
                  value={firstName}
                  onChange={handleFirstName}
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  required
                  fullWidth
                  label="Last Name"
                  name="lastName"
                  autoComplete="family-name"
                  value={lastName}
                  onChange={handleLastName}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  // name="username"
                  label="Username"
                  autoComplete="off"
                  value={username}
                  onChange={handleUsername}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  label="Password"
                  autoComplete="off"
                  value={password}
                  onChange={handlePassword}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  label="Email Address"
                  name="email"
                  value={email}
                  onChange={handleEmail}
                />
              </Grid>
            </Grid>
            <Button
              type="submit"
              fullWidth
              variant="outlined"
              sx={{ mt: 3, mb: 2 }}
              onClick={handleSignUp}
            >
              Sign Up
            </Button>
            <Grid container justifyContent="flex-end">
              <Grid item>
                <Link to="/login">Already have an account? Sign in</Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
        <Copyright sx={{ mt: 5 }} />
      </Container>
    </ThemeProvider>
  );
};

export default SignUpForm;
