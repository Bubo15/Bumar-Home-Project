import axios from "axios";

const serviceService = {
    createService: (formData, token) => {
        return  axios.post(`http://localhost:8000/service/create`, formData,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },

    getAllServices: axios.get("http://localhost:8000/service/all"),

    deleteService: (id, token) => {
        return axios.delete(`http://localhost:8000/service/delete/${id}`,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },

    editService: (formData, token, name) => {
        return  axios.post(`http://localhost:8000/service/edit/${name}`, formData,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },

    getServiceByName: (name) => {
       return axios.get(`http://localhost:8000/service/${name}`)
    }

};

export default serviceService;