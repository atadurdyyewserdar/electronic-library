import React, { useEffect } from 'react'
import { useAuth } from '../hooks/useAuth'
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { login } from '../redux-tool/authSlice';
import SignInForm from '../components/SignInForm'

const LoginPage = () => {

    const { isAuth } = useAuth();
    const navigate = useNavigate();
    const dispatch = useDispatch();

    useEffect(() => {
        if (isAuth) {
            console.log('i am here')
            navigate('/');
        }
    }, [isAuth])

    const handleLogin = ({ username, password }) => {
        dispatch(login({ username, password }))

        navigate('/');
    }

    return (
        <div>
            <SignInForm
                handleClick={handleLogin}
            />
        </div>
    )
}

export default LoginPage

