import axios from "axios";

const newsService = {

    createNews: (formData, token) => {
       return  axios.post(`http://localhost:8000/news/create`, formData,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },

    getAllNews: axios.get("http://localhost:8000/news/all"),

    deleteNews: (id, token) => {
       return axios.delete(`http://localhost:8000/news/delete/${id}`,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },

    editNews: (formData, token, id) => {
        return  axios.post(`http://localhost:8000/news/edit/${id}`, formData,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    }
};

export default newsService