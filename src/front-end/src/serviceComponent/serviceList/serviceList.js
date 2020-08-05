import React, {Component} from "react";
import Header from "../../header/header";
import AddBoxIcon from '@material-ui/icons/AddBox';
import Footer from "../../footer/footer";
import './serviceList.css'
import CreateService from "../createService/createService";
import img from '../img/image_uslugi.jpg'
import DeleteIcon from '@material-ui/icons/Delete';
import Service from "../service/service";
import serviceService from "../../service/service";

class ServiceList extends Component {

    constructor(props) {
        super(props);

        this.state = {
            services: [],
            showAdd: true,
            serviceId: null,
            hasRoleAdmin: this.props.roles.includes("ROLE_ADMIN"),
            hasRoleModerator: this.props.roles.includes("ROLE_MODERATOR")
        }
    }

    componentDidMount() {
        serviceService.getAllServices.then(data => {
            this.setState({services: data.data});
        });
    }

    clickAdd = () => {
        this.setState({
            showAdd: !this.state.showAdd,
        })
    };

    deleteService = (id) => {
        serviceService.deleteService(id, this.props.token).then(data => {
            document.location = "/api/service"
        });
    };

    renderService = () => {
        return this.state.services.map((service) => {
            const {name, id} = service;
            return (
                <div key={id}>
                    <div className="service-data">
                        <Service name={name} img={service.picture.url}/>
                    </div>

                    {this.state.hasRoleAdmin && <DeleteIcon onClick={() => this.deleteService(id)} className="delete-icon-service"/>}
                </div>
            )
        })
    };

    render() {
        return (
            <div>
                <div className="header-service">
                    <Header/>
                </div>

                <img className="main-img-service" src={img} alt="service-img"/>

                <h1 className="service-title">УСЛУГИ {(this.state.hasRoleAdmin || this.state.hasRoleModerator) &&
                <AddBoxIcon onClick={this.clickAdd}/>}</h1>

                {this.state.services.length !== 0 ? this.renderService() : null}

                <div hidden={this.state.showAdd} className="add-form-service">
                    <CreateService token={this.props.token}/>
                </div>

                <div className="service-footer">
                    <Footer/>
                </div>
            </div>
        )
    }
}

export default ServiceList;