import React, {useEffect, useState} from "react";
import Header from "../../header/header";
import Footer from "../../footer/footer";
import './commentList.css'
import commentService from "../../service/comment";
import Paper from "@material-ui/core/Paper";
import ClearIcon from '@material-ui/icons/Clear';
import Edit from '@material-ui/icons/Edit';
import Button from "@material-ui/core/Button";
import userInfoService from "../../service/userInfo";
import CreateComment from "../createComment/createComment";
import img from '../img/comment.jpg'
import EditComment from "../editComment/editComment";

const CommentList = (props) => {

    const [comments, setComments] = useState([]);
    const [user, setUser] = useState({});
    const [showCreate, setShowCreate] = useState(true);
    const [showEdit, setShowEdit] = useState(true);
    const [id, setId] = useState(null);

    const fetchData = async () => {
        await commentService.getAllComments.then(data => {
            setComments(data.data);

            userInfoService.getInfo(props.token).then(data => {
                setUser(data.data);
                console.log(data.data)
            })
        })
    };

    useEffect(() => {
        fetchData()
    }, []);


    const deleteComment = (id) => {
        commentService.deleteComment(id, props.token).then(data => document.location.pathname = "/api/comment")
    };

    const showCreateForm = () => {
        setShowCreate(!showCreate);
        setShowEdit(true);
    };

    const showEditForm = (id) => {
        setShowEdit(!showEdit);
        setShowCreate(true);
        setId(id);
    };

    const getComments = () => {
        let listOfComments = [];

        comments.map(comment => {

            listOfComments.push(
                <div>
                    <Paper style={{'background-color': 'antiquewhite', '-webkit-box-shadow': '1px 1px 2px 4px #ccc'}}
                           className="comments">
                        {user.username === comment.user.username &&
                        <ClearIcon onClick={() => deleteComment(comment.id)} className="comment-delete-btn"/>}
                        <h3 className="comment-username-column">{comment.user.username}</h3>
                        <text className="comment-description-column">{comment.description}</text>
                        {user.username === comment.user.username && <Edit onClick={() => showEditForm(comment.id)} className="edit-delete-btn"/>}
                    </Paper>
                    <br/>
                    <br/>
                </div>
            )
        });

        return listOfComments;
    };


    return (
        <div>
            <div className="comment-header">
                <Header/>
            </div>

            <img className="img-comment" src={img}/>

            <h1 className="comment-title">КОМЕНТАРИ</h1>

            {comments.length > 0 ? getComments() : <h1 style={{"position": "relative", "left": "750px", "top": "500px"}}>В момента няма коментари</h1>}

            <Button
                onClick={() => showCreateForm()}
                style={{
                    "top": "100px",
                    "left": "800px",
                    "position": "relative",
                    "width": "300px",
                    "color": "black",
                    "background-color": "antiquewhite"
                }}>
                КОМЕНТИРАЙ
            </Button>


            <div style={{"position": "relative", "top": "200px", "left": "20px"}} hidden={showCreate}>
                <CreateComment token={props.token}/>
            </div>

            <div style={{"position": "relative", "top": "200px", "left": "20px"}} hidden={showEdit}>
                <EditComment token={props.token} id={id}/>
            </div>

            <div className="comment-footer">
                <Footer/>
            </div>
        </div>
    )
};

export default CommentList;