/* eslint no-restricted-globals:0 */
import React, {useEffect, useState} from "react";
import Header from "../../header/header";
import Footer from "../../footer/footer";
import './productDetails.css'
import EditIcon from '@material-ui/icons/Edit';
import EditProduct from "../editProduct/editProduct";
import AddToCart from "../productAddRemoveCart/addToCart";
import Delete from "@material-ui/icons/Delete";
import productService from "../../service/product";

const ProductDetails = (props) => {

    const id = document.location.pathname.substr(17);
    const [product, setProduct] = useState({});
    const [picture, setPicture] = useState('');
    const [businessName, setBusinessName] = useState('');
    const [businessLogo, setBusinessLogo] = useState('');
    const [showEditForm, setShowEditForm] = useState(true);

    const hasRoleAdmin = props.roles.includes("ROLE_ADMIN");
    const hasRoleModerator = props.roles.includes("ROLE_MODERATOR");

    const fetchDetails = async () => {
        await productService.getDetails(id, props.token).then(data => {
            setProduct(data.data);
            setPicture(data.data.picture.url);
            setBusinessName(data.data.business.name);
            setBusinessLogo(data.data.business.logo.url);
        }).catch(e => {
            history.back();
        })
    };

    useEffect(() => {
        fetchDetails()
    }, []);

    const deleteDescriptionByKey = (key, productId) => {
        product.deleteDescriptionByKey(key, productId, props.token).then(data => document.location.pathname = "/product/details/" + id);
    };

    const renderDescription = () => {
        let listOfDescription = [];
        for (let key in product.description) {
            if (product.description.hasOwnProperty(key)) {
                listOfDescription = [...listOfDescription,
                    <div>
                        <p1>{key} : {product.description[key]} </p1>
                        {(hasRoleAdmin || hasRoleModerator) && <Delete onClick={() => deleteDescriptionByKey(key, id)}/>}
                        <br/>
                    </div>
                ]
            }
        }
        return listOfDescription;
    };

    const showEdit = () => {
        setShowEditForm(!showEditForm);
    };

    return (
        <div>
            <Header/>

            <img className={showEditForm ? "img-details-product" : "img-details-product-1"} src={picture}/>

            <p1 className="product-details-name">{product.name} {(hasRoleAdmin || hasRoleModerator) && <EditIcon onClick={() => showEdit()}/>}</p1>
            <p1 className="product-details-price">{product.price} ЛВ</p1>
            <p1 className="product-details-mainDescription">{product.mainDescription}</p1>
            <p1 className="product-details-countOfProduct">НАЛИЧНОСТ: {product.countOfProduct}</p1>

            <img className="product-details-businessLogo" src={businessLogo}/>
            <p1 className="product-details-businessName">ПРОИЗВОДИТЕЛ: {businessName}</p1>

            <div className="description-product-details">
                {renderDescription().map(l => {
                    return l
                })}
            </div>

            <AddToCart type="submit" id={id} isDetails={true} name={product.name}
                       countOfProduct={product.countOfProduct} img={picture} price={product.price}/>

            <div hidden={showEditForm}>
                <EditProduct id={id} token={props.token}/>
            </div>

            <div className="footer-product-details">
                <Footer/>
            </div>
        </div>
    )
};

export default ProductDetails;