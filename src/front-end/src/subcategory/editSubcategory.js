import {Form, Formik} from "formik";
import Container from "@material-ui/core/Container";
import Grid from "@material-ui/core/Grid";
import TextField from "@material-ui/core/TextField/TextField";
import Button from "@material-ui/core/Button";
import React, {Component} from "react";
import * as Yup from "yup";
import {Alert, AlertTitle} from "@material-ui/lab";
import Select from "@material-ui/core/Select/Select";
import subcategoryService from "../service/subcategory";

const validation = Yup.object().shape({
    name: Yup.string()
        .min(3, "Title must be least 3 symbols"),

    categoryName: Yup.string()
});


class EditSubcategory extends Component {

    constructor(props) {
        super(props);

        this.state = {
            categories: [],
            hasError: ""
        }
    }

    componentDidMount() {
        subcategoryService.getAllCategoriesName.then(data => {
            this.setState({categories: data.data});
        });
    }

    editSubcategory = (values) => {
        subcategoryService.editSubcategory(this.props.category,this.props.name , values, this.props.token)
            .then(data => document.location.pathname = "/")
            .catch((e) => this.setState({hasError: e.response.data}))
    };

    showError = () => {
        return (
            <Alert severity="error">
                <AlertTitle>Error</AlertTitle>
                {this.state.hasError}
            </Alert>
        )
    };

    getCategories = () => {
        return this.state.categories.map((category) => {
            return (
                <option key={category.id} value={category.name}>{category.name}</option>
            )
        })
    };

    render() {
        return (
            <div className="main-edit-news">
                <Formik
                    initialValues={{name: "", categoryName: ""}}
                    onSubmit={this.editSubcategory}
                    validationSchema={validation}>

                    {({errors, touched, name, categoryName, handleChange}) => (

                        <Form>
                            <Container component="main" maxWidth="xs">
                                <form noValidate>

                                    <br/>

                                    <Grid container spacing={2}>
                                        <Grid item xs={12}>
                                            <Select
                                                native
                                                fullWidth
                                                labelId="demo-simple-select-outlined-label"
                                                id="demo-simple-select-outlined"
                                                name="categoryName"
                                                error={touched.categoryName && Boolean(errors.categoryName)}
                                                onChange={handleChange(categoryName || "")}>

                                                <option value="">Избери категория</option>
                                                {this.getCategories()}
                                            </Select>
                                        </Grid>
                                    </Grid>

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

                                </form>

                                <br/>

                                <Button type="submit" fullWidth variant="contained" color="primary">
                                    ПРОМЕНИ ПОДКАТЕГОРИЯ
                                </Button>

                                {this.state.hasError !== "" ? this.showError() : null}

                            </Container>
                        </Form>
                    )}
                </Formik>
            </div>
        )
    }
}

export default EditSubcategory;