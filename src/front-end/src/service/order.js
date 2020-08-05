import axios from "axios";

const orderService = {

    createOrder: (formData) => {
        return axios.post('http://localhost:8000/order/create', formData,
            {
                headers: {
                    'Content-Type': 'application/json',
                }
            })
    },
    getAllOrders: (token) => {
        return axios.get('http://localhost:8000/order/all',
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },

    deleteOrder: (id, token) => {
        return axios.delete(`http://localhost:8000/order/${id}/delete`,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    }
};

export default orderService;