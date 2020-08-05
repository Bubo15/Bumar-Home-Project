import React from "react";
import AbstractCreateEdit from "../abstractCreateEdit";

const EditBusiness = (props) => {

    return(
        <div>
            <AbstractCreateEdit token={props.token} id={props.id} isCreated={false}/>
        </div>
    )
};

export default EditBusiness;