import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";

const getToken = () => {
  return localStorage.getItem("token");
};

export const getBooks = createAsyncThunk(
  "books/getBooks",
  async function (_, { rejectWithValue }) {
    try {
      const response = await axios
        .get(`http://localhost:8080/resource/books/all`)
        .catch((err) => {
          return rejectWithValue(err);
        });
      if (response.status !== 200) {
        throw new Error("Server error!");
      }
      const data = await response.data;
      console.log(data);
      return data;
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

export const getBooksFiltered = createAsyncThunk(
  "books/getBooksFiltered",
  async function ({ bookName }, { rejectWithValue }) {
    try {
      const params = {};
      if (bookName) {
        params["bookName"] = bookName;
      }
      console.log(params);
      const response = await axios
        .get(`http://localhost:8080/resource/books/all`, { params: params })
        .catch((err) => {
          return rejectWithValue(err);
        });
      if (response.status !== 200) {
        throw new Error("Server error!");
      }
      const data = await response.data;
      console.log(data);
      return data;
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

export const getBookById = createAsyncThunk(
  "books/getBookById",
  async function ({ id, username }, { rejectWithValue }) {
    try {
      const response = await axios
        .get(`http://localhost:8080/resource/books/${id}`)
        .catch((err) => {
          rejectWithValue(err);
        });
      if (response.status !== 200) {
        throw new Error("Server error!");
      }
      const data = response.data;
      if (username) {
        const userRate = await axios
          .get(`http://localhost:8080/resource/books/${id}/rate/${username}`)
          .catch((err) => {
            rejectWithValue(err);
          });
        data["rating"] = userRate.data;
      }
      console.log(data);
      return data;
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

export const getComments = createAsyncThunk(
  "books/getComments",
  async function ({ id }, { rejectWithValue }) {
    try {
      const response = await axios.get(
        `http://localhost:8080/resource/books/${id}/comments`
      );
      if (response.status !== 200) {
        throw new Error("Server error!");
      }
      return response.data.comments;
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

export const addComment = createAsyncThunk(
  "books/addComment",
  async function (
    { userName, firstName, lastName, comment, bookId },
    { rejectWithValue }
  ) {
    const data = await axios
      .post(`http://localhost:8080/resource/books/${bookId}/comments`, {
        username: userName,
        firstName: firstName,
        lastName: lastName,
        comment: comment,
        bookId: bookId,
      })
      .catch((err) => {
        return rejectWithValue(err);
      });
    return data.data;
  }
);

export const updateRate = createAsyncThunk(
  "books/updateRate",
  async function ({ star, efileId, username }, { dispatch }) {
    dispatch(updateRating({star, efileId, username}));
  }
);

const booksSlice = createSlice({
  name: "books",
  initialState: {
    books: [],
    book: {},
    status: null,
    error: null,
    comments: [],
    rating: {},
  },

  reducers: {
    updateRating(state, action) {
      state.rating = action.payload;
    },
  },

  extraReducers: {
    [addComment.fulfilled]: (state, action) => {
      state.status = "resolved";
      state.comments.unshift(action.payload);
      state.error = null;
    },
    [getBooksFiltered.fulfilled]: (state, action) => {
      state.status = "resolved";
      state.books = action.payload;
      state.error = null;
    },
    [getBooks.pending]: (state) => {
      state.status = "loading";
      state.error = null;
    },
    [getBooks.fulfilled]: (state, action) => {
      state.status = "resolved";
      state.books = action.payload;
    },
    [getBooks.rejected]: (state, action) => {
      state.status = "rejected";
      state.error = action.payload;
    },
    [getBookById.fulfilled]: (state, action) => {
      state.status = "resolved";
      state.book = action.payload.books;
      state.comments = action.payload.comments;
      state.rating = action.payload.rating;
      state.error = null;
    },
    [getBookById.rejected]: (state, action) => {
      state.status = "rejected";
      state.book = {};
      state.error = action.payload;
    },
    [getBookById.pending]: (state) => {
      state.status = "loading";
      state.book = {};
      state.error = null;
    },
    [getComments.pending]: (state) => {
      state.status = "loading";
      state.error = null;
    },
    [getComments.fulfilled]: (state, action) => {
      state.status = "resolved";
      state.comments = action.payload;
    },
    [getComments.rejected]: (state, action) => {
      state.status = "rejected";
      state.error = action.payload;
    },
  },
});

const { updateRating } = booksSlice.actions;

export default booksSlice.reducer;
