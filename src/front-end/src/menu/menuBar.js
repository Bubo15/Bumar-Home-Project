import React, {Component} from 'react'
import List from '@material-ui/core/List'
import ListItem from '@material-ui/core/ListItem'
import ListItemText from '@material-ui/core/ListItemText'
import Collapse from '@material-ui/core/Collapse'
import ExpandLess from '@material-ui/icons/ExpandLess'
import ExpandMore from '@material-ui/icons/ExpandMore'
import Drawer from '@material-ui/core/Drawer'
import {withStyles} from '@material-ui/core/styles'
import Button from "@material-ui/core/Button";
import CreateCategory from "../category/createCategory";
import DeleteIcon from '@material-ui/icons/Delete';
import EditIcon from '@material-ui/icons/Edit';
import categoryService from "../service/category";
import EditCategory from "../category/editCategory";
import CreateSubcategory from "../subcategory/createSubcategory";
import EditSubcategory from "../subcategory/editSubcategory";
import subcategoryService from "../service/subcategory";

const styles = {
    list: {
        width: 270,
    },
    links: {
        textDecoration: 'none',
    },
    menuHeader: {
        paddingLeft: '75px',
        top: '-1px'
    }
};

class MenuBar extends Component {
    constructor(props) {
        super(props);

        this.state = {
            categoryName: "",
            subcategoryName: "",
            showAddCategory: true,
            showEditCategory: true,
            showAddSubcategory: true,
            showEditSubcategory: true,
            subcategoryCategoryName: "",
            hasRoleAdmin: localStorage.getItem("roles").includes("ROLE_ADMIN"),
            hasRoleModerator: localStorage.getItem("roles").includes("ROLE_MODERATOR")
        };

    }

    handleClick(item) {
        this.setState(prevState => (
            {[item]: !prevState[item]}
        ))
    }

    clickAddCategory = () => {
        if (document.location.pathname !== "/") {
            document.location.pathname = "/"
        }

        this.setState({
            showAddCategory: !this.state.showAddCategory,
            showEditCategory: true,
            showAddSubcategory: true,
            showEditSubcategory: true
        })
    };

    clickEditCategory = (name) => {
        if (document.location.pathname !== "/") {
            document.location.pathname = "/"
        }else {
            this.setState({
                showEditCategory: !this.state.showEditCategory,
                showAddCategory: true,
                showAddSubcategory: true,
                showEditSubcategory: true,
                categoryName: name
            })
        }
    };

    deleteCategory = (name) => {
        if (document.location.pathname !== "/") {
            document.location.pathname = "/"
        }else {
            if (window.confirm("Сигурен ли сте, че искате да изтриете тази категория. При изтриването ѝ, ще бъдат изтрити всички подкатегории и техните продукти")) {
                categoryService.delete(name, this.props.token).then(data => document.location.pathname = "/")
            }
        }
    };


    clickAddSubcategory = () => {
        if (document.location.pathname !== "/") {
            document.location.pathname = "/"
        }else {
            this.setState({
                showAddCategory: true,
                showEditCategory: true,
                showAddSubcategory: !this.state.showAddSubcategory,
                showEditSubcategory: true
            })
        }
    };


    clickEditSubcategory = (name, category) => {
        if (document.location.pathname !== "/") {
            document.location.pathname = "/"
        }

        this.setState({
            showEditCategory: true,
            showAddCategory: true,
            showAddSubcategory: true,
            showEditSubcategory: !this.state.showEditSubcategory,
            subcategoryName: name,
            subcategoryCategoryName: category
        })
    };

    deleteSubcategory = (name) => {
        if (document.location.pathname !== "/") {
            document.location.pathname = "/"
        }else {
            if (window.confirm("Сигурен ли сте, че искате да изтриете тази подкатегория. При изтриването ѝ, ще бъдат изтрити всички нейни продукти")) {
                subcategoryService.deleteSubcategory(name, this.props.token).then(data => document.location.pathname = "/")
            }
        }
    };

    goCategory = (url) => {
        document.location.pathname = url
    };

    goSubcategory = (url) => {
        document.location.pathname = url;
    };

    handler(subCategory) {

        if (subCategory === undefined) {
            subCategory = [];
        }

        const {classes} = this.props;
        const {state} = this;

        return subCategory.map((subOption) => {

            const categoryName = subOption.url.split("/")[1];
            const subCategory = subOption.url.split("/")[2];

            if (!subOption.subCategory) {
                return (
                    <div key={subOption.name}>
                        <ListItem
                            button
                            key={subOption.name}>

                            {this.state.hasRoleAdmin &&
                            <DeleteIcon
                                onClick={() => !subOption.subCategory ? this.deleteSubcategory(subOption.name) : this.deleteCategory(subOption.name)}/>}
                            {(this.state.hasRoleAdmin || this.state.hasRoleModerator) &&
                            <EditIcon onClick={() => !subOption.subCategory ? this.clickEditSubcategory(subOption.name, categoryName) : this.clickEditCategory(subOption.name)}/>}

                            <ListItemText onClick={() => this.goSubcategory('/category/' + categoryName + "/subcategory/" + subCategory)} inset primary={subOption.name}/>
                        </ListItem>
                    </div>
                )
            }

            return (
                <div key={subOption.name}>
                    <ListItem
                        button
                        onClick={() => this.handleClick(subOption.name)}>

                        {this.state.hasRoleAdmin && <DeleteIcon onClick={() => this.deleteCategory(subOption.name)}/>}
                        {(this.state.hasRoleAdmin || this.state.hasRoleModerator) && <EditIcon onClick={() => this.clickEditCategory(subOption.name)}/>}

                        <ListItemText
                            onClick={() => this.goCategory("/category/" + subOption.name)}
                            inset
                            primary={subOption.name}/>
                        {state[subOption.name] ? <ExpandLess/> : <ExpandMore/>}
                    </ListItem>
                    <Collapse
                        in={state[subOption.name]}
                        timeout="auto"
                        unmountOnExit>
                        {this.handler(subOption.subCategory)}
                    </Collapse>
                </div>
            )
        })
    }

    render() {
        const {classes} = this.props;

        if (this.props.isTouched) {
            return (
                <div className={classes.list}>
                    <Drawer
                        variant="persistent"
                        anchor="left"
                        open
                        classes={{paper: classes.list}}>
                        <div>
                            <List>
                                <ListItem
                                    key="menuHeading"
                                    divider
                                    disableGutters>
                                    <ListItemText
                                        className={classes.menuHeader}
                                        inset
                                        primary="BUMAR MENU"
                                    />
                                </ListItem>
                                {this.handler(this.props.data)}
                            </List>
                        </div>

                        {(this.state.hasRoleAdmin || this.state.hasRoleModerator) &&
                        <Button onClick={this.clickAddCategory} fullWidth variant="contained" color="primary">
                            ДОБАВИ КАТЕГОРИЯ
                        </Button>}

                        <br/>

                        {(this.state.hasRoleAdmin || this.state.hasRoleModerator) &&
                        <Button onClick={this.clickAddSubcategory} fullWidth variant="contained" color="primary">
                            ДОБАВИ ПОДКАТЕГОРИЯ
                        </Button>}

                        <div hidden={this.state.showAddCategory}>
                            <CreateCategory token={this.props.token}/>
                        </div>

                        <div hidden={this.state.showEditCategory}>
                            <EditCategory name={this.state.categoryName} token={this.props.token}/>
                        </div>

                        <div hidden={this.state.showAddSubcategory}>
                            <CreateSubcategory token={this.props.token}/>
                        </div>

                        <div hidden={this.state.showEditSubcategory}>
                            <EditSubcategory category={this.state.subcategoryCategoryName} name={this.state.subcategoryName} token={this.props.token}/>
                        </div>

                    </Drawer>
                </div>
            )
        } else {
            return (
                <div/>
            )
        }
    }
}

export default withStyles(styles)(MenuBar)