import React from 'react'
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router';
import { useSelector } from 'react-redux';
import { logout } from '../redux-tool/authSlice';

const ProfilePage = () => {
    const {user} = useSelector((state) => state.auth);

    const dispatch = useDispatch();
    const navigate = useNavigate()
    
    const handleLogoutClick = () => {
        dispatch(logout());
        navigate("/");
    }

    return (
        <div>
            <h1>
                Profile page
            </h1>
            <div>
                <h3>Name: {user.firstName }</h3>
                <h3>Last name: {user.lastName }</h3>
            </div>
            <button onClick={handleLogoutClick}>Logout</button>
        </div>
    )
}

export default ProfilePage
