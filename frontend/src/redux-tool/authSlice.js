import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";

export const login = createAsyncThunk(
  "auth/login",
  async function ({ username, password, navigate }, { rejectWithValue }) {
    try {
      const response = await fetch(`http://localhost:8080/user/login`, {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username: username, password: password }),
      });
      console.log(response);
      if (response.status !== 200) {
        console.log("error here");
        throw new Error("Server error!");
      }
      const data = await response.json();
      localStorage.setItem("token", data.token);
      localStorage.setItem("user", JSON.stringify(data));
      navigate("/");
      return data;
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

export const logout = createAsyncThunk(
  "auth/logout",
  async function (_, { dispatch }) {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    dispatch(signOut());
  }
);

export const register = createAsyncThunk(
  "auth/register",
  async function (
    { firstName, lastName, username, password, email, navigate },
    { rejectWithValue }
  ) {
    try {
      console.log("username: ", username);
      console.log("email: ", email);
      const response = await axios.post(`http://localhost:8080/user/register`, {
        firstName: firstName,
        lastName: lastName,
        username: username,
        password: password,
        email: email,
      });
      const data = response.data;
      navigate("/login");
      return data;
    } catch (error) {
      return rejectWithValue(error.response.data.message);
    }
  }
);

const authSlice = createSlice({
  name: "auth",
  initialState: {
    token: null,
    user: {},
    status: null,
    error: null,
    isAuth: false,
  },
  reducers: {
    signOut(state) {
      state.status = null;
      state.error = null;
      state.user = {};
      state.token = null;
      state.isAuth = false;
    },
  },
  extraReducers: {
    [login.pending]: (state) => {
      state.status = "loading";
      state.error = null;
      state.user = {};
      state.token = null;
      state.isAuth = false;
    },
    [login.fulfilled]: (state, action) => {
      state.status = "resolved";
      state.user = action.payload;
      state.token = action.payload.token;
      state.isAuth = true;
      state.error = null;
    },
    [login.rejected]: (state, action) => {
      state.status = "rejected";
      state.error = action.payload;
      state.isAuth = false;
    },
    [register.fulfilled]: (state) => {
      state.status = "resolved";
      state.user = {};
      state.token = null;
      state.isAuth = false;
      state.error = null;
    },
    [register.pending]: (state) => {
      state.status = "loading";
      state.error = null;
      state.user = {};
      state.token = null;
      state.isAuth = false;
    },
    [register.rejected]: (state, action) => {
      state.status = "rejected";
      state.isAuth = false;
      state.error = action.payload;
      state.user = {};
      state.token = null;
    },
  },
});

const { signOut } = authSlice.actions;

export default authSlice.reducer;
