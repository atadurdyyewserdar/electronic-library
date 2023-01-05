import { configureStore, combineReducers } from "@reduxjs/toolkit";
import storage from "redux-persist/lib/storage";
import appUsersReducer from "./appUsersSlice";
import authReducer from "./authSlice";
import booksReducer from "./booksSlice";

import {
  persistStore,
  persistReducer,
  FLUSH,
  REHYDRATE,
  PAUSE,
  PERSIST,
  PURGE,
  REGISTER,
} from "redux-persist";

import { createBlacklistFilter } from "redux-persist-transform-filter";

const rootReducer = combineReducers({
  appUsers: appUsersReducer,
  auth: authReducer,
  books: booksReducer,
});

const saveSubsetBlacklistFilter = createBlacklistFilter("auth", [
  "error",
]);

const persistConfig = {
  key: "root",
  storage,
  transforms: [saveSubsetBlacklistFilter],
};

const persistedReducer = persistReducer(persistConfig, rootReducer);

const store = configureStore({
  reducer: persistedReducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: [FLUSH, REHYDRATE, PAUSE, PERSIST, PURGE, REGISTER],
      },
    }),
});

export const persistor = persistStore(store);
export default store;
