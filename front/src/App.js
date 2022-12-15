import { useSelector } from 'react-redux';
import { Route, Routes } from 'react-router';
import './App.css';
import BookPage from './pages/BookPage';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import ProfilePage from './pages/ProfilePage';
import RegistrationPage from './pages/RegistrationPage';
import { RequireAuth } from './route/RequireAuth';


function App() {
  const { user } = useSelector((state) => state.auth)

  return (
    <div className="App">
      <Routes>
        <Route exact path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route exact path="/books" element={<BookPage />} />
        <Route exact path="/books/:id" element={<BookPage/>} />
        <Route path="/books" element={<LoginPage />} />
        <Route path='/profile' element={
          <RequireAuth>
            <ProfilePage />
          </RequireAuth>
        } />
        <Route path="/register" element={<RegistrationPage />} />
      </Routes>
    </div>
  );
}

{/* <Route path='/profile' element={<ProtectedRoute />}>
          <Route path='/profile' element={<ProfilePage />} />
    </Route> 
    <Route exact path="/admin" element={<AdminPage />} />
        <Route path="/page-not-found" element={<PageNotFound />} />
        <Route path="/admin/users-data" element={<ProtectedRoute />}>
          <Route path="/admin/users-data" element={<UsersDataPage />} />
        </Route>
        <Route path="/upload-book" element={<UploadBook />} />
        <Route path="/testpage" element={<TestPage />} />
*/}
export default App;