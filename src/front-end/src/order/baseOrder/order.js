import React, {useEffect, useState} from "react";
import Header from "../../header/header";
import Footer from "../../footer/footer";
import SummeryOfOrder from "../../cart/summeryOfCart/summeryOfOrder";
import {Form, Formik} from "formik";
import Container from "@material-ui/core/Container";
import CssBaseline from "@material-ui/core/CssBaseline/CssBaseline";
import Typography from "@material-ui/core/Typography";
import Grid from "@material-ui/core/Grid";
import TextField from "@material-ui/core/TextField/TextField";
import Button from "@material-ui/core/Button";
import './order.css'
import * as Yup from "yup";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Radio from "@material-ui/core/Radio";
import RadioGroup from "@material-ui/core/RadioGroup";
import {Alert, AlertTitle} from "@material-ui/lab";
import orderService from "../../service/order";
import userInfoService from "../../service/userInfo";

const validationAuthenticated = Yup.object().shape({

    firstName: Yup.string()
        .required("The field is required")
        .min(2, "The name must be least 2 symbols")
        .matches(/^[[A-Z*][a-z]+/, "Name must be start with upper letter"),

    lastName: Yup.string()
        .required("The field is required")
        .min(2, "The name must be least 2 symbols")
        .matches(/^[[A-Z][a-z]+/, "Name must be start with upper letter"),

    phone: Yup.string()
        .required('Phone is required')
        .min(10, "Phone must be 10 symbols")
        .max(10, "Phone must be 10 symbols"),

    address: Yup.string()
        .required('Address is required')
        .min(5, "Address must be 5 symbols"),

    city: Yup.string()
        .required('City is required'),

    postCode: Yup.string()
        .required('Post Code is required')
        .min(4, "Post Code must be 4 symbols")
        .max(4, "Post Code must be 4 symbols")
});

const validationNonAuthenticated = Yup.object().shape({

    username: Yup.string()
        .required("The field is required")
        .min(2, "Username must be least 2 symbols"),

    firstName: Yup.string()
        .required("The field is required")
        .min(2, "The name must be least 2 symbols")
        .matches(/^[[A-Z*][a-z]+/, "Name must be start with upper letter"),

    lastName: Yup.string()
        .required("The field is required")
        .min(2, "The name must be least 2 symbols")
        .matches(/^[[A-Z][a-z]+/, "Name must be start with upper letter"),

    email: Yup.string()
        .required('Email is required')
        .email("Email must be valid"),

    phone: Yup.string()
        .required('Phone is required')
        .min(10, "Phone must be 10 symbols")
        .max(10, "Phone must be 10 symbols"),

    address: Yup.string()
        .required('Address is required')
        .min(5, "Address must be 5 symbols"),

    city: Yup.string()
        .required('City is required'),

    postCode: Yup.string()
        .required('Post Code is required')
        .min(4, "Post Code must be 4 symbols")
        .max(4, "Post Code must be 4 symbols")
});

const Order = (props) => {

    const [typeOfOrder, setTypeOfOrder] = useState(0);
    const [typeOfOrderErrors, setTypeOfOrderErrors] = useState("");
    const [user, setUser] = useState({});

    const isHaveProducts = localStorage.getItem("productsToCart");
    const classes = props;

    const fetchData = async () => {
        await userInfoService.getInfo(props.token).then(data => {
            setUser(data.data);
        })
    };

    useEffect(() => {
        fetchData();
    }, []);

    const getSumOfAllProduct = () => {
        let totalSum = 0;
        JSON.parse(isHaveProducts).map(product => {
            totalSum += product.productCount * product.price;
        });

        return totalSum + typeOfOrder - (props.token !== "null" ? 5 : 0);
    };

    const getAllProduct = () => {
        let countOfProduct = [];
        JSON.parse(isHaveProducts).map(product => {
            countOfProduct += 1
        });
        return countOfProduct;
    };

    const makeOrder = (values) => {

        if (typeOfOrder !== 0) {
            let products = [];
            JSON.parse(isHaveProducts).forEach(product => {
                products = [...products, {[product['id']]: product['productCount']}]
            });

            values.totalPrice = getSumOfAllProduct();
            values.products = products;

            if (props.token !== "null") {
                values.username = user.username;
                values.email = user.email;
            }


            let formData = new FormData();
            const orderBlob = new Blob([JSON.stringify(values)], {type: 'application/json'});

            formData.append('orderCreateBindingModel', orderBlob);

            if (window.confirm("Сигурен ли сте че искате да направите тази поръчка? В случай че я направите, можете да откажете до 2 дни.")) {
                orderService.createOrder(formData).then(data => {
                    localStorage.setItem("productsToCart", []);
                    document.location.pathname = "/"
                });
            }
        } else {
            setTypeOfOrderErrors("Must choose type of delivery")
        }
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
            <Header/>

            <div className="type-of-delivery">
                <h2>ТИП НА ДОСТАВКАТА</h2>
                <RadioGroup>
                    <FormControlLabel onClick={() => {
                        setTypeOfOrder(10);
                        setTypeOfOrderErrors("")
                    }} value="homeAddress" control={<Radio/>} label="Домашен адрес: 10 лв."/>
                    <FormControlLabel onClick={() => {
                        setTypeOfOrder(1);
                        setTypeOfOrderErrors("")
                    }} value="shopAddress" control={<Radio/>} label="Вземане от магазин: 1 лв."/>
                </RadioGroup>

                {typeOfOrderErrors !== "" ? showError(typeOfOrderErrors) : null}
            </div>

            <Formik
                initialValues={{
                    username: "",
                    firstName: "",
                    lastName: "",
                    email: "",
                    phone: "",
                    address: "",
                    city: "",
                    postCode: ""
                }}

                onSubmit={makeOrder}
                validationSchema={props.token !== "null" ? validationAuthenticated : validationNonAuthenticated}>

                {({errors, touched, firstName, lastName, username, email, phone, address, city, postCode, handleChange}) => (


                    <Form className="container-order">

                        <Container component="main" maxWidth="xs">
                            <CssBaseline/>
                            <div className={classes.paper}>
                                <Typography className="title-reg" component="h1" variant="h5">
                                    АДРЕС ЗА ДОСТАВКА
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
                                                label="Име"
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
                                                label="Фамилно Име"
                                                value={lastName}
                                                fullWidth
                                                onChange={handleChange(lastName || "")}
                                            />
                                        </Grid>

                                        {props.token === "null" && <Grid item xs={12} sm={6}>
                                            <TextField
                                                id="username"
                                                name="username"
                                                helperText={touched.username ? errors.username : ""}
                                                error={touched.username && Boolean(errors.username)}
                                                label="Username"
                                                value={username}
                                                fullWidth
                                                onChange={handleChange(username || "")}
                                            />
                                        </Grid>}

                                        {props.token === "null" && <Grid item xs={12} sm={6}>
                                            <TextField
                                                id="email"
                                                name="email"
                                                helperText={touched.email ? errors.email : ""}
                                                error={touched.email && Boolean(errors.email)}
                                                label="E-mail"
                                                value={email}
                                                fullWidth
                                                onChange={handleChange(email || "")}
                                            />
                                        </Grid>}

                                        <Grid item xs={12} sm={6}>
                                            <TextField
                                                id="phone"
                                                name="phone"
                                                helperText={touched.phone ? errors.phone : ""}
                                                error={touched.phone && Boolean(errors.phone)}
                                                label="Tелефон"
                                                value={phone}
                                                fullWidth
                                                onChange={handleChange(phone || "")}
                                            />
                                        </Grid>

                                        <Grid item xs={12} sm={6}>
                                            <TextField
                                                id="address"
                                                name="address"
                                                helperText={touched.address ? errors.address : ""}
                                                error={touched.address && Boolean(errors.address)}
                                                label="Адрес"
                                                value={address}
                                                fullWidth
                                                onChange={handleChange(address || "")}
                                            />
                                        </Grid>

                                        <Grid item xs={12} sm={6}>
                                            <TextField
                                                id="city"
                                                name="city"
                                                helperText={touched.city ? errors.city : ""}
                                                error={touched.city && Boolean(errors.city)}
                                                label="Град"
                                                value={city}
                                                fullWidth
                                                onChange={handleChange(city || "")}
                                            />
                                        </Grid>

                                        <Grid item xs={12} sm={6}>
                                            <TextField
                                                id="postCode"
                                                name="postCode"
                                                helperText={touched.postCode ? errors.postCode : ""}
                                                error={touched.postCode && Boolean(errors.postCode)}
                                                label="Пощенски код"
                                                value={postCode}
                                                fullWidth
                                                onChange={handleChange(postCode || "")}
                                            />
                                        </Grid>

                                    </Grid>
                                </form>

                                <br/>

                                <Button type="submit" fullWidth variant="contained" color="primary">
                                    КУПИ
                                </Button>

                            </div>
                        </Container>
                    </Form>
                )}

            </Formik>

            <SummeryOfOrder countOfItems={getAllProduct().length} sumOfAllProduct={getSumOfAllProduct()}
                            isAuthenticated={props.token}/>

            <Footer/>
        </div>
    )
};

export default Order;