import React from "react";
import AbstractCreateEdit from "../abstractCreateEdit";

const CreateBusiness = (props) => {
    return(
        <div>
            <AbstractCreateEdit token={props.token} isCreated={true}/>
        </div>
    )
};

export default CreateBusiness;