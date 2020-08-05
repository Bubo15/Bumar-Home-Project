import React, {Component} from "react";
import businessService from "../../service/business";
import Business from "../business/business";
import AddBoxIcon from '@material-ui/icons/AddBox';
import Header from "../../header/header";
import Footer from "../../footer/footer";
import img from '../img/business.jpeg'
import './businessList.css'
import DeleteIcon from '@material-ui/icons/Delete';
import EditIcon from '@material-ui/icons/Edit';
import CreateBusiness from "../createBusiness/createBusiness";
import EditBusiness from "../editBusiness/editBusiness";

const DELETE_BUSINESS_MESSAGE = "Сигурен ли сте, че искате да изтриете тази фирма. При изтриването ѝ, ще бъдат изтрити всички нейни продукти";

class BusinessList extends Component {

    constructor(props) {
        super(props);

        this.state = {
            businessId: null,
            businesses: [],
            showAdd: true,
            showEdit: true,
            hasRoleAdmin: this.props.roles.includes("ROLE_ADMIN"),
            hasRoleModerator: this.props.roles.includes("ROLE_MODERATOR")
        }
    }

    componentDidMount = async () => {
        await businessService.getAllBusiness
            .then(data => {
                if (JSON.stringify(data.data) !== JSON.stringify({})){
                    this.setState({businesses: data.data['_embedded'].businessResponseModelList})
                }
            });
    };

    clickAdd = () => {
        this.setState({
            showAdd: !this.state.showAdd,
        })
    };

    clickEdit = (id) => {
        this.setState({
            showEdit: !this.state.showEdit,
            businessId: id
        })
    };

    clickDelete = url => {
        if (window.confirm(DELETE_BUSINESS_MESSAGE)) {
            businessService.deleteBusiness(url, this.props.token).then(data => document.location = "/api/business");
        }
    };

    renderBusiness = () => {
         return this.state.businesses.map((business) => {
                const {name, logo, id, _links} = business;
                let link = "";

                if (_links['business-product'] !== undefined) {
                    link = business['_links']['business-product'].href.split('/');
                }

                return (
                    <div>
                        {(this.state.hasRoleAdmin || this.state.hasRoleModerator) && <EditIcon className="business-edit-btn" onClick={() => this.clickEdit(id)}/>}
                        <div key={id} className="business-data">
                            {
                                _links['business-product'] !== undefined
                                    ?
                                    <Business name={name} img={logo.url} link={`${link[4]}/${link[5]}`}/>
                                    :
                                    <Business name={name} img={logo.url}/>
                            }
                        </div>
                        {this.state.hasRoleAdmin && <DeleteIcon className="business-delete-btn" onClick={() => this.clickDelete(_links['business-delete'].href)}/>}
                    </div>
                )
            })
        };

    render() {
        return (
            <div>
                <div className="header-business">
                    <Header/>
                </div>
                <img className="main-img-business" src={img} alt="business-img"/>

                <h1 className="business-title"> НАШИТЕ ОСНОВНИ ПАРТНЬОРИ {(this.state.hasRoleAdmin || this.state.hasRoleModerator) &&
                <AddBoxIcon onClick={this.clickAdd}/>} </h1>

                <div hidden={this.state.showAdd && this.state.showEdit} className="add-form-business">
                    {!this.state.showAdd ? <CreateBusiness token={this.props.token}/> : <EditBusiness token={this.props.token} id={this.state.businessId}/>}
                </div>

                {this.state.businesses.length !== 0 ? this.renderBusiness() : <p1 className="business-have-not">В момента няма фирми с които си съдружничим</p1>}

                <div className={this.state.businesses.length === 0 ? "footer-business" : "footer-business-1"}>
                    <Footer/>
                </div>
            </div>
        )
    }
}

export default BusinessList;