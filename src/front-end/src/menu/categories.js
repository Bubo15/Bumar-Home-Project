import React, {Component} from "react";
import MenuBar from "./menuBar";
import categoryService from "../service/category";

class Categories extends Component {

    constructor(props) {
        super(props);

        this.state = {
            data: []
        };
    }

    componentDidMount = () => {
        categoryService.getAllCategories.then(data => {
            this.setState({ data: data.data });
        });
    };

    render() {
        return (
            <div>
                <MenuBar token={this.props.token} data={this.state.data} isTouched={this.props.isTouched}/>
            </div>
        )
    }
}

export default Categories
