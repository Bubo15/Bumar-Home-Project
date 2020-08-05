import React, {Component} from "react";
import './footer.css'
import {Link} from "react-router-dom";
import ImportContactsIcon from '@material-ui/icons/ImportContacts';
import RoomServiceIcon from '@material-ui/icons/RoomService';
import BusinessCenterIcon from '@material-ui/icons/BusinessCenter';
import CommentIcon from '@material-ui/icons/Comment';
import FacebookIcon from '@material-ui/icons/Facebook';
import InstagramIcon from '@material-ui/icons/Instagram';
import YouTubeIcon from '@material-ui/icons/YouTube';
import TwitterIcon from '@material-ui/icons/Twitter';
import PinterestIcon from '@material-ui/icons/Pinterest';

const Footer = () => {
    return (
        <footer className="footer">
            <div>
                <hr className="hr-footer"/>

                <div>
                    <Link to="/api/news">
                        <ImportContactsIcon className="news-icon"/>
                    </Link>
                    <h1 className="news-footer-title"> НОВИНИ</h1>
                </div>

                <div>
                    <Link to="/api/business">
                        <BusinessCenterIcon className="business-icon"/>
                    </Link>
                    <h1 className="business-footer-title"> ПАРТНЬОРИ</h1>
                </div>

                <div>
                    <Link to="/api/service">
                        <RoomServiceIcon className="service-icon"/>
                    </Link>
                    <h1 className="service-footer-title"> УСЛУГИ</h1>
                </div>

                <div>
                    <Link to="/api/comment">
                        <CommentIcon className="comment-icon"/>
                    </Link>
                    <h1 className="comment-footer-title"> КОМЕНТАРИ</h1>
                </div>

                <div className="follow-us">
                    <h3>СЛЕДВАЙТЕ НИ НА</h3>
                    <div className="app-footer">
                        <a href="https://www.facebook.com/BUMAR-HOME-614020522588480"><FacebookIcon style={{"color": "blue"}}/></a>
                        <a href="https://www.instagram.com/explore/tags/bumarhome/?hl=bg"><InstagramIcon style={{"color": "purple"}}/></a>
                        <a href="https://www.youtube.com"><YouTubeIcon style={{"color": "red"}}/></a>
                        <a href="https://twitter.com"><TwitterIcon style={{"color": "dodgerblue"}}/></a>
                        <a href="https://www.pinterest.es"><PinterestIcon style={{"color": "red"}}/></a>
                    </div>
                </div>

                <div className="info-contact">
                    <h3>КОНТАКТИ</h3>
                    <h5 style={{"font-size": "15px"}}>+359 15 088 812</h5>
                    <h5 style={{"font-size": "15px"}}>bumarhome@gmail.com</h5>
                </div>

            </div>
        </footer>
    )
};


export default Footer;