import React, { useEffect, useState } from "react";
import Header from "../components/Header";
import {
  Button,
  Container,
  Grid,
  Rating,
  TextField,
  Typography,
} from "@mui/material";
import { Box } from "@mui/system";
import { DesktopDatePicker, LocalizationProvider } from "@mui/lab";
import DateAdapter from "@mui/lab/AdapterDateFns";
import { getBooks, getBooksFiltered } from "../redux-tool/booksSlice";
import { useDispatch, useSelector } from "react-redux";
import BooksList from "../components/BooksList";
import { makeStyles } from "@mui/styles";
import { createSearchParams, useSearchParams } from "react-router-dom";

const useStyles = makeStyles({
  buttons: {
    textTransform: "none",
    backgroundColor: "white",
    color: "#474747",
    border: "1px solid #474747",
    "&:hover": {
      backgroundColor: "#474747",
      color: "white",
    },
  },
  mainContentGrid: {
    display: "flex",
    // flexDirection: "column",
    flexWrap: "nowrap",
    justifyContent: "space-between",
  },
});

export default function HomePage() {
  const classes = useStyles();
  const dispatch = useDispatch();

  const [value, setValue] = React.useState(new Date());
  const [bookName, setBookName] = useState("");

  const handleChange = (newValue) => {
    setValue(newValue);
  };

  const [searchParams, setSearchParams] = useSearchParams();

  useEffect(() => {
    dispatch(getBooks());
  }, [dispatch]);

  const handleApplyFilter = () => {
    searchParams.set("bookname", bookName);
    setSearchParams(searchParams);
    dispatch(getBooksFiltered({ bookName }));
  };

  const handleSearchInput = (e) => {
    e.preventDefault();
    setBookName(e.target.value);
  };

  return (
    <>
      <Header />
      <Container maxWidth={"lg"} sx={{ pt: 8, pb: 6 }}>
        <Box sx={{ flexGrow: 1 }}>
          <Grid className={classes.mainContentGrid} container>
            <Grid item style={{ width: 900 }}>
              <BooksList />
            </Grid>
            <Grid
              className={classes.grid}
              item
              style={{
                display: "flex",
                flexDirection: "column",
                justifyContent: "flex-start",
                width: "466",
              }}
            >
              <Grid item style={{ marginBottom: "5px" }}>
                <Typography variant="h2" align="left">
                  Search...
                </Typography>
              </Grid>
              <Grid item>
                <TextField
                  id="outlined-basic"
                  label="Book name"
                  variant="outlined"
                  fullWidth
                  value={bookName}
                  onChange={handleSearchInput}
                />
              </Grid>
              <Grid item style={{ marginBottom: "5px" }}>
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
              <Grid item style={{ marginBottom: "5px" }}>
                <Button
                  type="submit"
                  size="large"
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
  );
}
