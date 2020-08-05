import React, {Component} from 'react'
import Categories from "../menu/categories";
import "./index.css"
import DehazeIcon from '@material-ui/icons/Dehaze';

const isTouchedFunc = {
    false: (handleOutsideClick) => document.removeEventListener('mouseover', handleOutsideClick, false),
    true: (handleOutsideClick) => document.addEventListener('mouseover', handleOutsideClick, false)
};

class Title extends Component {

    constructor(props) {
        super(props);

        this.state = {
            isTouched: this.props.isTouched
        }
    }

    handleClick = () => {
        isTouchedFunc[!this.state.isTouched](this.handleOutsideClick);

        this.setState(prevState => ({
            isTouched: !prevState.isTouched,
        }));

    };

    handleOutsideClick = (e) => {
        if (this.node === null || this.node.contains(e.target)) {
            return;
        }
        this.handleClick();
    };

    render() {
        return (
            <div ref={node => {this.node = node;}}>
                <DehazeIcon className="icon-siteName"/>

                <h1 className="title" onMouseOver={this.handleClick}>BUMAR HOME</h1>

                <Categories token={this.props.token} isTouched={this.state.isTouched}/>
            </div>
        )
    }
}

export default Title