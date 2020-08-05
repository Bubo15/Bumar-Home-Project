import React from "react";
import CreateEditComment from "../commentCreateEdit";

const CreateComment = (props) => {

    return(
        <div>
            <CreateEditComment token={props.token} isCreate={true}/>
        </div>
    )
};

export default CreateComment;