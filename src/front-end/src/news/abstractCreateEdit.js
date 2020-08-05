/* eslint-disable no-undef */
import React, {Component} from "react";
import {Form, Formik} from "formik";
import Container from "@material-ui/core/Container";
import Grid from "@material-ui/core/Grid";
import TextField from "@material-ui/core/TextField/TextField";
import Button from "@material-ui/core/Button";
import * as Yup from "yup";
import './createNews/createNews.css'
import newsService from "../service/news";

const validationCreate = Yup.object().shape({
    title: Yup.string()
        .required('The field is required')
        .min(3, "Title must be least 3 symbols"),

    text: Yup.string()
        .required("The field is required")
        .min(10, "Description must be least 10 symbols"),
});

const validationEdit = Yup.object().shape({
    title: Yup.string()
        .min(3, "Title must be least 3 symbols"),

    text: Yup.string()
        .min(10, "Description must be least 10 symbols"),
});

class AbstractCreateEdit extends Component {

    constructor(props) {
        super(props);

        this.state = {
            pictureFile: null,
        }
    };

    postNews = (values) => {
        const bindingModel = this.props.isCreate === true ? "newsCreateBindingModel" : "newsEditBindingModel";
        let formData = new FormData();
        const newsBlob = new Blob([JSON.stringify(values)], {type: 'application/json'});
        formData.append('pictureFile', this.state.pictureFile);
        formData.append(bindingModel, newsBlob);

        this.props.isCreate
            ?
            newsService.createNews(formData, this.props.token).then(data => {
                document.location.pathname = '/api/news'
            })
            :
            newsService.editNews(formData, this.props.token, this.props.id).then(data => {
                document.location.pathname = '/api/news'
            });

    };

    upload = (files) => {
        this.setState({pictureFile: files[0]});
    };

    // ReactJS integration - set url and post to back-end
    //  myWidget = () => cloudinary.createUploadWidget({
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

    render() {
        return (
            <div className="main-add-news">
                <Formik
                    initialValues={{title: "", text: ""}}
                    onSubmit={this.postNews}
                    validationSchema={this.props.isCreate ? validationCreate : validationEdit}>

                    {({errors, touched, title, text, handleChange}) => (

                        <Form>
                            <Container component="main" maxWidth="xs">
                                <form noValidate>

                                    <Grid container spacing={2}>
                                        <Grid item xs={12}>
                                            <TextField
                                                id="title"
                                                name="title"
                                                helperText={touched.title ? errors.title : ""}
                                                error={touched.title && Boolean(errors.title)}
                                                label="Title"
                                                fullWidth
                                                value={title}
                                                onChange={handleChange(title || "")}
                                            />
                                        </Grid>
                                    </Grid>

                                    <Grid container spacing={2}>
                                        <Grid item xs={12}>
                                            <TextField
                                                id="text"
                                                name="text"
                                                helperText={touched.text ? errors.text : ""}
                                                error={touched.text && Boolean(errors.text)}
                                                label="Text"
                                                fullWidth
                                                value={text}
                                                onChange={handleChange(text || "")}
                                            />
                                        </Grid>
                                    </Grid>

                                    {/*<Grid container spacing={2}>*/}
                                    {/*    <Grid item xs={12}>*/}
                                    {/*        <div onClick={() => this.myWidget().open()} id="upload_widget"*/}
                                    {/*             className="cloudinary-button">Upload files*/}
                                    {/*        </div>*/}
                                    {/*    </Grid>*/}
                                    {/*/!*</Grid> */}

                                    <Grid container spacing={2}>
                                        <Grid item xs={12}>
                                            <input onChange={(e) => this.upload(e.target.files)} type="file" name="file"
                                                   id="file" className="input-file"/>
                                            <label htmlFor="file">Choose a file</label>
                                        </Grid>
                                    </Grid>

                                </form>

                                <br/>

                                <Button type="submit" fullWidth variant="contained" color="primary">
                                    {this.props.isCreate ? 'СЪЗДАЙ НОВИНА' : 'ПРОМЕНИ НОВИНА'}
                                </Button>

                            </Container>
                        </Form>
                    )}
                </Formik>
            </div>
        )
    }
}

export default AbstractCreateEdit;