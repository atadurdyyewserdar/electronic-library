import React, { useEffect } from 'react'
import Header from '../components/Header'
import { Button, Container, Grid, Rating, TextField, Typography } from '@mui/material'
import { Box } from '@mui/system'
import { DesktopDatePicker, LocalizationProvider } from '@mui/lab'
import DateAdapter from '@mui/lab/AdapterDateFns';
import { getBooks } from '../redux-tool/booksSlice'
import { useDispatch, useSelector } from 'react-redux'
import BooksList from '../components/BooksList'
import { makeStyles } from '@mui/styles'

const useStyles = makeStyles({
    buttons: {
        textTransform: "none",
        backgroundColor: "white",
        color: "#474747",
        border: "1px solid #474747",
        '&:hover': {
            backgroundColor: '#474747',
            color: 'white',
        }
    },
    grid: {
        display: "flex",
        flexDirection: "column",
        justifyContent: "flex-start",
    },
    mainContentGrid: {
        display: "flex",
        flexDirection: "row",
        flexWrap: "nowrap",
        justifyContent: "flex-start",
    }
})

export default function HomePage() {
    const classes = useStyles();
    const [value, setValue] = React.useState(new Date());
    const [rateVal, setRateVal] = React.useState()
    const handleChange = (newValue) => {
        setValue(newValue);
    };

    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(getBooks());
    }, [dispatch])

    const handleApplyFilter = () => {

    }

    return (
        <>
            <Header />
            <Container maxWidth={'lg'} sx={{ pt: 8, pb: 6 }}>
                <Box sx={{ flexGrow: 1 }}>
                    <Grid className={classes.mainContentGrid} container>
                        <Grid item xs={12}>
                            <BooksList />
                        </Grid>
                        <Grid className={classes.grid} item>
                            <Grid item>
                                <Typography variant='h2' align='left' >
                                    Filters
                                </Typography>
                            </Grid>
                            <Grid item>
                                <TextField
                                    id="outlined-basic" label="Author" variant="outlined" fullWidth />
                            </Grid>
                            <Grid item>
                                <LocalizationProvider dateAdapter={DateAdapter}>
                                    <DesktopDatePicker
                                        label="Published date from"
                                        inputFormat="dd/MM/yyyy"
                                        value={value}
                                        onChange={handleChange}
                                        renderInput={(params) => <TextField {...params} />}
                                    />
                                </LocalizationProvider>
                            </Grid>
                            <Grid item>
                                <Typography component="legend">Rating</Typography>
                                <Rating
                                    name="simple-controlled"
                                    value={rateVal}
                                    onChange={(event, newValue) => {
                                        setRateVal(newValue);
                                    }}
                                />
                            </Grid>
                            <Grid item>
                                <Button
                                    type="submit"
                                    size='large'
                                    onClick={handleApplyFilter}
                                    className={classes.buttons}
                                >
                                    Apply filter
                                </Button>
                            </Grid>

                        </Grid>
                    </Grid>
                </Box>
            </Container>

        </>
    )
}
