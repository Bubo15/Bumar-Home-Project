import axios from "axios";

const businessService = {

    getAllBusiness: axios.get("http://localhost:8000/business/all"),

    createBusiness: (formData, token) => {
        return axios.post(`http://localhost:8000/business/create`, formData,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },

    getAllProductByBusiness: (url, token) => {
        return axios.get(url,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },

    deleteBusiness: (url, token) => {
        return axios.delete(url,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },

    editBusiness: (formData, token, id) => {
        return axios.put(`http://localhost:8000/business/edit/${id}`, formData,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },

    getAllBusinessName: axios.get('http://localhost:8000/business/all/name')
};

export default businessService