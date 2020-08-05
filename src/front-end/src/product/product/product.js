import React from "react";
import './product.css'
import {Link} from "react-router-dom";
import AddToCart from "../productAddRemoveCart/addToCart";

const Product = (props) => {

    return (
        <div className="product">
            <Link to={`/product/details/${props.id}`}>
            <img className="product-main-img" src={props.img} alt="name"/>
            </Link>
            <h1>{props.name}</h1>
            <p className="price">{props.price} лв.</p>
            <p>
                <AddToCart id={props.id} name={props.name} countOfProduct={props.countOfProduct} img={props.img} price={props.price}>ДОБАВИ В КОЛИЧКАТА</AddToCart>
            </p>
        </div>
    )
};

export default Product;