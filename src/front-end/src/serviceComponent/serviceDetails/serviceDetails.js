import {Redirect} from "react-router-dom";
import React, {Component} from "react";
import serviceService from "../../service/service";
import Header from "../../header/header";
import Footer from "../../footer/footer";
import './serviceDetails.css'
import Button from "@material-ui/core/Button";
import EditIcon from '@material-ui/icons/Edit';
import EditService from "../editService/editService";

class ServiceDetails extends Component {

    constructor(props) {
        super(props);

        this.state = {
            service: {},
            showEdit: true,
            isRedirect: false,
            isEnableToRender: false,
            name: document.location.pathname.substr(13),
            hasRoleAdmin: this.props.roles.includes("ROLE_ADMIN"),
            hasRoleModerator: this.props.roles.includes("ROLE_MODERATOR")
        };
    }

    componentDidMount() {
        serviceService.getServiceByName(this.state.name).then(data => {
            this.setState({service: data.data, isEnableToRender: true});
        }).catch((e) => {
            this.setState({isRedirect: true});
        });
    }

    renderService = () => {
        return (
            <div>
                <img className="img-service-details" src={this.state.service.picture.url} alt="img-news-details"/>
                <h1 className="name-service-details">{this.state.service.name} {(this.state.hasRoleAdmin || this.state.hasRoleModerator) && <EditIcon onClick={this.clickEdit}/>}</h1>
                <h2 className="description-service-details">{this.state.service.description}</h2>
            </div>
        )
    };

    goBack = () => {
        document.location.pathname = "/api/service";
    };

    clickEdit = () => {
        this.setState({showEdit: !this.state.showEdit})
    };

    render() {

        if (!this.state.isRedirect) {
            return (
                <div>
                    <div className="header-service-details">
                        <Header/>
                    </div>

                    {this.state.isEnableToRender ? this.renderService() : null}

                    <div hidden={this.state.showEdit} className="edit-form-service-details">
                        <EditService name={this.state.name} token={this.props.token}/>
                    </div>

                    <Button className="btn-service-details" onClick={() => this.goBack()} variant="contained"
                            color="primary">
                        ВЪРНИ СЕ ОБРАТНО
                    </Button>

                    <div className="footer-service-details">
                        <Footer/>
                    </div>
                </div>
            )
        } else {
            return (
                <div>
                    <Redirect to="/api/service"/>
                </div>
            )
        }
    }
}

export default ServiceDetails;