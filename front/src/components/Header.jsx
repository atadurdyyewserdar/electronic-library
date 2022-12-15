import React from 'react'
import { useDispatch } from 'react-redux';
import { useNavigate } from "react-router-dom";
import { Link as DOMLink } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { logout } from '../redux-tool/authSlice';
import AppBar from '@mui/material/AppBar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import GlobalStyles from '@mui/material/GlobalStyles';
import Link from '@mui/material/Link';


const Header = () => {

    const { isAuth, status, error } = useAuth();

    const navigate = useNavigate()
    const dispatch = useDispatch();

    const handleLogoutClick = () => {
        dispatch(logout());
        navigate("/login");
    }

    const handleLoginClick = () => {
        navigate("/login")
    }

    return (
        <div>
            <GlobalStyles styles={{ ul: { margin: 0, padding: 0, listStyle: 'none' } }} />
            <CssBaseline />
            <AppBar
                position="static"
                color="default"
                elevation={0}
                sx={{ borderBottom: (theme) => `1px solid ${theme.palette.divider}` }}
            >
                <Toolbar sx={{ flexWrap: 'wrap' }}>
                    <Typography variant="h6" color="inherit" noWrap sx={{ flexGrow: 1 }}>
                        <DOMLink to='/' style={{ textDecoration: 'none', color: 'inherit' }}>
                            Electronic Library
                        </DOMLink>
                    </Typography>
                    {
                        isAuth && (
                            <nav>
                                <Link
                                    variant="button"
                                    color="text.primary"
                                    href="#"
                                    sx={{ my: 1, mx: 1.5 }}
                                    underline='none'
                                >
                                    Profile
                                </Link>
                                <Link
                                    variant="button"
                                    color="text.primary"
                                    href="#"
                                    sx={{ my: 1, mx: 1.5 }}
                                    underline='none'
                                >
                                    Dashboard
                                </Link>
                                <Button href="#" variant="outlined" sx={{ my: 1, mx: 1 }}
                                    onClick={handleLogoutClick}
                                >
                                    Sign out
                                </Button>
                            </nav>
                        )}
                        {
                        !isAuth && (
                            <nav>
                                <Link
                                    variant="button"
                                    color="text.primary"
                                    href="#"
                                    sx={{ my: 1, mx: 1.5 }}
                                    underline='none'
                                >
                                    Profile
                                </Link>
                                <Link
                                    variant="button"
                                    color="text.primary"
                                    href="#"
                                    sx={{ my: 1, mx: 1.5 }}
                                    underline='none'
                                >
                                    Dashboard
                                </Link>
                                <Button variant="outlined" sx={{ my: 1, mx: 1 }}
                                    onClick={handleLoginClick}
                                >
                                    Sign in
                                </Button>
                                <Button variant="outlined" sx={{ my: 1, mx: 1 }}
                                    onClick={() => navigate('/register')}
                                >
                                    Sign Up
                                </Button>
                            </nav>
                        )

                    }
                </Toolbar>
            </AppBar>
        </div>
    )
}

export default Header
