import axios from "axios";

const userService = {
    getAllUsers: (token) => {
        return axios.get("http://localhost:8000/user/all/users", {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token
            }
        })
    },

    login: ({username, password}) => {
        return axios.post("http://localhost:8000/user/login", {username, password},
            {
                headers: {
                    'Content-Type': 'application/json',
                }
            })
    },

    register: (values) => {
        return axios.post("http://localhost:8000/user/register", values,
            {
                headers: {
                    'Content-Type': 'application/json',
                }
            })
    },

    logout: () => {
        localStorage.setItem("auth", "null");
        localStorage.setItem("roles", []);
        document.location = "/";
    },

    getCurrentUserRoles: (token) => {
        return  axios.get("http://localhost:8000/user/roles",
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            })
    }
};

export default userService