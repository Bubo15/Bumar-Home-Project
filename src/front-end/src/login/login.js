import React, {Component, useState} from "react";
import TextField from "@material-ui/core/TextField/TextField";
import Container from "@material-ui/core/Container";
import CssBaseline from "@material-ui/core/CssBaseline";
import Avatar from "@material-ui/core/Avatar";
import Typography from "@material-ui/core/Typography";
import Grid from "@material-ui/core/Grid";
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Button from "@material-ui/core/Button";
import {AlertTitle, Alert} from '@material-ui/lab';
import userService from "../service/auth";

const Login = (props) => {

    const [username, setUsername] = useState("");
    const [usernameErr, setUsernameErr] = useState("");

    const [password, setPassword] = useState("");
    const [passwordErr, setPasswordErr] = useState("");


    const [classes, setClasses] = useState(props);
    const [isTouched, setIsTouched] = useState(true);
    const [error, setError] = useState(false);


    const handleClick = () => {
        props.func(isTouched);
        setIsTouched(!isTouched);
    };

    const handleChangeUsername = (e) => {
        let username = e.target.value;
        setUsername(username);
        validUsername(username);
    };

    const validUsername = (value) => {
        value.length < 2 ? setUsernameErr("Username must be least 2 symbols") : setUsernameErr("")
        value.length > 8 ? setUsernameErr("Username must at most 8 symbols") : setUsernameErr("")
    };

    const handleChangePassword = (e) => {
        let password = e.target.value;
        setPassword(password);
        validPassword(password);
    };

    const validPassword = (value) => {
        value.length < 6 ? setPasswordErr("Password must be least 6 symbols") : setPasswordErr("")
    };

    const loginUser = async () => {
        await userService.login({username, password})
            .then((mainData) => {
                userService.getCurrentUserRoles(mainData.headers.authorization)
                    .then((data) => {
                        localStorage.setItem("auth", mainData.headers.authorization);
                        localStorage.setItem("roles", data.data);
                    });
                document.location.pathname = props.redirectTo;
            })
            .catch(e => {
               setError(true)
            });
    };

    const showError = (message) => {
        return (
            <Alert severity="error">
                <AlertTitle>Error</AlertTitle>
                {message}
            </Alert>
        )
    };

    return (
        <div>

            <div className="container-register">

                <Container component="main" maxWidth="xs">
                    <CssBaseline/>
                    <div className={classes.paper}>
                        <Avatar className="avatar">
                            <LockOutlinedIcon/>
                        </Avatar>
                        <Typography className="title-reg" component="h1" variant="h5">
                            РЕГИСТРИРАНИ ПОТРЕБИТЕЛИ
                        </Typography>
                        <br/>
                        <form>
                            <Grid container spacing={2}>

                                <Grid item xs={12}>
                                    <TextField
                                        id="username"
                                        name="username"
                                        label="Username"
                                        fullWidth
                                        onChange={e => handleChangeUsername(e)}
                                    />
                                </Grid>

                                {usernameErr !== "" ? showError(usernameErr) : null}

                                <Grid item xs={12}>
                                    <TextField
                                        id="password"
                                        name="password"
                                        label="Password"
                                        fullWidth
                                        type="password"
                                        onChange={e => handleChangePassword(e)}
                                    />
                                </Grid>

                                {passwordErr !== "" ? showError(passwordErr) : null}

                            </Grid>
                        </form>

                        <br/>

                        {error ? showError("Invalid Username or Password!") : null}

                        <br/>

                        <Button onClick={() => loginUser()} type="submit" fullWidth variant="contained"
                                color="primary">
                            Sign in
                        </Button>

                        <br/>
                        <br/>

                        {props.redirectTo !== "/order" && <Grid container justify="flex-end">
                            <Grid item>
                                <div onClick={() => handleClick()}>
                                    Create new account? Sign up
                                </div>
                            </Grid>
                        </Grid>}

                    </div>
                </Container>
            </div>
        </div>
    )
};

export default Login;