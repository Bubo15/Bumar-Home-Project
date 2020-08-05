import React, {Component} from "react";
import {Form, Formik} from "formik";
import Container from "@material-ui/core/Container";
import Grid from "@material-ui/core/Grid";
import TextField from "@material-ui/core/TextField/TextField";
import Button from "@material-ui/core/Button";
import * as Yup from "yup";
import {Alert, AlertTitle} from "@material-ui/lab";
import userInfoService from "../../service/userInfo";

const validation = Yup.object().shape({
    newUsername: Yup.string()
        .required("The field is required")
        .min(2, "Username must be least 2 symbols")
        .max(8, "Username must be at most 8 symbols")
});

class ChangeUsername extends Component {

    constructor(props) {
        super(props);

        this.state = {
            hasError: ""
        }
    }

    changeUsername = (values) => {
        userInfoService.changeUsername(values, this.props.token)
            .then(data => {
                localStorage.setItem("auth", data.headers.authorization);
                document.location.pathname = '/user/info'
            })
            .catch((err) => {
                this.setState({hasError: err.response.data})
            })
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
                initialValues={{newUsername: ""}}
                onSubmit={this.changeUsername}
                validationSchema={validation}>

                {({errors, touched, newUsername, handleChange}) => (

                    <Form>
                        <Container component="main" maxWidth="xs">
                            <form noValidate>
                                <Grid container spacing={2}>
                                    <Grid item xs={12}>
                                        <TextField
                                            id="newUsername"
                                            name="newUsername"
                                            helperText={touched.newUsername ? errors.newUsername : ""}
                                            error={touched.newUsername && Boolean(errors.newUsername)}
                                            label="New Username"
                                            fullWidth
                                            value={newUsername}
                                            onChange={handleChange(newUsername || "")}
                                        />
                                    </Grid>
                                </Grid>
                            </form>

                            <br/>

                            <Button type="submit" fullWidth variant="contained"
                                    color="primary">
                                ПРОМЕНИ USERNAME
                            </Button>

                            {this.state.hasError !== "" ? this.showError() : null}


                        </Container>
                    </Form>
                )}

            </Formik>
        )
    }
}

export default ChangeUsername;