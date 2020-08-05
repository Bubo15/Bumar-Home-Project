import React, {Component} from "react";
import {Form, Formik} from "formik";
import Container from "@material-ui/core/Container";
import Grid from "@material-ui/core/Grid";
import TextField from "@material-ui/core/TextField/TextField";
import Button from "@material-ui/core/Button";
import * as Yup from "yup";
import './createBusiness/createBusiness.css'
import businessService from "../service/business";
import {Alert, AlertTitle} from "@material-ui/lab";

const validationCreate = Yup.object().shape({
    name: Yup.string()
        .required('Field is required')
        .min(3, "Name must be least 3 symbols"),
});

const validationEdit = Yup.object().shape({
    name: Yup.string()
        .min(3, "Name must be least 3 symbols"),
});

class abstractCreateEdit extends Component {

    constructor(props) {
        super(props);

        this.state = {
            pictureFile: null,
            hasError: ""
        }
    }

    createBusiness = (values) => {
        const bindingModel = this.props.isCreated ? "businessCreateBindingModel" : "businessEditBindingModel";
        let formData = new FormData();
        const businessBlob = new Blob([JSON.stringify(values)], {type: 'application/json'});
        formData.append('pictureFile', this.state.pictureFile);
        formData.append(bindingModel, businessBlob);

        this.props.isCreated
            ?
            businessService.createBusiness(formData, this.props.token).then(data => {document.location.pathname = '/api/business'})
                .catch(err => {this.setState({hasError: err.response.data})})
            :
            businessService.editBusiness(formData, this.props.token, this.props.id).then(data => {document.location.pathname = '/api/business'})
                .catch(err => {this.setState({hasError: err.response.data})})
    };

    upload = (files) => {
        this.setState({pictureFile: files[0]});
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
            <div className="main-add-business">
                <Formik
                    initialValues={{name: ""}}
                    onSubmit={this.createBusiness}
                    validationSchema={this.props.isCreated ? validationCreate : validationEdit}>

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

                                    <Grid container spacing={2}>
                                        <Grid item xs={12}>
                                            <input onChange={(e) => this.upload(e.target.files)} type="file" name="file"
                                                   id="file"
                                                   className="input-file"/>
                                            <label htmlFor="file">Choose a file</label>
                                        </Grid>
                                    </Grid>

                                </form>

                                <br/>

                                <Button type="submit" fullWidth variant="contained" color="primary">
                                    {this.props.isCreated ? 'СЪЗДАЙ ФИРМА' : 'ПРОМЕНИ ФИРМА'}
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

export default abstractCreateEdit;