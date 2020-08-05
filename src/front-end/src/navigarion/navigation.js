import React, {Component} from "react";
import Home from "../home/home";
import {Route, Switch, Redirect} from "react-router-dom";
import Info from '../userInfo/userInfo'
import Err from '../error/baseError/error'
import { AuthContext } from "../context/context"
import WrappedContext  from "../context/context"
import NewsList from "../news/newsList/newsList";
import ServiceList from "../serviceComponent/serviceList/serviceList";
import ServiceDetails from "../serviceComponent/serviceDetails/serviceDetails";
import BusinessList from "../business/businessList/businessList";
import BusinessProductList from "../product/businessProduct/businessProducts";
import Cart from "../cart/mainCart/cart";
import CategoryProductList from "../product/categoryProduct/categoryProductList";
import SubcategoryProductList from "../product/subcategoryProduct/subcategoryProductList";
import ProductDetails from "../product/productDetails/productDetails";
import Order from "../order/baseOrder/order";
import PreOrder from "../order/preOrder/preOrder";
import Admin from "../admin/Admin";
import UserOrders from "../userInfo/orders/userOrders";
import CommentList from "../comment/commentList/commentList";

const Navigation = () => {

        return (
            <WrappedContext>
                <AuthContext.Consumer>
                    {value => (
                        <div>
                            <Switch>
                                <Route path="/" exact render={() => <Home token={value.auth}/>}/>
                                <Route path="/admin" exact render={() => (value.auth !== "null" && value.roles.includes("ROLE_ADMIN")) ? <Admin token={value.auth} roles={value.roles}/> : <Redirect to="/"/>}/>
                                <Route path="/api/business" exact render={() => <BusinessList token={value.auth} roles={value.roles}/>}/>
                                <Route path="/business/:id/products" exact render={() => <BusinessProductList token={value.auth} roles={value.roles}/>}/>
                                <Route path="/api/news" exact render={() => <NewsList token={value.auth} roles={value.roles}/>}/>
                                <Route path="/api/service" exact render={() => <ServiceList token={value.auth} roles={value.roles}/>}/>
                                <Route path="/api/comment" exact render={() => <CommentList token={value.auth} roles={value.roles}/>}/>
                                <Route path="/api/service/:name" exact render={() => <ServiceDetails token={value.auth} roles={value.roles}/>}/>
                                <Route path="/user/info" render={() => value.auth !== "null" ? <Info token={value.auth}/> : <Redirect to={"/"}/> }/>
                                <Route path="/user/orders" render={() => value.auth !== "null" ? <UserOrders token={value.auth}/> : <Redirect to={"/"}/> }/>
                                <Route path="/shoppingCart" render={() => <Cart token={value.auth} roles={value.roles}/>}/>
                                <Route path="/category/:name" exact render={() => <CategoryProductList token={value.auth} roles={value.roles}/>}/>
                                <Route path="/category/:name/subcategory/:name" exact render={() => <SubcategoryProductList token={value.auth} roles={value.roles}/>}/>
                                <Route path="/product/details/:id" exact render={() => <ProductDetails token={value.auth} roles={value.roles}/>}/>
                                <Route path="/order" exact render={() => localStorage.getItem("productsToCart").length !== 0 ? <Order token={value.auth} roles={value.roles}/> : <Redirect to="/"/>}/>
                                <Route path="/preOrder" exact render={() => (localStorage.getItem("productsToCart").length !== 0 && value.auth === "null") ? <PreOrder token={value.auth} roles={value.roles}/> : <Redirect to="/"/>}/>
                                <Route component={Err}/>
                            </Switch>
                        </div>
                    )}
                </AuthContext.Consumer>
            </WrappedContext>
        )
};

export default Navigation