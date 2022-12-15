import * as React from 'react';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import { Link, useNavigate } from 'react-router-dom';
import { CardActionArea, Grid } from '@mui/material';
import imageU from './default.jpg';
import { makeStyles } from '@mui/styles'
import Rating from '@mui/material/Rating';

const useStyles = makeStyles({
    multiLineEllipsis: {
        overflow: "hidden",
        textOverflow: "ellipsis",
        width: 200,
        fontSize: 14
    },
    buttons: {
        textTransform: "none",
        backgroundColor: "white",
        color: "#474747",
        border: "1px solid #474747",
        '&:hover': {
            backgroundColor: '#474747',
            color: 'white',
        }
    }
});

export default function BooksCard(props) {
    const classes = useStyles();
    const navigate = useNavigate()
    const handleClick = () => {
        navigate(`/books/${props.obj.id}`, { state: props.obj })
    }
    return (
        <Card elevation={2} sx={{ width: 230, height: 310, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            <CardMedia
                component="img"
                image={imageU}
                sx={{ maxWidth: 120, maxHeight: 200, mt: 1 }}
            />
            <CardContent sx={{ display: "flex", alignItems: "flex-start", flexDirection: "column" }}>
                <Typography align="left" noWrap className={classes.multiLineEllipsis}>
                    {props.obj.bookName}
                </Typography>
                <Rating name="Rate" value={4} readOnly />
            </CardContent>
            <CardActions>
                <Button size="small" className={classes.buttons}>Share</Button>
                <Button size="small" className={classes.buttons}>Add to...</Button>
                <Button onClick={handleClick} size="small" className={classes.buttons}>Read</Button>
            </CardActions>
        </Card >
    );
}

/* <Typography variant="body2" color="text.secondary" align={'left'}>
{/* {props.obj.description} */
//serdar
// </Typography> */}