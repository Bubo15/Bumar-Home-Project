import React from "react";
import ProductCreateEdit from "../productCreateEdit";


const AddProduct = (props) => {

    return(
        <ProductCreateEdit isCreate={true} token={props.token} isCategory={props.isCategory}/>
    )

};

export default AddProduct