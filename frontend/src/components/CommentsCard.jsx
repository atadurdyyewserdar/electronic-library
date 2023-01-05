import { Box, Rating, Typography } from "@mui/material";
import React from "react";

const CommentsCard = ({comment, firstName, lastName}) => {
  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "start",
      }}
    >
      <Typography fontWeight="bold">
        {firstName} {lastName}
      </Typography>
      <Typography fontSize="small" textAlign="start">
        {comment}
      </Typography>
    </Box>
  );
};

export default CommentsCard;
