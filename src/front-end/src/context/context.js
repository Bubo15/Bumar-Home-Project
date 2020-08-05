import React, {createContext} from "react";

export const AuthContext = createContext({auth: "null", roles: []});

const WrappedContext = props => {

    return (
        <AuthContext.Provider value={{auth: localStorage.getItem("auth"), roles: localStorage.getItem("roles")}}>
            {props.children}
        </AuthContext.Provider>
    )
};

export default WrappedContext;