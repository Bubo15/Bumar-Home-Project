/* eslint-disable no-undef */
import React from "react";
import AbstractCreateEdit from "../abstractCreateEdit";
// import {Form, Formik} from "formik";
// import Container from "@material-ui/core/Container";
// import Grid from "@material-ui/core/Grid";
// import TextField from "@material-ui/core/TextField/TextField";
// import Button from "@material-ui/core/Button";
// import newsService from "../service/news";
// import * as Yup from "yup";


const EditNews = props => {

    return(
        <div>
            <AbstractCreateEdit isCreate={false} token={props.token} id={props.id}/>
        </div>
    )


    // constructor(props) {
    //     super(props);
    //
    //     this.state = {
    //         pictureFile: null
    //     }
    // }
    //
    // postNews = (values) => {
    //     let formData = new FormData();
    //     const newsBlob = new Blob([JSON.stringify(values)], {type: 'application/json'});
    //     formData.append('pictureFile', this.state.pictureFile);
    //     formData.append('newsEditBindingModel', newsBlob);
    //
    //     newsService.editNews(formData, this.props.token, this.props.id).then(data => {
    //         document.location.pathname = '/api/news'
    //     })
    // };
    //
    // uploadFile = (files) => {
    //     this.setState({pictureFile: files[0]});
    // };

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
    //
    // render() {
    //     debugger
    //     return (
    //         <div className="main-edit-news">
    //             <Formik
    //                 initialValues={{title: "", text: ""}}
    //                 onSubmit={this.postNews}
    //                 validationSchema={validation}>
    //
    //                 {({errors, touched, title, text, handleChange}) => (
    //
    //                     <Form>
    //                         <Container component="main" maxWidth="xs">
    //                             <form noValidate>
    //
    //                                 <Grid container spacing={2}>
    //                                     <Grid item xs={12}>
    //                                         <TextField
    //                                             id="title"
    //                                             name="title"
    //                                             helperText={touched.title ? errors.title : ""}
    //                                             error={touched.title && Boolean(errors.title)}
    //                                             label="Title"
    //                                             fullWidth
    //                                             value={title}
    //                                             onChange={handleChange(title || "")}
    //                                         />
    //                                     </Grid>
    //                                 </Grid>
    //
    //                                 <Grid container spacing={2}>
    //                                     <Grid item xs={12}>
    //                                         <TextField
    //                                             id="text"
    //                                             name="text"
    //                                             helperText={touched.text ? errors.text : ""}
    //                                             error={touched.text && Boolean(errors.text)}
    //                                             label="Text"
    //                                             fullWidth
    //                                             value={text}
    //                                             onChange={handleChange(text || "")}
    //                                         />
    //                                     </Grid>
    //                                 </Grid>

                                    {/*<Grid container spacing={2}>*/}
                                    {/*    <Grid item xs={12}>*/}
                                    {/*        <div onClick={() => this.myWidget().open()} id="upload_widget"*/}
                                    {/*             className="cloudinary-button">Upload files*/}
                                    {/*        </div>*/}
                                    {/*    </Grid>*/}
                                    {/*</Grid>*/}

    {/*                                <Grid container spacing={2}>*/}
    {/*                                    <Grid item xs={12}>*/}
    {/*                                        <input onChange={(e) => this.uploadFile(e.target.files)} type="file" name="file"*/}
    {/*                                               id="file"*/}
    {/*                                               className="input-file"/>*/}
    {/*                                        <label htmlFor="file">Choose a file</label>*/}
    {/*                                    </Grid>*/}
    {/*                                </Grid>*/}

    {/*                            </form>*/}

    {/*                            <br/>*/}

    {/*                            <Button type="submit" fullWidth variant="contained" color="primary">*/}
    {/*                                ПРОМЕНИ НОВИНА*/}
    {/*                            </Button>*/}

    {/*                        </Container>*/}
    {/*                    </Form>*/}
    {/*                )}*/}
    {/*            </Formik>*/}
    {/*        </div>*/}
    {/*    )*/}
    {/*}*/}
};

export default EditNews;