import React, {useEffect, useState} from "react";
import userService from "../service/auth";
import './admin.css'
import Button from "@material-ui/core/Button";
import Header from "../header/header";
import Footer from "../footer/footer";
import admin from "../service/admin";

const roles = ["ADMIN", "MODERATOR", "USER"];

const Admin = (props) => {

    const [allUsers, setAllUsers] = useState([]);

    const fetchData = async () => {
        await userService.getAllUsers(props.token).then(data => {
            setAllUsers(data.data)
        })
    };

    useEffect(() => {
        fetchData();
    }, []);

    const redirectTo = (url) => {
        document.location.pathname = "/admin";
    };

    const addRoleToUser = (role, userId) => {
        admin.addRoleToUser(userId, role, props.token).then(data => redirectTo("/admin"));
    };

    const removeRoleFromUser = (role, userId) => {
        if (role === "ADMIN") {
            if (props.roles.includes("ROLE_OWNER")) {
                admin.removeRoleToUser(userId, role, props.token).then(data => redirectTo("/admin"))
            } else {
                alert("Само Owner има право да премахва роля Admin")
            }
        } else {
            admin.removeRoleToUser(userId, role, props.token).then(data => redirectTo("/admin"))
        }
    };

    const mainBlockAndUnBlock = (userId, authorities, isBlock) => {
        if (authorities.map(role => role.authority.substring(5)).includes("ADMIN")) {
            if (props.roles.includes("ROLE_OWNER")) {
                isBlock ? admin.block(userId, props.token).then(data => redirectTo("/admin")) :
                    admin.unBlock(userId, props.token).then(data => redirectTo("/admin"))
            } else {
                alert("Само Owner има право да блокира юзър с роля Admin")
            }
        } else {
            isBlock ? admin.block(userId, props.token).then(data => redirectTo("/admin")) :
                admin.unBlock(userId, props.token).then(data => redirectTo("/admin"))
        }
    };

    const renderTableData = () => {
        return allUsers.map((user) => {
            const {id, username, authorities, enabled} = user;
            const currentRoles = [];

            authorities.map(role => {
                currentRoles.push(role.authority.substring(5));
            });

            const freeRoles = roles.filter(role => !currentRoles.includes(role)).map(role => <Button key={id}
                                                                                                     color="primary"
                                                                                                     onClick={() => addRoleToUser(role, id)}>{role}</Button>);
            const currentRolesView = currentRoles.map(role => <Button key={username} color="primary"
                                                                      onClick={() => removeRoleFromUser(role, id, authorities)}>{role}</Button>);

            return (
                <tr key={id}>
                    <td>{id}</td>
                    <td>{username}</td>
                    <td>{currentRolesView}</td>
                    <td>{(freeRoles.length > 0 && !currentRoles.includes("ADMIN")) ? freeRoles : 'Няма по-голяма роля от Admin'}</td>
                    <td>{!enabled ?
                        <Button onClick={() => mainBlockAndUnBlock(id, authorities, false)}>ОТБЛОКИРАЙ</Button> :
                        <Button onClick={() => mainBlockAndUnBlock(id, authorities, true)}>БЛОКИРАЙ</Button>}</td>
                </tr>
            )
        })
    };

    return (
        <div>
            <Header/>

            <h1 className="title-admin-users">ПОТРЕБИТЕЛИ</h1>
                <table className="users">
                    <tbody>
                    <tr className="table-head">
                        <td>id</td>
                        <td>username</td>
                        <td>настоящи роли</td>
                        <td>свободни роли</td>
                        <td>състояние</td>
                    </tr>
                    {renderTableData()}
                    </tbody>
                </table>

            <div className="footer-admin">
                <Footer/>
            </div>
        </div>
    )
};


export default Admin;