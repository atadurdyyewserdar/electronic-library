import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";

export const login = createAsyncThunk(
    'auth/login',
    async function ({ username, password }, { rejectWithValue }) {
        try {
            const response = await fetch(`http://localhost:8080/user/login`, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username: username, password: password })
            });
            console.log(response)
            if (response.status !== 200) {
                throw new Error('Server error!');
            }
            const data = await response.json();
            localStorage.setItem('token', data.token);
            localStorage.setItem('user', JSON.stringify(data));
            return data;
        } catch (error) {
            return rejectWithValue(error.message);
        }
    }
)

export const logout = createAsyncThunk(
    'auth/logout',
    async function (_, { dispatch }) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        dispatch(signOut());
    }
)

export const register = createAsyncThunk(
    'auth/register',
    async function ({ firstName, lastName, username, email }, { rejectWithValue }) {
        try {
            const response = await fetch(`http://localhost:8080/user/register`, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ firstName: firstName, lastName: lastName, username: username, email: email })
            });

            const data = await response;
            return data;
        } catch (error) {
            return rejectWithValue(error.message)
        }
    }
)

const authSlice = createSlice({
    name: 'auth',
    initialState: {
        token: null,
        user: {},
        status: null,
        error: null,
        isAuth: false
    },
    reducers: {
        signOut(state) {
            state.status = null;
            state.error = null;
            state.user = {};
            state.token = null;
            state.isAuth = false
        }
    },
    extraReducers: {
        [login.pending]: (state) => {
            state.status = 'loading';
            state.error = null;
            state.user = {};
            state.token = null;
            state.isAuth = false;
        },
        [login.fulfilled]: (state, action) => {
            state.status = 'resolved';
            state.user = action.payload;
            state.token = action.payload.token;
            state.isAuth = true;
        },
        [login.rejected]: (state, action) => {
            state.status = 'rejected';
            state.error = action.payload;
            state.isAuth = false;
        },
        [register.rejected]: (state, action) => {
            state.status = 'rejected';
            state.isAuth = false;
            state.error = action.payload;
        }
    }
})

const { signOut } = authSlice.actions;

export default authSlice.reducer