import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";

const getToken = () => {
    return localStorage.getItem('token');
}

export const getBooks = createAsyncThunk(
    'books/getBooks',
    async function (_, { rejectWithValue }) {
        try {
            const response = await fetch(`http://localhost:8080/resource/books/all`, {
                method: 'GET',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
            });
            if (response.status !== 200) {
                throw new Error('Server error!');
            }
            const data = await response.json();
            console.log(data);
            return data;
        } catch (error) {
            return rejectWithValue(error.message);
        }
    }
)

const booksSlice = createSlice({
    name: 'books',

    initialState: {
        books: [],
        status: null,
        error: null
    },

    reducers: {
    },

    extraReducers: {
        [getBooks.pending]: (state) => {
            state.status = 'loading';
            state.error = null;
        },
        [getBooks.fulfilled]: (state, action) => {
            state.status = 'resolved';
            state.books = action.payload;
        },
        [getBooks.rejected]: (state, action) => {
            state.status = 'rejected';
            state.error = action.payload;
        }
    }
})

export default booksSlice.reducer