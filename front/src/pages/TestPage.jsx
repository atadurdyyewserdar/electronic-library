import React, { useEffect, useState } from 'react'
import MaterialTable from 'material-table'
import { useDispatch } from 'react-redux';
import { useSelector } from 'react-redux';

const TestPage = () => {

    const { usersData } = useSelector((state) => state.usersInfo)

    const [data, setData] = useState([])

    const dispatch = useDispatch();

    const columns = [
        { title: "id", field: "id", hidden: true },
        { title: "First name", field: "firstName" },
        { title: "Last name", field: "lastName" },
        { title: "Email", field: "email" },
        { title: "Username", field: "username" },
        { title: "Status", field: "notLocked" },
    ]

    useEffect(() => {
    }, [])

    const handleAddUser = (data) => {
        
    }

    return (
        <div className="App">
            <h1 align="center">React-App</h1>
            <h4 align='center'>Material Table with CRUD operation</h4>
            <MaterialTable
                title="Employee Data"
                data={data}
                columns={columns}
                editable={{
                    onRowAdd: (newRow) => new Promise((resolve, reject) => {
                        const updatedRows = [...data, { id: Math.floor(Math.random() * 100), ...newRow }]
                        setTimeout(() => {
                            handleAddUser(newRow);
                            setData(updatedRows)
                            resolve()
                        }, 2000)
                    }),
                    onRowDelete: selectedRow => new Promise((resolve, reject) => {
                        const index = selectedRow.tableData.id;
                        const updatedRows = [...data]
                        updatedRows.splice(index, 1)
                        setTimeout(() => {
                            setData(updatedRows)
                            resolve()
                        }, 2000)
                    }),
                    onRowUpdate: (updatedRow, oldRow) => new Promise((resolve, reject) => {
                        const index = oldRow.tableData.id;
                        const updatedRows = [...data]
                        updatedRows[index] = updatedRow
                        setTimeout(() => {
                            setData(updatedRows)
                            resolve()
                        }, 2000)
                    })

                }}
                options={{
                    actionsColumnIndex: -1, addRowPosition: "first"
                }}
            />
        </div>
    );
}

export default TestPage
