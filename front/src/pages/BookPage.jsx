import React from 'react'
import { useLocation } from 'react-router'

const BookPage = () => {

    const { state } = useLocation();

    console.log(state);
    return (
        <div>

        </div>
    )
}

export default BookPage
