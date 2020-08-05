import React, {useEffect, useState} from "react";
import Button from "@material-ui/core/Button";
import {Form, Formik} from "formik";
import Container from "@material-ui/core/Container";
import Grid from "@material-ui/core/Grid";
import TextField from "@material-ui/core/TextField/TextField";
import * as Yup from "yup";
import Select from "@material-ui/core/Select/Select";
import businessService from "../service/business";
import {Alert, AlertTitle} from "@material-ui/lab";
import product from "../service/product";
import './createProduct/addProduct.css'
import './editProduct/editProduct.css'

const validationCreate = Yup.object().shape({
    name: Yup.string()
        .required('The Field is required')
        .min(3, "Name must be least 3 symbols"),

    mainDescription: Yup.string()
        .required("The field is required")
        .min(4, "Main Description must be least 4 symbols"),

    // price: Yup.number()
    //     .required("The field is required")
    //     .positive("Price must be positive"),
    //
    // countOfProduct: Yup.number()
    //     .required("The field is required")
    //     .positive("Product count must be positive"),

    businessName: Yup.string()
        .required("The field is required")

});

const validationEdit = Yup.object().shape({
    name: Yup.string()
        .min(3, "Name must be least 3 symbols"),

    mainDescription: Yup.string()
        .min(4, "Main Description must be least 4 symbols"),

    businessName: Yup.string()
});

const ProductCreateEdit = (props) => {

    const [inputList, setInputList] = useState([{key: "", value: ""}]);
    const [businesses, setBusinesses] = useState([]);
    const [isExistProduct, setIsExistProduct] = useState("");
    const [pictureFile, setPictureFile] = useState(null);
    const [descriptionError, setDescriptionError] = useState(false);

    const [price, setPrice] = useState(0);
    const [priceError, setPriceError] = useState("");

    const [countOfProduct, setCountOfProduct] = useState(0);
    const [countOfProductError, setCountOfProductError] = useState("");


    const asyncCall = () => {
        businessService.getAllBusinessName.then(data => setBusinesses(data.data));
    };

    useEffect(() => {
        asyncCall()
    }, []);

    const upload = (files) => {
        setPictureFile(files[0]);
    };

    const handleRemoveClick = index => {
        const list = [...inputList];
        list.splice(index, 1);
        setInputList(list);
    };

    const handleAddClick = () => {
        setInputList([...inputList, {key: "", value: ""}]);
    };

    const handleDescriptionChange = (e, index) => {
        const {name, value} = e.target;
        const list = [...inputList];
        list[index][name] = value;
        setInputList(list);
        validDescription(index);
    };

    const validDescription = (index) => {
        if (inputList[index]['value'] === "" && inputList[index]['key'] === "") {
            setDescriptionError(false);
        } else {
            if (inputList[index]['value'] === "" || inputList[index]['key'] === "") {
                setDescriptionError(true);
            } else {
                setDescriptionError(false);
            }
        }
    };

    const handelPriceChange = (value) => {
        if (!isNaN(value.target.value)) {
            setPriceError("");
            setPrice(value.target.value);
            validPrice(value.target.value)
        }else {
            setPriceError("Must be number")
        }
    };

    const validPrice = (value) => {
        value < 0 ? setPriceError("Must be positive number") : setPriceError("");
    };

    const handelCountOfProductChange = (value) => {
        if (!isNaN(value.target.value)) {
            setCountOfProductError("");
            setCountOfProduct(value.target.value);
            validCountOfProduct(value.target.value)
        }else {
            setCountOfProductError("Must be number")
        }
    };

    const validCountOfProduct = (value) => {
        value < 0 ? setCountOfProductError("Must be positive number") : setCountOfProductError("");
    };

    const createEditService = (values) => {
        const {name, mainDescription, businessName} = values;


        if (!descriptionError && !priceError && !countOfProductError) {
            let newInputList = [];

            inputList.forEach(list => {
                newInputList = [...newInputList, {[list['key']]: list['value']}]
            });

            const newValues = {name, price, mainDescription, countOfProduct, businessName, description: newInputList};

            let formData = new FormData();
            const productBlob = new Blob([JSON.stringify(newValues)], {type: 'application/json'});

            formData.append('pictureFile', pictureFile);

            if (props.isCreate) {
                formData.append('productCreateBindingModel', productBlob);

                const categoryName = props.isCategory ? document.location.pathname.substr(10) :
                    document.location.pathname.substr(10, document.location.pathname.indexOf("/subcategory") - 10);

                const subcategoryName = document.location.pathname.substr(document.location.pathname.indexOf("/subcategory") + 13);

                props.isCategory
                    ?
                    product.createCategoryProduct(formData, props.token, categoryName)
                        .then(data => document.location.pathname = "/category/" + categoryName)
                        .catch(err => setIsExistProduct(err.response.data))
                    :
                    product.createSubcategoryProduct(formData, props.token, categoryName, subcategoryName)
                        .then(data => document.location.pathname = "/category/" + categoryName + "/subcategory/" + subcategoryName)
                        .catch(err => setIsExistProduct(err.response.data))
            } else {


                formData.append('productEditBindingModel', productBlob);

                product.editProduct(formData, props.token, props.id)
                    .then(data => {
                        let products = [];

                        if (localStorage.getItem('productsToCart')) {
                            products = JSON.parse(localStorage.getItem('productsToCart'));
                        }

                        let product = products.filter(product => Number(product.id) === Number(data.data.id));

                        if (product.length > 0) {
                            product[0]['img'] = data.data.picture.url;
                            product[0]['name'] = data.data.name;
                            product[0]['price'] = data.data.price;

                            localStorage.setItem('productsToCart', JSON.stringify(products));
                        }

                        document.location.pathname = "/product/details/" + props.id
                    });
            }
        }
    };

    const getBusinesses = () => {
        return businesses.map((business) => {
            return (
                <option key={business.id} value={business.name}>{business.name}</option>
            )
        })
    };

    const showErr = message => {
        return (
            <Alert severity="error">
                <AlertTitle>Error</AlertTitle>
                {message}
            </Alert>
        )
    };

    return (
        <div className={!props.isCreate && "main-edit-product"}>
            <Formik
                initialValues={{
                    name: "",
                    mainDescription: "",
                    // price: 0,
                    // countOfProduct: 0,
                    businessName: "",
                    // key: "",
                    // value: ""
                }}
                onSubmit={createEditService}
                validationSchema={props.isCreate ? validationCreate : validationEdit}>

                {({errors, touched, name, mainDescription, businessName, handleChange}) => (

                    <Form>
                        <Container component="main" maxWidth="xs">
                            <form noValidate>

                                <Grid container spacing={2}>
                                    <Grid item xs={12}>
                                        <TextField
                                            id="name"
                                            name="name"
                                            helperText={touched.name ? errors.name : ""}
                                            error={touched.name && Boolean(errors.name)}
                                            label="Name"
                                            fullWidth
                                            value={name}
                                            onChange={handleChange(name || "")}
                                        />
                                    </Grid>
                                </Grid>

                                <Grid container spacing={2}>
                                    <Grid item xs={12}>
                                        <TextField
                                            id="mainDescription"
                                            name="mainDescription"
                                            helperText={touched.mainDescription ? errors.mainDescription : ""}
                                            error={touched.mainDescription && Boolean(errors.mainDescription)}
                                            label="Main Description"
                                            fullWidth
                                            value={mainDescription}
                                            onChange={handleChange(mainDescription || "")}
                                        />
                                    </Grid>
                                </Grid>

                                <Grid container spacing={2}>
                                    <Grid item xs={12}>
                                        <TextField
                                            id="price"
                                            name="price"
                                            helperText={touched.price ? errors.price : ""}
                                            error={touched.price && Boolean(errors.price)}
                                            label="Price"
                                            fullWidth
                                            onChange={e => handelPriceChange(e)}
                                        />
                                    </Grid>
                                </Grid>

                                {priceError !== "" ? showErr(priceError) : null}

                                <Grid container spacing={2}>
                                    <Grid item xs={12}>
                                        <TextField
                                            id="countOfProduct"
                                            name="countOfProduct"
                                            helperText={touched.countOfProduct ? errors.countOfProduct : ""}
                                            error={touched.countOfProduct && Boolean(errors.countOfProduct)}
                                            label="Count Of Product"
                                            fullWidth
                                            onChange={e => handelCountOfProductChange(e)}
                                        />
                                    </Grid>
                                </Grid>

                                {countOfProductError !== "" ? showErr(countOfProductError) : null}

                                <br/>

                                <label>Description <br/></label>
                                <Grid>
                                    {inputList.map((x, i) => {
                                        return (
                                            <div className="box">
                                                <input
                                                    name="key"
                                                    placeholder="Enter Key"
                                                    value={x.key}
                                                    onChange={e => handleDescriptionChange(e, i)}
                                                />
                                                <input
                                                    name="value"
                                                    placeholder="Enter Value"
                                                    value={x.value}
                                                    onChange={e => handleDescriptionChange(e, i)}
                                                />

                                                <div className="btn-box">
                                                    {inputList.length !== 1 && <button
                                                        className="mr10"
                                                        onClick={() => handleRemoveClick(i)}>ПРЕМАХНИ</button>}
                                                    {inputList.length - 1 === i &&
                                                    <button onClick={handleAddClick}>ДОБАВИ</button>}
                                                </div>
                                            </div>
                                        );
                                    })}
                                </Grid>


                                {descriptionError ? showErr("Must enter key and value") : null}

                                <Grid container spacing={2}>
                                    <Grid item xs={12}>
                                        <Select
                                            native
                                            fullWidth
                                            labelId="demo-simple-select-outlined-label"
                                            id="demo-simple-select-outlined"
                                            name="businessName"
                                            error={touched.businessName && Boolean(errors.businessName)}
                                            onChange={handleChange(businessName || "")}>

                                            <option value="">Избери фирма</option>
                                            {getBusinesses()}
                                        </Select>
                                    </Grid>
                                </Grid>


                                <Grid container spacing={2}>
                                    <Grid item xs={12}>
                                        <input onChange={(e) => upload(e.target.files)} type="file" name="file"
                                               id="file"
                                               className="input-file"/>
                                        <label htmlFor="file">Choose a file</label>
                                    </Grid>
                                </Grid>

                            </form>

                            <br/>

                            <Button type="submit" fullWidth variant="contained" color="primary">
                                {props.isCreate ? 'СЪЗДАЙ ПРОДУКТ' : 'ПРОМЕНИ ПРОДУКТ'}
                            </Button>

                            {isExistProduct !== "" ? showErr(isExistProduct) : null}

                        </Container>
                    </Form>
                )}
            </Formik>
        </div>
    );
};

export default ProductCreateEdit