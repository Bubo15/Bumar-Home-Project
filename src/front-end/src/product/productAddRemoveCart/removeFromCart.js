import React from "react";
import RemoveIcon from '@material-ui/icons/Remove';

const RemoveFromCart = (props) => {

    const removeFromCart = () => {

        let products = [];

        if (localStorage.getItem('productsToCart')) {
            products = JSON.parse(localStorage.getItem('productsToCart'));
        }

        let product = products.filter(product => product.id === props.id);

        if (product.length > 0) {
            let productCount = Number(product[0].productCount);

            if (productCount > 1) {
                productCount -= 1;
                product[0]['productCount'] = productCount;

                localStorage.setItem('productsToCart', JSON.stringify(products));
                document.location.pathname = '/shoppingCart';
            }
        } else {
            products.push({id: props.id, name: props.name, productCount: 1, img: props.img, price: props.price, countOfProduct: props.countOfProduct});
            localStorage.setItem('productsToCart', JSON.stringify(products));
        }
    };

    return (
       <RemoveIcon onClick={() => removeFromCart()}/>
    )
};

export default RemoveFromCart;