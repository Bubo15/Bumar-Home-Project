import axios from "axios";

const productService = {

    createCategoryProduct: (formData, token, categoryName) => {
        return axios.post(`http://localhost:8000/product/category/${categoryName}/create`, formData,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },

    deleteCategoryProduct: (id, token) => {
        return axios.delete(`http://localhost:8000/product/category/delete/${id}`,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            }
        )
    },

    deleteSubcategoryProduct: (id, token) => {
        return axios.delete(`http://localhost:8000/product/subcategory/delete/${id}`,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            }
        )
    },

    createSubcategoryProduct: (formData, token, categoryName, subcategoryName) => {
        return axios.post(`http://localhost:8000/product/category/${categoryName}/subcategory/${subcategoryName}/create`, formData,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },

    getDetails: (id, token) => {
        return axios.get(`http://localhost:8000/product/details/${id}`,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },

    editProduct: (formData, token, id) => {
        return axios.post(`http://localhost:8000/product/edit/${id}`, formData,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },

    deleteDescriptionByKey: (key, productId, token) => {
        return axios.delete(`http://localhost:8000/product/${productId}/delete/description/${key}`,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },
    getNewProduct: axios.get("http://localhost:8000/product/new")
};


export default productService;