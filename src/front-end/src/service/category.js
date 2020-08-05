import axios from "axios";

const categoryService = {

    getAllCategories: axios.get("http://localhost:8000/category/all"),

    addCategory: (values, token) => {
        return axios.post("http://localhost:8000/category/create", values,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },

    delete: (name, token) => {
        return axios.delete(`http://localhost:8000/category/delete/${name}`,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },


    editCategory: (name, values, token) => {
        return axios.post(`http://localhost:8000/category/edit/${name}`, values,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },

    getAllProductByCategory: (url, token) => {
        return axios.get(url,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    }
};

export default categoryService