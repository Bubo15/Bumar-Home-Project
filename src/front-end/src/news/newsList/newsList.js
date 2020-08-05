/* eslint-disable no-undef */
import React, {Component} from "react";
import Header from "../../header/header";
import Footer from "../../footer/footer";
import './newsList.css'
import CreateNews from "../createNews/createNews";
import AddCircleOutlineIcon from '@material-ui/icons/AddCircleOutline';
import News from "../news/news";
import DeleteIcon from '@material-ui/icons/Delete';
import img from '../img/news.png'
import img1 from '../img/News-handing-1024x615.gif'
import newsService from "../../service/news";
import EditIcon from '@material-ui/icons/Edit';
import EditNews from "../edit/editNews";

class NewsList extends Component {

    constructor(props) {
        super(props);

        this.state = {
            newsId: null,
            news: [],
            showAdd: true,
            showEdit: true,
            hasRoleAdmin: this.props.roles.includes("ROLE_ADMIN"),
            hasRoleModerator: this.props.roles.includes("ROLE_MODERATOR")
        }
    }

    componentDidMount() {
        newsService.getAllNews.then(data => {
            this.setState({news: data.data});
        });
    }

    clickAdd = () => {
        this.setState({
            showAdd: !this.state.showAdd,
            showEdit: true
        })
    };

    clickEdit = (id) => {
        this.setState({
            showEdit: !this.state.showEdit,
            showAdd: true,
            newsId: id
        });
    };

    deleteNews = (id) => {
        newsService.deleteNews(id, this.props.token).then(data => {
            document.location = "/api/news"
        });
    };

    renderNews() {
        return this.state.news.map((news) => {
            const {title, text, id} = news;

            return (
                <div key={id}>
                    {(this.state.hasRoleAdmin || this.state.hasRoleModerator) &&
                    <EditIcon className="edit-icon-news" onClick={() => this.clickEdit(id)}/>}

                    <div className="news-data">
                        <News title={title} text={text} img={news.picture.url}/>
                    </div>

                    {this.state.hasRoleAdmin &&
                    <DeleteIcon onClick={() => this.deleteNews(id)} className="delete-icon-news"/>}
                </div>
            )
        })
    }

    render() {
        return (
            <div>
                <div className="header-news">
                    <Header/>
                </div>

                <h1 className="news-title">НОВИНИ {(this.state.hasRoleAdmin || this.state.hasRoleModerator) &&
                <AddCircleOutlineIcon onClick={this.clickAdd}/>}</h1>

                {this.state.news.length !== 0 ? this.renderNews() :
                    <h1 className="not-news-title">В МОМЕНТА НЯМА АКТУАЛНИ НОВИНИ</h1>}

                <img className="background-img-news" src={img} alt="news-img"/>
                <img className="background-img-news-1" src={img1} alt="news-img"/>

                {/*make it by this way, because, when i make with 2 divs,
                 always only in first can upload a picture, in second for example EditNews, doesnt work,
                 so i make it by this way
                 */}
                <div>
                    <div hidden={this.state.showAdd && this.state.showEdit} className="add-form-news">
                        {!this.state.showAdd ? <CreateNews token={this.props.token}/> : <EditNews token={this.props.token} id={this.state.newsId}/>}
                    </div>
                </div>

                <div className="news-footer">
                    <Footer/>
                </div>
            </div>
        )
    }
}

export default NewsList;