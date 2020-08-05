import img from "./noProductFound.jpg";
import React from "react";
import './noProductFound.css'


const NoProductFound = () => {

    return(
        <div>
            <img src={img} className="img-no-product-found"/>
            <p1 className="have-not-categoryProduct-title">В момента не предлагаме продукти от тази
                категория
            </p1>
        </div>
    )
};

export default NoProductFound;