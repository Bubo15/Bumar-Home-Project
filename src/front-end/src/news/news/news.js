import React from "react";
import './news.css'

const News = (props) => {

    return (
               <div>
                   <img className="img-news" src={props.img}  alt="img-news"/>
                   <h1>{props.title}</h1>
                   <p className="text">{props.text}</p>
               </div>
    )
};

export default News;