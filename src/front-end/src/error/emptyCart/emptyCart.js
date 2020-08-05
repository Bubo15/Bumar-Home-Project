import img from "./emptycart.png";
import React from "react";
import './emptyCart.css'

const EmptyCart = () => {

    return(
        <div>
            <img className="img-empty-cart" src={img} alt="empty-stroller"/>
            <p1 className="empty-text">Изглежда твоята количка е празна. Купете продукт, направете една
                количка
                щастлива.
            </p1>
        </div>
    )
};

export default EmptyCart;