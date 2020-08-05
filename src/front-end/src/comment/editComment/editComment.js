import CreateEditComment from "../commentCreateEdit";
import React from "react";

const EditComment = (props) => {

    return(
        <div>
            <CreateEditComment token={props.token} id={props.id}/>
        </div>
    )
};

export default EditComment;