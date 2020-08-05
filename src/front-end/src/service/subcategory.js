import axios from "axios";

const subcategoryService = {

    getAllCategoriesName: axios.get("http://localhost:8000/subcategory/all/category/name"),

    addSubcategory: (values, token) => {
        return axios.post("http://localhost:8000/subcategory/create", values,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },

    deleteSubcategory: (name, token) => {
        return axios.delete(`http://localhost:8000/subcategory/delete/${name}`,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },

    editSubcategory: (category,subcategory ,values, token) => {
        return axios.post(`http://localhost:8000/subcategory/edit/${category}/${subcategory}`, values,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },

    getAllProductBySubcategory(categoryName, subcategoryName, token) {
        return axios.get(`/subcategory/${subcategoryName}/category/${categoryName}/products`,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    }
};

export default subcategoryService