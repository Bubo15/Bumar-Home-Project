import React, {useEffect, useState} from "react";
import Header from "../../header/header";
import Footer from "../../footer/footer";
import Product from "../product/product";
import Grid from "@material-ui/core/Grid";
import categoryService from "../../service/category";
import './categoryProduct.css'
import img1 from '../img/categoryNotFound.png'
import AddCircleOutlineIcon from '@material-ui/icons/AddCircleOutline';
import Paper from "@material-ui/core/Paper";
import AddProduct from '../createProduct/addProduct'
import DeleteIcon from '@material-ui/icons/Delete';
import product from "../../service/product";
import NoProductFound from "../../error/noProductFound/noProductFound";

const CategoryProductList = (props) => {

    const [products, setProducts] = useState([]);
    const [showAddForm, setShowAddForm] = useState(true);

    const hasRoleAdmin = props.roles.includes("ROLE_ADMIN");
    const hasRoleModerator = props.roles.includes("ROLE_MODERATOR");

    async function asyncCall() {
        const nameCategory = document.location.pathname.substr(10);
        const firstChar = nameCategory.substring(0, 1).toUpperCase();

        await categoryService.getAllCategories
            .then(data => {
                const category = data.data.filter(c => c.name === firstChar + nameCategory.substring(1, nameCategory.length));

                JSON.stringify(category) !== JSON.stringify([]) ?
                    category[0]["links"].length !== 0 ? categoryService.getAllProductByCategory(category[0]["links"][0].href, props.token)
                        .then(data => {
                            setProducts(data.data)
                        }) : setProducts([]) : setProducts(null)
            });
    }

    useEffect(() => {
        asyncCall()
    }, []);

    const showAdd = () => {
        setShowAddForm(!showAddForm);
    };

    const deleteProductCategory = (id) => {
        const categoryName = document.location.pathname.substr(10);
        product.deleteCategoryProduct(id, props.token).then(data => document.location.pathname = "/category/" + categoryName);
    };

    return (
        <div>
            <Header/>

            {
                products === null
                    ?
                    <div>
                        <img src={img1} className="img-no-category-found"/>
                    </div>
                    :
                    products.length !== 0
                        ?
                        <div>
                            <Grid container spacing={24}>
                                {products.map(p => {
                                    return (
                                        <Grid item md={3}>
                                            {hasRoleAdmin &&
                                            <DeleteIcon onClick={() => deleteProductCategory(p.id)} className="delete-product-category-icon"/>}
                                            <Product id={p.id} name={p.name} price={p.price} mainDescription={p.mainDescription}
                                                     img={p.picture.url} countOfProduct={p.countOfProduct}/>
                                        </Grid>
                                    );
                                })}
                            </Grid>
                        </div>
                        :
                       <NoProductFound/>
            }

            {(hasRoleAdmin || hasRoleModerator) &&
            products !== null &&
            <div>
                <br/>
                <br/>
                <Paper onClick={showAdd} className={products.length !== 0 ? "add-category-product" : "add-category-product-1"}>
                    <div>
                        <p1 className="add-category-product-title">Add Product</p1>
                        <AddCircleOutlineIcon className="add-category-product-icon"/>
                    </div>
                </Paper>
            </div>
            }

            <div hidden={showAddForm}>
                <AddProduct isCategory={true} token={props.token}/>
            </div>

            <div className={hasRoleAdmin ? products !== null ? "categoryProduct-footer" : "categoryProduct-footer-1" : "categoryProduct-footer-2"}>
                <Footer/>
            </div>

        </div>
    )
};


export default CategoryProductList;