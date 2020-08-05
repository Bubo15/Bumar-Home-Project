import React, {useState} from "react";
import "./header.css"
import SiteName from "../siteName/siteName";
import Icon from "../userIcon/userIcon";
import {AuthContext} from "../context/context"
import HomeIcon from '@material-ui/icons/Home';
import {Link} from "react-router-dom";
import ShopCartIcon from "../cart/shopCartTitle/shopingCartTitle";
import AdminIcon from "../admin/icon/AdminIcon";

const Header = (props) => {

     const hasRoleAdmin = localStorage.getItem("roles").includes("ROLE_ADMIN");

    return (
        <AuthContext.Consumer>
            {value => (
                <header className="header">
                    <div>
                        <SiteName token={props.token}/>
                        <Icon func={props.func} auth={value.auth}/>
                        <ShopCartIcon auth={value.auth}/>
                        {hasRoleAdmin && <AdminIcon auth={value.auth}/>}

                        {document.location.pathname !== "/" && <Link to="/">
                            <HomeIcon className="home-icon-siteName"/>
                        </Link>}
                    </div>
                </header>
            )}
        </AuthContext.Consumer>
    )
};


export default Header;