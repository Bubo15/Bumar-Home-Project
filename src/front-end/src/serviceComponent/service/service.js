import React  from "react";
import './service.css'
import {Link} from "react-router-dom";

const Service = props => {
    return (
        <div>
            <Link to={`/api/service/${props.name}`}>
                <div>
                    <img className="img-service" src={props.img} alt="img-news"/>
                </div>
            </Link>
            <h1>{props.name}</h1>
        </div>
    )
};

export default Service;