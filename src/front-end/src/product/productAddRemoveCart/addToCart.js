import React from "react";
import AddIcon from '@material-ui/icons/Add';

const AddToCart = (props) => {

    const successful = () => {
        localStorage.getItem("productsToCart")
            ? alert('Вие добавихте продукта: ' + props.name + " в количката.")
            : alert('Поздравления Вие добавихте първия си продукта: ' + props.name + " в количката.");
    };

    const noProduct = () => {
        alert(`От този продукт: ${props.name}, има в наличност само ${props.countOfProduct}`)
    };

    // [{name: count}] by called this method if exist name, get count++;
    const addToCart = () => {
        let products = [];

        if (localStorage.getItem('productsToCart')) {
            products = JSON.parse(localStorage.getItem('productsToCart'));
        }

        let product = products.filter(product => product.id === props.id);

        if (product.length > 0) {
            let productCount = Number(product[0].productCount);

            if (productCount < props.countOfProduct) {
                productCount += 1;
                product[0]['productCount'] = productCount;

                props.isCart?  document.location.pathname = '/shoppingCart' : successful();
                localStorage.setItem('productsToCart', JSON.stringify(products));
            } else {
                noProduct()
            }

        } else {
            products.push({id: props.id, name: props.name, productCount: 1, img: props.img, price: props.price, countOfProduct: props.countOfProduct});
            successful();
            localStorage.setItem('productsToCart', JSON.stringify(products));
        }
    };

    // if method is called from isCart show +, otherwise button
    return (
        <div>
            {!props.isCart
                ?
                <button onClick={() => addToCart()} type="submit"
                        className={props.isDetails ? "btn-add-to-cart-product-details" : ""}>
                    ДОБАВИ В КОЛИЧКАТА
                </button>
                :
                <AddIcon onClick={() => addToCart()}/>
            }
        </div>
    )
};

export default AddToCart;