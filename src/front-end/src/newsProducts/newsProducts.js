import React, {useEffect, useState} from "react";
import product from "../service/product";
import Product from "../product/product/product";
import GridList from "@material-ui/core/GridList";
import './newsProduct.css'
import Grid from "@material-ui/core/Grid";

const NewsProducts = () => {

    const [products, setProducts] = useState([]);

    const fetchData = async () => {
        await product.getNewProduct.then(data => {
            setProducts(data.data)
        })
    };

    useEffect(() => {
        fetchData()
    }, []);

    return (
        <div>
            {products.length === 3 ? <div>
                <h1 className="the-newest-product-title">НАЙ-НОВИ ПРОДУКТИ</h1>
                <br/>
                <br/>
                <br/>

                <div>
                    <Grid container>

                        <Product id={products[0].id} name={products[0].name} price={products[0].price}
                                 img={products[0].picture.url} countOfProduct={products[0].countOfProduct}/>

                        <Product id={products[1].id} name={products[1].name} price={products[1].price}
                                 img={products[1].picture.url} countOfProduct={products[1].countOfProduct}/>

                        <Product id={products[2].id} name={products[2].name} price={products[2].price}
                                 img={products[2].picture.url} countOfProduct={products[2].countOfProduct}/>

                    </Grid>
                </div>
            </div> : <div/>}
        </div>
    )
};

export default NewsProducts;