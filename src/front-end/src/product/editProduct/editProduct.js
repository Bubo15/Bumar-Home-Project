import ProductCreateEdit from "../productCreateEdit";
import React from "react";


const EditProduct = props => {
    return(
        <ProductCreateEdit token={props.token} id={props.id} isCreate={false}/>
    )
};

export default EditProduct;