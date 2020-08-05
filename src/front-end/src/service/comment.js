import axios from "axios";

const commentService = {

    getAllComments: axios.get("http://localhost:8000/comment/all"),

    deleteComment: (id, token) => {
        return axios.delete(`http://localhost:8000/comment/delete/${id}`,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },

    createComment: (value, token) => {
        return axios.post(`http://localhost:8000/comment/create`, value,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },

    editComment: (id, value, token) => {
        return axios.post(`http://localhost:8000/comment/edit/${id}`, value,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },


};

export default commentService