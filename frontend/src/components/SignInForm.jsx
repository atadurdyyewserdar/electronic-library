import React, { useState } from "react";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import { Link } from "react-router-dom";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import { Alert, AlertTitle } from "@mui/material";
import { useSelector } from "react-redux";

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

const SignInForm = ({ handleClick }) => {
  const { error } = useSelector((state) => state.auth);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

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
            Sign in
          </Typography>
          <Box
            component="form"
            onSubmit={(e) => {
              handleClick({ username, password }, e);
            }}
            noValidate
            sx={{ mt: 1 }}
          >
            {error && (
              <Alert severity="error">Wrong username or password</Alert>
            )}
            <TextField
              margin="normal"
              required
              fullWidth
              id="username"
              label="Email or username"
              name="username"
              autoComplete="username"
              autoFocus
              onChange={(e) => setUsername(e.target.value)}
              value={username}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              autoComplete="current-password"
              onChange={(e) => setPassword(e.target.value)}
              value={password}
            />
            <Button
              type="submit"
              fullWidth
              variant="outlined"
              sx={{ mt: 3, mb: 2 }}
            >
              Sign In
            </Button>
            <Grid container>
              <Grid item xs>
                <Link to="/reset-password">Forgot password?</Link>
              </Grid>
              <Grid item>
                <Link to="/register">Don't have an account? Sign Up</Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
        <Copyright sx={{ mt: 8, mb: 4 }} />
      </Container>
    </ThemeProvider>
  );
};

export default SignInForm;

{
  /* <FormControlLabel
control={<Checkbox value="remember" color="primary" />}
label="Remember me"
/> */
}
