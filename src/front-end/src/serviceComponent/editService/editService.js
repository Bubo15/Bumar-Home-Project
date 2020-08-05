/* eslint-disable no-undef */
import React, {Component} from "react";
import serviceService from "../../service/service";
import {Form, Formik} from "formik";
import Container from "@material-ui/core/Container";
import Grid from "@material-ui/core/Grid";
import TextField from "@material-ui/core/TextField/TextField";
import Button from "@material-ui/core/Button";
import * as Yup from "yup";
import {Alert, AlertTitle} from "@material-ui/lab";

const validation = Yup.object().shape({
    name: Yup.string()
        .min(3, "Name must be least 3 symbols"),

    description: Yup.string()
        .min(10, "Description must be least 10 symbols"),
});


class EditService extends Component {

    constructor(props) {
        super(props);

        this.state = {
            hasError: "",
            pictureFile: null
        }
    }

    editService = (values) => {
        let formData = new FormData();
        const serviceEditBlob = new Blob([JSON.stringify(values)], {type: 'application/json'});
        formData.append('pictureFile', this.state.pictureFile);
        formData.append('serviceEditBindingModel', serviceEditBlob);

        serviceService.editService(formData, this.props.token, this.props.name)
            .then(data => {
            document.location.pathname = `/api/service/${data.data.name}`
        }).catch((e) => {
            this.setState({hasError: e.response.data});
        })
    };

    upload = (files) => {
        this.setState({pictureFile: files[0]});
    };

    // myWidget = () => cloudinary.createUploadWidget({
    //         cloudName: 'bubo1551',
    //         appKey: '111176256725993',
    //         uploadPreset: 'bumar_picture'
    //     }, (error, result) => {
    //         if (!error && result && result.event === "success") {
    //             console.log('Done! Here is the image info: ', result.info);
    //             this.setState({url: result.info.url})
    //         }
    //     }
    // );

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
            <div className="main-add-news">
                <Formik
                    initialValues={{name: "", description: ""}}
                    onSubmit={this.editService}
                    validationSchema={validation}>

                    {({errors, touched, name, description, handleChange}) => (

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
                                                id="description"
                                                name="description"
                                                helperText={touched.description ? errors.description : ""}
                                                error={touched.description && Boolean(errors.description)}
                                                label="Description"
                                                fullWidth
                                                value={description}
                                                onChange={handleChange(description || "")}
                                            />
                                        </Grid>
                                    </Grid>

                                    {/*<Grid container spacing={2}>*/}
                                    {/*    <Grid item xs={12}>*/}
                                    {/*        <div onClick={() => this.myWidget().open()} id="upload_widget"*/}
                                    {/*             className="cloudinary-button">Upload files*/}
                                    {/*        </div>*/}
                                    {/*    </Grid>*/}
                                    {/*</Grid>*/}



                                    <Grid container spacing={2}>
                                        <Grid item xs={12}>
                                            <input onChange={(e) => this.upload(e.target.files)} type="file" name="file" id="file"
                                                   className="input-file"/>
                                            <label htmlFor="file">Choose a file</label>
                                        </Grid>
                                    </Grid>

                                </form>

                                <br/>

                                <Button type="submit" fullWidth variant="contained" color="primary">
                                    ПРОМЕНИ УСЛУГА
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

export default EditService;