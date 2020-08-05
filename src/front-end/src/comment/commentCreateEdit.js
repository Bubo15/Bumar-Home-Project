import * as Yup from "yup";
import {Form, Formik} from "formik";
import Container from "@material-ui/core/Container";
import Grid from "@material-ui/core/Grid";
import TextField from "@material-ui/core/TextField/TextField";
import Button from "@material-ui/core/Button";
import React from "react";
import commentService from "../service/comment";

const validation = Yup.object().shape({
    description: Yup.string()
        .max(250, "Description can be max 250 symbols"),
});
const CreateEditComment = (props) => {

    const redirectTo = (url) => {
        document.location.pathname = url
    };

    const createEditComment = (values) => {
        props.isCreate
            ?
            commentService.createComment(values, props.token).then(data => {redirectTo('/api/comment')})
            :
            commentService.editComment(props.id, values, props.token).then(data => {redirectTo('/api/comment')})

    };

    return (
        <div>
            <Formik
                initialValues={{description: ""}}
                onSubmit={createEditComment}
                validationSchema={validation}>

                {({errors, touched, description, handleChange}) => (

                    <Form>
                        <Container component="main" maxWidth="xs">
                            <form noValidate>

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

                            </form>

                            <br/>

                            <Button type="submit" fullWidth variant="contained" color="primary">
                                {props.isCreate ? 'КОМЕНТИРАЙ' : 'ПРОМЕНИ КОМЕНТАР'}
                            </Button>

                        </Container>
                    </Form>
                )}
            </Formik>
        </div>
    )
};

export default CreateEditComment;