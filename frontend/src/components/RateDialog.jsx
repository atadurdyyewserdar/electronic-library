import { Button, Dialog, DialogActions, DialogTitle, Rating } from "@mui/material";
import axios from "axios";
import React, { useState } from "react";

const RateDialog = ({ onClose, open, bookId }) => {
  const [rateValue, setRateValue] = useState(0);
  const { user } = useState((state) => state.auth);

  const handleSubmit = async () => {
    const body = {
      star: rateValue,
      efileId: bookId,
      username: user.userName,
    };
    await axios
      .post(`http://localhost:8080/resource/books/${bookId}/rate`, body)
      .catch((err) => {
        console.log(err);
      });
    onClose(false);
  };
  return (
    <Dialog onClose={() => onClose(false)} open={open}>
      <DialogTitle>Rate this book</DialogTitle>
      <Rating
        size="small"
        name="Rate"
        value={rateValue}
        onChange={(event, newValue) => {
          setRateValue(newValue);
        }}
      />
      <DialogActions>
        <Button onClick={handleSubmit} variant="outlined" size="small">
          Submit
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default RateDialog;
