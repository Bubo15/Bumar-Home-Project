import React from "react";
import ShoppingCartIcon from '@material-ui/icons/ShoppingCart';
import {Link} from "react-router-dom";
import './shopingCartTitle.css'

const ShopCartIcon = (props) =>{

    return(
        <Link to={"/shoppingCart"}>
            <div className={props.auth !== "null" ? "shop-icon" : "shop-icon-1"}>
                <ShoppingCartIcon/>
            </div>
            <h1 className={props.auth !== "null" ? "shop-title-icon" : "shop-title-icon-1"}>КОЛИЧКА</h1>
        </Link>
    )
};

export default ShopCartIcon;