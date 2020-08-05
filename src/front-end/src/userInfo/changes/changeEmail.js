import React, {Component} from "react";
import * as Yup from "yup";
import {Form, Formik} from "formik";
import Container from "@material-ui/core/Container";
import Grid from "@material-ui/core/Grid";
import TextField from "@material-ui/core/TextField/TextField";
import Button from "@material-ui/core/Button";
import userInfoService from "../../service/userInfo";

const validation = Yup.object().shape({
    newEmail: Yup.string()
        .required('Email is required')
        .email("Email must be valid")
});

class ChangeEmail extends Component{

    constructor(props) {
        super(props);

    }

    changeEmail = (values) => {
        userInfoService.changeEmail(values, this.props.token).then(data => {document.location.pathname = '/user/info'})
    };

    render() {
        return (

            <Formik
                initialValues={{newEmail: ""}}
                onSubmit={this.changeEmail}
                validationSchema={validation}>

                {({errors, touched, newEmail,handleChange}) => (

                    <Form >
                        <Container component="main" maxWidth="xs">
                            <form noValidate>
                                <Grid container spacing={2}>
                                    <Grid item xs={12}>
                                        <TextField
                                            id="newEmail"
                                            name="newEmail"
                                            helperText={touched.newEmail ? errors.newEmail : ""}
                                            error={touched.newEmail && Boolean(errors.newEmail)}
                                            label="New Email"
                                            fullWidth
                                            value={newEmail}
                                            onChange={handleChange(newEmail || "")}
                                        />
                                    </Grid>
                                </Grid>
                            </form>

                            <br/>

                            <Button type="submit" fullWidth variant="contained"
                                    color="primary">
                                ПРОМЕНИ EMAIL
                            </Button>

                        </Container>
                    </Form>
                )}

            </Formik>
        )
    }
}

export default ChangeEmail;