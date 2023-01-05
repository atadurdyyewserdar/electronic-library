import {
  Box,
  Button,
  CardMedia,
  Container,
  Dialog,
  DialogActions,
  DialogTitle,
  Divider,
  Grid,
  Rating,
  TextField,
  Typography,
} from "@mui/material";
import StarIcon from "@mui/icons-material/Star";
import axios from "axios";
import React, { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { useSelector } from "react-redux";
import { useNavigate, useParams } from "react-router";
import imageU from "../components/default.jpg";
import Header from "../components/Header";
import { addComment, getBookById, updateRate } from "../redux-tool/booksSlice";
import { saveAs } from "file-saver";
import CommentsCard from "../components/CommentsCard";
import { useAuth } from "../hooks/useAuth";
const BookPage = () => {
  const { status, book, comments, rating } = useSelector(
    (state) => state.books
  );
  const { user } = useSelector((state) => state.auth);
  const { isAuth } = useAuth();
  const { id } = useParams();
  const dispatch = useDispatch();

  const [errorDownload, setErrorDownload] = useState(null);

  useEffect(() => {
    if (isAuth) {
      const username = user.username;
      dispatch(getBookById({ id, username }));
      return;
    }
    dispatch(getBookById({ id }));
  }, [id]);

  const handleDownload = async () => {
    if (id) {
      const data = await axios
        .get(`http://localhost:8080/resource/download/${id}`, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
          responseType: "arraybuffer",
        })
        .catch((err) => {
          console.log(err);
          setErrorDownload(err);
        });
      const dataR = await data.data;
      const blob = new Blob([dataR], { type: "application/pdf;charset=utf-8" });
      const fileName = "".concat(book.bookName).concat(".pdf");
      saveAs(blob, fileName);
    }
  };

  const [comment, setComment] = useState("");

  const handleCommentClick = () => {
    if (!isAuth) {
      navigate("/login");
      return;
    }
    const obj = {
      userName: user.username,
      lastName: user.lastName,
      firstName: user.firstName,
      comment: comment,
      bookId: id,
    };
    dispatch(addComment(obj));
    setComment("");
  };
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);
  const [rateValue, setRateValue] = useState(0);
  const handleSubmitRate = async () => {
    if (!isAuth) {
      navigate("/login");
      return;
    }
    const body = {
      star: rateValue,
      efileId: id,
      username: user.username,
    };
    const data = await axios
      .post(`http://localhost:8080/resource/books/${id}/rate`, body)
      .catch((err) => {
        console.log(err);
      });
    console.log("body: ", body);
    setOpen(false);
    dispatch(updateRate(data.data));
  };

  return (
    <>
      <Header />
      {status === "loading" && <Typography>Loading...</Typography>}
      {status === "resolved" && (
        <Container>
          <Box sx={{ paddingTop: 5 }}>
            <Box
              sx={{
                display: "flex",
                flexDirextion: "column",
                gap: "30px",
                pb: 5,
              }}
            >
              <Box sx={{ width: "250px" }}>
                <CardMedia
                  component="img"
                  image={imageU}
                  sx={{ maxWidth: 200, maxHeight: 280, mt: 1 }}
                />
              </Box>
              <Box
                sx={{
                  display: "flex",
                  justifyContent: "space-between",
                  flexDirection: "column",
                }}
              >
                <Box
                  sx={{
                    display: "flex",
                    flexDirection: "column",
                    width: "700px",
                    pt: 1,
                  }}
                >
                  <Grid sx={{ display: "flex", gap: 2, pb: 1 }}>
                    <Typography fontSize={15} fontWeight={700}>
                      Name:
                    </Typography>
                    <Typography fontSize={15} fontWeight={700}>
                      {book.bookName}
                    </Typography>
                  </Grid>
                  <Grid sx={{ display: "flex", gap: 2, pb: 1 }}>
                    <Typography fontSize={15} fontWeight={700}>
                      Author:
                    </Typography>
                    <Typography fontSize={15} fontWeight={700}>
                      {/* {book.author} */} Uknown
                    </Typography>
                  </Grid>
                  <Grid sx={{ display: "flex", gap: 2, pb: 1 }}>
                    <Typography fontSize={15} fontWeight={700}>
                      Published Date:
                    </Typography>
                    <Typography fontSize={15} fontWeight={700}>
                      Uknown
                    </Typography>
                  </Grid>
                  <Grid sx={{ display: "flex", gap: 2, pb: 1 }}>
                    <Typography fontSize={15} fontWeight={700}>
                      Overall rating:
                    </Typography>
                    <Rating
                      size="small"
                      name="Rate"
                      precision={0.5}
                      value={book.averageRating}
                      readOnly
                    />
                  </Grid>
                  <Grid
                    sx={{
                      display: "flex",
                      flexDirection: "column",
                      textAlign: "start",
                    }}
                  >
                    <Typography fontSize="small" fontWeight="700">
                      Description
                    </Typography>
                    <Typography fontSize="small">
                      Lorem ipsum, dolor sit amet consectetur adipisicing elit.
                      In libero sint tenetur autem illum quos cupiditate,
                      placeat rerum ea quasi nulla facere facilis molestias
                      commodi recusandae eaque, perferendis neque suscipit.Lorem
                      ipsum, dolor sit amet consectetur adipisicing elit. In
                      libero sint tenetur autem illum quos cupiditate, placeat
                      rerum ea quasi nulla facere facilis molestias commodi
                      recusandae eaque, perferendis neque suscipit.
                    </Typography>
                  </Grid>
                </Box>
                <Box
                  sx={{
                    textAlign: "start",
                    display: "flex",
                    gap: 1,
                    alignItems: "center",
                  }}
                >
                  <Button
                    onClick={handleDownload}
                    href="#"
                    variant="outlined"
                    size="small"
                    sx={{}}
                  >
                    Download
                  </Button>
                  {rating.username === "" ? (
                    <div>
                      <Button
                        onClick={() => setOpen(true)}
                        href="#"
                        variant="outlined"
                        size="small"
                        sx={{}}
                        startIcon={<StarIcon color="warning" />}
                      >
                        Rate
                      </Button>
                      {/* <RateDialog onClose={setOpen} open={open} bookId={id} /> */}
                      <Dialog onClose={() => setOpen(false)} open={open}>
                        <Box
                          sx={{
                            p: 5,
                            display: "flex",
                            justifyContent: "center",
                            alignItems: "center",
                            flexDirection: "column",
                          }}
                        >
                          <DialogTitle>Rate this book</DialogTitle>
                          <Rating
                            name="Rate"
                            value={rateValue}
                            onChange={(event, newValue) => {
                              setRateValue(newValue);
                            }}
                          />
                        </Box>
                        <DialogActions>
                          <Button
                            onClick={() => handleSubmitRate()}
                            variant="contained"
                            size="small"
                          >
                            Submit
                          </Button>
                        </DialogActions>
                      </Dialog>
                    </div>
                  ) : (
                    <Grid
                      sx={{
                        display: "flex",
                        gap: 1,
                        border: "1px solid black",
                        justifyContent: "center",
                        width: "190px",
                        height: "29px",
                        borderRadius: 1,
                        pt: "2px",
                      }}
                    >
                      <Typography fontSize={15} fontWeight={700}>
                        Your rate:{" "}
                      </Typography>
                      <Rating
                        size="small"
                        name="Rate"
                        value={rating.star}
                        readOnly
                      />
                    </Grid>
                  )}
                </Box>
              </Box>
            </Box>
            <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
              <TextField
                margin="normal"
                fullWidth
                label="Your comment"
                type="text"
                size="small"
                value={comment}
                onChange={(e) => setComment(e.target.value)}
              />
              <Button
                href="#"
                onClick={handleCommentClick}
                variant="contained"
                sx={{ mt: "5px" }}
              >
                Comment
              </Button>
            </Box>
            <Box sx={{ display: "flex", flexDirection: "column" }}>
              {comments.map((v, i) => (
                <Grid sx={{ pl: "2px", pb: 2 }} key={i}>
                  <CommentsCard
                    comment={v.comment}
                    firstName={v.firstName}
                    lastName={v.lastName}
                  />
                  <Divider />
                </Grid>
              ))}
            </Box>
          </Box>
        </Container>
      )}
    </>
  );
};

export default BookPage;
