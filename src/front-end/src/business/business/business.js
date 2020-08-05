import {Link} from "react-router-dom";
import React from "react";
import './business.css'

const Business = props => {
        return (
            <div>
                <Link to={props.link ? '/business/' + props.link : '/api/business'}>
                    <div>
                        <img className="img-business" src={props.img} alt="img-business"/>
                    </div>
                </Link>
                <h1>{props.name}</h1>
            </div>
        )
};

export default Business;