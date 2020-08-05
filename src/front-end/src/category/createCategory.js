import {Form, Formik} from "formik";
import Container from "@material-ui/core/Container";
import Grid from "@material-ui/core/Grid";
import TextField from "@material-ui/core/TextField/TextField";
import Button from "@material-ui/core/Button";
import React, {Component} from "react";
import * as Yup from "yup";
import categoryService from "../service/category";
import {Alert, AlertTitle} from "@material-ui/lab";

const validation = Yup.object().shape({
    name: Yup.string()
        .required('Field is required')
        .min(3, "Title must be least 3 symbols"),
});


class CreateCategory extends Component {

    constructor(props) {
        super(props);

        this.state = {
            hasError: ""
        }
    }

    createCategory = (values) => {
        categoryService.addCategory(values, this.props.token)
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

    render() {
        return (
            <div className="main-edit-news">
                <Formik
                    initialValues={{name: ""}}
                    onSubmit={this.createCategory}
                    validationSchema={validation}>

                    {({errors, touched, name, handleChange}) => (

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
                                </form>

                                <br/>

                                <Button type="submit" fullWidth variant="contained" color="primary">
                                    СЪЗДАЙ КАТЕГОРИЯ
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

export default CreateCategory;