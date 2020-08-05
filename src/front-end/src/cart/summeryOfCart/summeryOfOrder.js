import Button from "@material-ui/core/Button";
import React from "react";
import './summeryOfOrder.css'

const SummeryOfOrder = (props) => {

    const redirectTo = (path) => {
        document.location.pathname = path;
    };

    return(
        <div className="summary-order">
            <h4>ОБОБЩЕНИЕ НА ПОРЪЧКАТА</h4>
            <br/>
            <p3 className="all-items-summery-order">АРТИКУЛИ: ({props.countOfItems})</p3>
            <hr style={{'position': 'relative'}}/>

            <h4 style={{'position': 'absolute', 'left': '60px'}}>ОБЩО: {props.sumOfAllProduct} ЛВ</h4>


            {props.isCart && <Button
                onClick={() => props.isAuthenticated !== "null" ? redirectTo('/order') : redirectTo("/preOrder")}
                style={{'width': '220px', 'background': 'dodgerblue', 'top': '70px', 'left': '13px'}}>
                КУПИ
            </Button>}
        </div>
    )
};

export default SummeryOfOrder;