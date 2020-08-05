import React, {Component} from "react";
import './userInfo.css'
import Header from "../header/header";
import Footer from "../footer/footer";
import jwt_decode from "jwt-decode";
import Button from "@material-ui/core/Button";
import ChangePassword from "./changes/changePassword";
import ChangeUsername from "./changes/changeUsername";
import ChangeEmail from "./changes/changeEmail";
import CreateIcon from '@material-ui/icons/Create';
import userInfoService from "../service/userInfo";
import userService from "../service/auth";
import img from './personal-data.png'
import {Link} from "react-router-dom";

class UserInfo extends Component {

    constructor(props, context) {
        super(props, context);

        this.state = {
            showEmail: true,
            showPassword: true,
            showUsername: true,
            user: {
                username: "",
                email: ""
            }
        }
    }

    componentDidMount() {
        userInfoService.getInfo(this.props.token).then(data => {
            this.setState({user: data.data});
        });
    }

    getName = token => {
        let decoded = jwt_decode(token);
        return decoded["sub"];
    };

    logout = () => {
        userService.logout();
    };

    clickPassword = () => {
        this.setState({
            showPassword: !this.state.showPassword,
            showUsername: true,
            showEmail: true
        })
    };

    clickUsername = () => {
        this.setState({
            showUsername: !this.state.showUsername,
            showPassword: true,
            showEmail: true
        })
    };

    clickEmail = () => {
        this.setState({
            showEmail: !this.state.showEmail,
            showUsername: true,
            showPassword: true
        })
    };

    render() {
        return (
            <div>

                <div className="header-user-info">
                    <Header/>
                </div>

                <h1 className="title-info-user">
                    ТВОИТЕ ДАННИ
                </h1>

                <img style={{
                    '-webkit-filter': 'blur(3px)',
                    'position': 'absolute',
                    'height': '760px',
                    'width': '1400px',
                    'left': '230px',
                    'top': '150px'
                }} src={img}/>

                <div className="info-username">
                    <h2 style={{'color': 'white'}}>Username: {this.state.user.username}</h2>
                    <Button onClick={this.clickUsername} type="submit" fullWidth variant="contained" color="primary">
                        <CreateIcon/> промени
                    </Button>
                </div>

                <div className="info-email">
                    <h2 style={{'color': 'white'}}>Email: {this.state.user.email}</h2>
                    <Button onClick={this.clickEmail} type="submit" fullWidth variant="contained" color="primary">
                        <CreateIcon/> промени
                    </Button>
                </div>

                <div className="info-password">
                    <h2 style={{'color': 'white'}}>Password: ********</h2>
                    <Button onClick={this.clickPassword} type="submit" fullWidth variant="contained" color="primary">
                        <CreateIcon/> промени
                    </Button>
                </div>

                <Link to="/user/orders">
                    <Button className="btn-all-orders-user-info" type="submit" variant="contained" color="primary">
                        поръчки
                    </Button>
                </Link>

                <div hidden={this.state.showPassword} className="change-form">
                    <ChangePassword token={this.props.token}/>
                </div>

                <div hidden={this.state.showUsername} className="change-form">
                    <ChangeUsername token={this.props.token}/>
                </div>

                <div hidden={this.state.showEmail} className="change-form">
                    <ChangeEmail token={this.props.token}/>
                </div>

                <Button onClick={this.logout} className="exit-btn" type="submit" fullWidth variant="contained"
                        color="primary">
                    ИЗХОД
                </Button>

                <div className="user-info-footer">
                    <Footer/>
                </div>
            </div>
        )
    }
}

export default UserInfo;