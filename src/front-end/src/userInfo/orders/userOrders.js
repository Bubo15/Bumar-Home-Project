import React, {useEffect, useState} from "react";
import orderService from "../../service/order";
import Header from "../../header/header";
import Footer from "../../footer/footer";
import Paper from "@material-ui/core/Paper";
import './userOrders.css'
import ClearIcon from '@material-ui/icons/Clear';
import img from '../img/no-order.png'

const UserOrders = (props) => {

    const [orders, setOrders] = useState([]);

    const fetchData = async () => {
        await orderService.getAllOrders(props.token).then(data => {
            setOrders(data.data);
        })
    };

    useEffect(() => {
        fetchData();
    }, []);

    const deleteOrder = (id) => {
        orderService.deleteOrder(id, props.token)
            .then(data => document.location.pathname = "/user/orders")
            .catch(e => {
                alert(e.response.data);
            })
    };

    const showAllOrders = () => {
        let listOfOrders = [];

        orders.map(order => {
            const {id, ordered, products, totalPrice} = order;

            listOfOrders.push(
                <div>
                    <Paper style={{'background-color': 'antiquewhite', '-webkit-box-shadow': '4px 4px 6px 8px #ccc'}}
                           className="orders-user">
                        <h3 className="order-name-column">Поръчка: {id}</h3>
                        <h3 className="order-make-column">Направена на: {ordered}</h3>
                        <h3 className="order-products-count-column">Брой продукти: {products}</h3>
                        <h3 className="order-price-column">Обща цена: {totalPrice} лв</h3>
                        <ClearIcon onClick={() => deleteOrder(id)} className="user-order-delete-btn"/>
                    </Paper>
                    <br/>
                    <br/>
                </div>
            )
        });

        return listOfOrders;
    };

    return (
        <div>
            <Header/>

            <h1 className="user-orders-title">ТВОИТЕ ПОРЪЧКИ</h1>

            {
                orders.length > 0 ? showAllOrders() :
                    <div>
                        <img style={{"position": "absolute", "width": "950px", "left": "450px", "top": "280px"}} src={img}/>
                        <h1 style={{"position": "absolute", "font-size": "80px", "left": "452px", "top": "140px"}}>В момета нямате поръчки</h1>
                    </div>
            }

            <Footer/>
        </div>
    )
};

export default UserOrders;