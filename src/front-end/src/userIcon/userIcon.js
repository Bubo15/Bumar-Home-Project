import React, {Component} from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faUser} from "@fortawesome/free-solid-svg-icons";
import {Link} from "react-router-dom";
import jwt_decode from 'jwt-decode';

class Icon extends Component {

    constructor(props) {
        super(props);

        this.state = {
            isTouched: true,
        };
    }


    handleClick = () => {
        if (this.props.func === undefined){document.location = "/"}
        this.props.func(!this.state.isTouched);
        this.setState(prevState => ({isTouched: !prevState.isTouched}));
    };

    getName = () => {
        let token = this.props.auth;
        let decoded = jwt_decode(token);
        let name = decoded["sub"];
        return name.toUpperCase();
    };


    render() {
        if (this.props.auth === "null") {
            return (
                <div>
                    <div onClick={this.handleClick}>
                        <div className="icon-user">
                            <FontAwesomeIcon icon={faUser}/>
                        </div>
                        <h1 className="user-title-icon">НАЧАЛО НА СЕСИЯТА</h1>
                    </div>
                </div>
            )
        } else {
            return (
                <div>
                    <Link to={"/user/info"}>
                            <div className="icon-user">
                                <FontAwesomeIcon icon={faUser}/>
                            </div>
                        <h1 className="user-title-icon">{this.getName()}</h1>
                    </Link>
                </div>
            )
        }
    }
}

export default Icon;