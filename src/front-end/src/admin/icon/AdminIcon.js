import React from "react";
import SupervisorAccountIcon from '@material-ui/icons/SupervisorAccount';
import './adminIcon.css'
import {Link} from "react-router-dom";

const AdminIcon = () => {

    return (
        <div>
            <Link to="/admin">
                <SupervisorAccountIcon className="admin-header-icon"/>
            </Link>
            <h1 className="admin-title-icon">АДМИН</h1>
        </div>
    )
};

export default AdminIcon;