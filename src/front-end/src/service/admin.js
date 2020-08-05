import axios from "axios";

const adminService = {

    addRoleToUser: (id, role, token) => {
        return axios.post(`http://localhost:8000/admin/add/user/${id}/${role}`, {},{
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token
            }
        })
    },

    removeRoleToUser: (id, role, token) => {
        return axios.post(`http://localhost:8000/admin/remove/user/${id}/${role}`, {},{
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token
            }
        })
    },

    block: (id, token) => {
        return axios.post(`http://localhost:8000/admin/block/user/${id}`, {},{
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token
            }
        })
    },


    unBlock: (id, token) => {
        return axios.post(`http://localhost:8000/admin/unblock/user/${id}`, {},{
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token
            }
        })
    }
};

export default adminService;