import React from "react";
import img from '../../error/emptyCart/emptycart.png'
import './cart.css'
import Header from "../../header/header";
import Footer from "../../footer/footer";
import AddToCart from "../../product/productAddRemoveCart/addToCart";
import RemoveFromCart from "../../product/productAddRemoveCart/removeFromCart";
import Button from "@material-ui/core/Button";
import ClearIcon from '@material-ui/icons/Clear';
import EmptyCart from "../../error/emptyCart/emptyCart";
import SummeryOfOrder from "../summeryOfCart/summeryOfOrder";
import {Link} from "react-router-dom";

const Cart = (props) => {

    const isHaveProducts = localStorage.getItem("productsToCart");
    const showButtonAndSummary = JSON.stringify(isHaveProducts) !== JSON.stringify("");
    const isAuthenticated = props.token;

    const redirectTo = (path) => {
        document.location.pathname = path;
    };

    const emptyTheCart = () => {
        localStorage.setItem("productsToCart", []);
        redirectTo("/shoppingCart");
    };

    const removeProductFromCart = (id) => {
        let products = JSON.parse(localStorage.getItem("productsToCart"));
        let newProducts = products.filter(product => Number(product.id) !== Number(id));
        localStorage.setItem("productsToCart", JSON.stringify(newProducts));
        redirectTo("/shoppingCart")
    };

    const getSumOfAllProduct = () => {
        let totalSum = 0;

        JSON.parse(isHaveProducts).map(product => {
            totalSum += product.productCount * product.price;
        });

        return totalSum;
    };

    const getAllProduct = () => {
        let listOfProducts = [];

        JSON.parse(isHaveProducts).map(product => {
            listOfProducts = [...listOfProducts,
                <div>
                    <Link to={`/product/details/${product.id}`}>
                    <img style={{'max-width': '70px'}} src={product.img}/>
                    </Link>
                    <a className="cart-product-name">{product.name}</a>
                    <ClearIcon onClick={() => removeProductFromCart(product.id)} className="clear-item-from-cart"/>
                    <hr className="hr-cart"/>
                    <div className="remove-form-cart">
                        <RemoveFromCart
                            id={product.id}
                            name={product.name}
                            img={product.img} price={product.price}
                            productCount={product.productCount}
                            countOfProduct={product.countOfProduct}
                            isCart={true}/>
                    </div>
                    <a className="cart-product-count">{product.productCount}</a>
                    <div className="add-form-cart">
                        <AddToCart
                            id={product.id}
                            name={product.name}
                            img={product.img} price={product.price}
                            productCount={product.productCount}
                            countOfProduct={product.countOfProduct}
                            isCart={true}/>
                    </div>
                    <p1 className="cart-product-price">{product.productCount * product.price} лв</p1>
                    <br/>
                </div>
            ]
        });

        return listOfProducts;
    };

    return (
        <div>
            <Header/>

            {
                isHaveProducts ?
                    <div className="cart-products">
                        {getAllProduct().map(l => {
                            return l
                        })}
                    </div>
                    :
                    <EmptyCart/>
            }

            {
                showButtonAndSummary &&
                <div>
                    <div>
                        <Button onClick={() => emptyTheCart()}
                                style={{'border': '2px solid black', 'top': '750px', 'left': '150px'}}>
                            ИЗПРАЗНИ КОЛИЧКАТА
                        </Button>
                    </div>

                    <SummeryOfOrder isCart={true} countOfItems={getAllProduct().length} sumOfAllProduct={getSumOfAllProduct()} isAuthenticated={isAuthenticated}/>
                </div>
            }

            <div className="footer-cart">
                <Footer/>
            </div>
        </div>
    )
};

export default Cart