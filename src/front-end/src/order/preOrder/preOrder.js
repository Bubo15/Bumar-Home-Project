import Login from "../../login/login";
import React, {useState} from "react";
import './preOrder.css'
import Header from "../../header/header";
import Footer from "../../footer/footer";
import Register from "../../register/formReg";
import Button from "@material-ui/core/Button";

const PreOrder = () => {

    const [showRegister, setShowRegister] = useState(true);

    const showRegisterFunc = () => {
        setShowRegister(!showRegister)
    };

    const redirectTo = (path) => {
        document.location.pathname = path;
    };

    return (
        <div>
            <Header/>

            <div className="preOrder-login">
                <Login redirectTo={"/order"}/>
            </div>

            <h4 className="five-lv-sale-discount">При логване, получавате 5 лв отстъпка</h4>


            <div className="news-users-preOrder">
                <h2>НОВИ ПОТРЕБИТЕЛИ</h2>
                <Button onClick={() => redirectTo("/order")} style={{'border': '2px solid black', 'width': '460px'}}>ПАЗАРУВАЙТЕ КАТО ГОСТ</Button>
                <br/>
                <br/>
                <Button onClick={() => showRegisterFunc()} style={{'border': '2px solid black', 'width': '460px'}}>СЪЗДАЙ НОВ ПРОФИЛ</Button>
            </div>


            <div hidden={showRegister} className="preOrder-register">
                <Register redirectTo={"/preOrder"}/>
            </div>

            <Footer/>
        </div>
    )
};

export default PreOrder