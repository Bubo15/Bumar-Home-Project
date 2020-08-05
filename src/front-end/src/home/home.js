import React, {Component} from "react";
import "./index.css"
import Header from "../header/header"
import Register from "../register/formReg";
import Login from "../login/login";
import Footer from "../footer/footer";
import Slider from "./slider";
import img from "./img/garden.jpg"
import img2 from "./img/amazon-rivet-furniture-1533048038.jpg"
import img3 from "./img/jasper-ii-leather-lifestyle-1800x880px.jpg"
import img4 from "./img/hmgoepprod.jpg"
import NewsProducts from "../newsProducts/newsProducts";

const images = [img, img3, img4, img2];

class Home extends Component {

    constructor(props, context) {
        super(props, context);

        this.state = {
            isHiddenRegister: true,
            isHiddenLogin: true,
        }
    }

    isClickRegister = (isClick) => {
        this.setState({
            isHiddenRegister: isClick,
            isHiddenLogin: true
        })
    };

    isClickLogin = (isClick) => {
        this.setState({
            isHiddenLogin: isClick,
            isHiddenRegister: !isClick,
        })
    };


    render() {
        return (
            <div className="home">

                <div className="header-home">
                    <Header func={this.isClickRegister} token={this.props.token}/>
                </div>

                <div>

                    <Slider slides={images}/>

                    <div hidden={this.state.isHiddenRegister}>
                        <Register func={this.isClickLogin}/>
                    </div>

                    <div hidden={this.state.isHiddenLogin}>
                        <Login redirectTo={"/"} isLogin={this.props.isLogin} func={this.isClickLogin}/>
                    </div>

                    <NewsProducts/>

                </div>

                <div className="home-footer">
                    <Footer/>
                </div>
            </div>
        )
    }
}

export default Home;