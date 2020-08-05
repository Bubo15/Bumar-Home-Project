import axios from "axios";

const userInfoService = {
    changeEmail: (values, token) => {
        return axios.post(`http://localhost:8000/user/change/email`, values,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },

    changePassword: (values, token) => {
        return axios.post(`http://localhost:8000/user/change/password`, values,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },

    changeUsername: (values, token) => {
        return axios.post(`http://localhost:8000/user/change/username`, values,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    },

    getInfo: (token) => {
        return axios.get(`http://localhost:8000/user/info`,
            {
                'headers': {'Authorization': token}
            })
    },

    getInfoForOrder: (token) => {
        return axios.get('http://localhost:8000/user/info/order',
            {
                'headers': {'Authorization': token}
            })
    }
};

export default userInfoService;