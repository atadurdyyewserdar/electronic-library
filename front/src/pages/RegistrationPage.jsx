import React from 'react'
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router';
import SignUpForm from '../components/SignUpForm';
import { register } from '../redux-tool/authSlice';

const RegistrationPage = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const handleSignUp = ({ firstName, lastName, username, email }) => {
        dispatch(register({ firstName, lastName, username, email }));
        navigate('/login');
    }

    return (
        <div>
            <SignUpForm
                handleClick={handleSignUp}
            />
        </div>
    )
}

export default RegistrationPage;