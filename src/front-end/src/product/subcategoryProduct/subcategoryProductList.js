import React, {useEffect, useState} from "react";
import product from "../../service/product";
import Header from "../../header/header";
import img from "../../error/noProductFound/noProductFound.jpg";
import img1 from "../img/categoryNotFound.png";
import DeleteIcon from '@material-ui/icons/Delete';
import Grid from "@material-ui/core/Grid";
import Product from "../product/product";
import Paper from "@material-ui/core/Paper/Paper";
import AddProduct from "../createProduct/addProduct";
import Footer from "../../footer/footer";
import AddCircleOutlineIcon from '@material-ui/icons/AddCircleOutline';
import subcategoryService from "../../service/subcategory";
import NoProductFound from "../../error/noProductFound/noProductFound";
import GridList from "@material-ui/core/GridList";
import GridListTile from "@material-ui/core/GridListTile";

const SubcategoryProductList = (props) => {

    const [products, setProducts] = useState([]);
    const [showAddForm, setShowAddForm] = useState(true);

    const hasRoleAdmin = props.roles.includes("ROLE_ADMIN");
    const hasRoleModerator = props.roles.includes("ROLE_MODERATOR");

    const asyncCall = async () => {

        const categoryName = document.location.pathname.substr(10, document.location.pathname.indexOf("/subcategory") - 10);
        const subcategoryName = document.location.pathname.substr(document.location.pathname.indexOf("/subcategory") + 13);

        await subcategoryService.getAllProductBySubcategory(categoryName, subcategoryName, props.token)
            .then(data => {
                setProducts(data.data)
            })
            .catch(e => {
                setProducts(null)
            })
    };

    useEffect(() => {
        asyncCall();
    }, []);

    const showAdd = () => {
        setShowAddForm(!showAddForm);
    };

    const deleteProductSubcategory = (id) => {
        const categoryName = document.location.pathname.substr(10, document.location.pathname.indexOf("/subcategory") - 10);
        const subcategoryName = document.location.pathname.substr(document.location.pathname.indexOf("/subcategory") + 13);

        product.deleteSubcategoryProduct(id, props.token)
            .then(data => document.location.pathname = "/category/" + categoryName + '/subcategory/' + subcategoryName);
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
                                            <DeleteIcon onClick={() => deleteProductSubcategory(p.id)} className="delete-product-category-icon"/>}
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
                <Paper onClick={showAdd}
                       className={products.length !== 0 ? "add-category-product" : "add-category-product-1"}>
                    <div>
                        <p1 className="add-category-product-title">Add Product</p1>
                        <AddCircleOutlineIcon className="add-category-product-icon"/>
                    </div>
                </Paper>
            </div>
            }

            <div hidden={showAddForm}>
                <AddProduct token={props.token}/>
            </div>

            <div className={
                hasRoleAdmin ?
                    products !== null ? "categoryProduct-footer" : "categoryProduct-footer-1" : "categoryProduct-footer-2"
            }>
                <Footer/>
            </div>

        </div>
    )
}


export default SubcategoryProductList;