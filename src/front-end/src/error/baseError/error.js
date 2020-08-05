import React from "react";
import './err.css'
import Button from "@material-ui/core/Button";
import {Link} from "react-router-dom";

const Error = () => {

    return (
        <body className={"err-class"}>
        <Link to={"/"}>
            <Button className={"btn-err"} type="submit" fullWidth variant="contained" color="primary">
                BACK TO HOME
            </Button>
        </Link>
        </body>
    )
};

export default Error;