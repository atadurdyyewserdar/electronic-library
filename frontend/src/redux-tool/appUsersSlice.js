import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";

const getToken = () => {
    return localStorage.getItem('token');
}

export const getAppUsers = createAsyncThunk(
    'appUsers/getAppUsers',
    async function (_, { rejectWithValue }) {
        try {
            const response = await axios.get(`http://localhost:8080/user/list`, {
                headers: {
                    'Authorization': 'Bearer ' + getToken()
                }
            });
            if (!response.ok) {
                throw new Error('Server error!');
            }
            const data = await response.json();
            return data;
        } catch (error) {
            return rejectWithValue(error.message);
        }
    }
)

const appUsersSlice = createSlice({
    name: 'appUsers',
    
    initialState: {
        appUsers: [],
        status: null,
        error: null
    },

    reducers: {
    },

    extraReducers: {
        [getAppUsers.pending]: (state) => {
            state.status = 'loading';
            state.error = null;
        },
        [getAppUsers.fulfilled]: (state, action) => {
            state.status = 'resolved';
            state.appUsers = action.payload;
        },
        [getAppUsers.rejected]: (state, action) => {
            state.status = 'rejected';
            state.error = action.payload;
        }
    }
})

export default appUsersSlice.reducer