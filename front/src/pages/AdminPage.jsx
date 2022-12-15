import React, { useEffect } from 'react'
import { useDispatch } from 'react-redux';
import { useSelector } from 'react-redux'
import { useNavigate } from 'react-router';
import { getAllUsers } from '../redux/usersdata/actions';

const AdminPage = () => {
    const { user } = useSelector((state) => state.auth);
    const navigate = useNavigate();
    const dispatch = useDispatch();

    useEffect(() => {
        if (user.role !== 'ROLE_SUPER_USER') {
            navigate('/page-not-found')
        }
    }, [user]);

    const handleUsersDataClick = () => {
        dispatch(getAllUsers());
        navigate('/admin/users-data');
    }
    return (
        <div>
            <div>
                <button
                    onClick={handleUsersDataClick}
                >
                    Users data
                </button>
            </div>
            <div>
                <button
                >
                    Book data
                </button>
            </div>
        </div>
    )
}

export default AdminPage
