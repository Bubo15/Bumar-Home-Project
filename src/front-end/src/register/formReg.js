import React, {Component, useState} from "react";
import {Formik, Form} from "formik";
import * as Yup from "yup"
import TextField from "@material-ui/core/TextField/TextField";
import Container from "@material-ui/core/Container";
import CssBaseline from "@material-ui/core/CssBaseline";
import Avatar from "@material-ui/core/Avatar";
import Typography from "@material-ui/core/Typography";
import Grid from "@material-ui/core/Grid";
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Button from "@material-ui/core/Button";
import './register.css'
import {Alert, AlertTitle} from "@material-ui/lab";
import userService from "../service/auth";

const validation = Yup.object().shape({
    firstName: Yup.string()
        .required("The field is required")
        .min(2, "The name must be least 2 symbols")
        .matches(/^[[A-Z*][a-z]+/, "Name must be start with upper letter"),

    username: Yup.string()
        .required("The field is required")
        .min(2, "Username must be least 2 symbols")
        .max(8, "Username must be at most 8 symbols"),

    lastName: Yup.string()
        .required("The field is required")
        .min(2, "The name must be least 2 symbols")
        .matches(/^[[A-Z][a-z]+/, "Name must be start with upper letter"),

    email: Yup.string()
        .required('Email is required')
        .email("Email must be valid"),

    password: Yup.string()
        .required('Password is required')
        .min(6, "Minimum 6 symbols"),

    confirmPassword: Yup.string()
        .required('ConfirmPassword is required')
        .oneOf([Yup.ref('password'), null], 'Passwords must match')
});

const Register = (props) => {

    const [classes, setClasses] = useState(props);
    const [isTouched, setIsTouched] = useState(true);
    const [hasError, setHasError] = useState("");

    const redirectTo = (path) => {
        document.location.pathname = path;
    };

    const handleClick = () => {
        props.func(!isTouched);
        setIsTouched(!isTouched);
    };

    const registerUser = async (values) => {
        await userService.register(values)
            .then(data => {
                props.redirectTo !== "/preOrder" ? handleClick() : redirectTo(props.redirectTo)
            })
            .catch(err => {
                setHasError(err.response.data);
            });
    };

    const showError = () => {
        return (
            <Alert severity="error">
                <AlertTitle>Error</AlertTitle>
                {hasError}
            </Alert>
        )
    };

    return (
        <div>
            <Formik
                initialValues={{
                    username: "",
                    firstName: "",
                    lastName: "",
                    email: "",
                    password: "",
                    confirmPassword: ""
                }}
                onSubmit={registerUser}
                validationSchema={validation}>

                {({errors, touched, username, firstName, lastName, email, password, confirmPassword, handleChange}) => (


                    <Form className="container-register">

                        <Container component="main" maxWidth="xs">
                            <CssBaseline/>
                            <div className={classes.paper}>
                                <Avatar className="avatar">
                                    <LockOutlinedIcon/>
                                </Avatar>
                                <Typography className="title-reg" component="h1" variant="h5">
                                    СЪЗДАЙТЕ СИ ПРОФИЛ В BUMAR HOME
                                </Typography>
                                <br/>
                                <form noValidate>

                                    <Grid container spacing={2}>
                                        <Grid item xs={12} sm={6}>
                                            <TextField
                                                id="firstName"
                                                name="firstName"
                                                helperText={touched.firstName ? errors.firstName : ""}
                                                error={touched.firstName && Boolean(errors.firstName)}
                                                label="First Name"
                                                value={firstName}
                                                fullWidth
                                                onChange={handleChange(firstName || "")}
                                            />
                                        </Grid>

                                        <Grid item xs={12} sm={6}>
                                            <TextField
                                                id="lastName"
                                                name="lastName"
                                                helperText={touched.lastName ? errors.lastName : ""}
                                                error={touched.lastName && Boolean(errors.lastName)}
                                                label="Last Name"
                                                value={lastName}
                                                fullWidth
                                                onChange={handleChange(lastName || "")}
                                            />
                                        </Grid>

                                        <Grid item xs={12}>
                                            <TextField
                                                id="username"
                                                name="username"
                                                helperText={touched.username ? errors.username : ""}
                                                error={touched.username && Boolean(errors.username)}
                                                label="Username"
                                                fullWidth
                                                value={username}
                                                onChange={handleChange(username || "")}
                                            />
                                        </Grid>

                                        <Grid item xs={12}>
                                            <TextField
                                                id="email"
                                                name="email"
                                                helperText={touched.email ? errors.email : ""}
                                                error={touched.email && Boolean(errors.email)}
                                                label="E-mail"
                                                fullWidth
                                                value={email}
                                                onChange={handleChange(email || "")}
                                            />
                                        </Grid>

                                        <Grid item xs={12}>
                                            <TextField
                                                id="password"
                                                name="password"
                                                helperText={touched.password ? errors.password : ""}
                                                error={touched.password && Boolean(errors.password)}
                                                label="Password"
                                                fullWidth
                                                type="password"
                                                value={password}
                                                onChange={handleChange(password || "")}
                                            />
                                        </Grid>

                                        <Grid item xs={12}>
                                            <TextField
                                                id="confirmPassword"
                                                name="confirmPassword"
                                                helperText={touched.confirmPassword ? errors.confirmPassword : ""}
                                                error={touched.confirmPassword && Boolean(errors.confirmPassword)}
                                                label="Confirm Password"
                                                fullWidth
                                                type="password"
                                                value={confirmPassword}
                                                onChange={handleChange(confirmPassword || "")}
                                            />
                                        </Grid>

                                    </Grid>
                                </form>

                                <br/>

                                <Button type="submit" fullWidth variant="contained" color="primary">
                                    Sign Up
                                </Button>

                                {hasError !== "" ? showError() : null}

                                <br/>
                                <br/>

                                {props.redirectTo !== "/preOrder" && <Grid container justify="flex-end">
                                    <Grid item>
                                        <div onClick={() => handleClick()}>
                                            Already have an account? Sign in
                                        </div>
                                    </Grid>
                                </Grid>}

                            </div>
                        </Container>
                    </Form>
                )}

            </Formik>

        </div>
    )
};

export default Register;