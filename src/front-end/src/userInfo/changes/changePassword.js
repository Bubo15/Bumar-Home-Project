import React, {Component} from "react";
import Grid from "@material-ui/core/Grid";
import TextField from "@material-ui/core/TextField/TextField";
import Container from "@material-ui/core/Container";
import Button from "@material-ui/core/Button";
import {Alert, AlertTitle} from "@material-ui/lab";
import * as Yup from "yup";
import {Form, Formik} from "formik";
import userInfoService from "../../service/userInfo";


const validation = Yup.object().shape({
    oldPassword: Yup.string()
        .required('Old Password is required')
        .min(6, "Minimum 6 symbols"),

    newPassword: Yup.string()
        .required('News Password is required')
        .min(6, "Minimum 6 symbols"),
});

class ChangePassword extends Component {

    constructor(props, context) {
        super(props, context);

        this.state = {
            hasError: ""
        }
    }

    changePassword = (values) => {
        userInfoService.changePassword(values, this.props.token)
            .then(data => {
                document.location.pathname = '/user/info'
            })
            .catch((err) => {
                this.setState({hasError: err.response.data})
            });
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

            <Formik
                initialValues={{oldPassword: "", newPassword: ""}}
                onSubmit={this.changePassword}
                validationSchema={validation}>

                {({errors, touched, oldPassword, newPassword, handleChange}) => (

                    <Form>
                        <Container component="main" maxWidth="xs">
                            <form noValidate>
                                <Grid container spacing={2}>
                                    <Grid item xs={12}>
                                        <TextField
                                            id="oldPassword"
                                            name="oldPassword"
                                            helperText={touched.oldPassword ? errors.oldPassword : ""}
                                            error={touched.oldPassword && Boolean(errors.oldPassword)}
                                            label="Old Password"
                                            fullWidth
                                            value={oldPassword}
                                            type="password"
                                            onChange={handleChange(oldPassword || "")}
                                        />
                                    </Grid>
                                    <Grid item xs={12}>
                                        <TextField
                                            id="newPassword"
                                            name="newPassword"
                                            helperText={touched.newPassword ? errors.newPassword : ""}
                                            error={touched.newPassword && Boolean(errors.newPassword)}
                                            label="New Password"
                                            fullWidth
                                            value={newPassword}
                                            type="password"
                                            onChange={handleChange(newPassword || "")}
                                        />
                                    </Grid>
                                </Grid>
                            </form>

                            <br/>

                            <Button type="submit" fullWidth variant="contained"
                                    color="primary">
                                ПРОМЕНИ ПАРОЛА
                            </Button>

                            {this.state.hasError !== "" ? this.showError() : null}

                        </Container>
                    </Form>
                )}

            </Formik>
        )
    }
}

export default ChangePassword;